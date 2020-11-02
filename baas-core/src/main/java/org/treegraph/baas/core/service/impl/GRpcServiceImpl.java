package org.treegraph.baas.core.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import conflux.web3j.grpc.GrpcWeb3j;
import jodd.io.FileUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.treegraph.baas.core.component.BizTimer;
import org.treegraph.baas.core.das.KeypairDas;
import org.treegraph.baas.core.das.NodeCustomizedConfigDas;
import org.treegraph.baas.core.das.bo.NodeKeypairBo;
import org.treegraph.baas.core.das.bo.NodeKeypairInfoBo;
import org.treegraph.baas.core.domain.KeypairPo;
import org.treegraph.baas.core.domain.NodePo;
import org.treegraph.baas.core.enums.CryptoEnum.KeyType;
import org.treegraph.baas.core.enums.HeartbeatEnum;
import org.treegraph.baas.core.enums.MembershipEnum;
import org.treegraph.baas.core.exception.AlertException;
import org.treegraph.baas.core.heartbeat.bo.Heartbeat;
import org.treegraph.baas.core.heartbeat.impl.HeartbeatListener;
import org.treegraph.baas.core.service.BlockchainRpcService;
import org.treegraph.baas.core.service.bo.GRpcKeypairCertBo;
import org.treegraph.baas.core.service.bo.GRpcParaBo;
import org.treegraph.baas.core.task.CheckMembershipChangeTimerTask;
import org.treegraph.baas.core.util.ConfluxUtils;
import org.treegraph.baas.core.util.IdWorker;
import org.treegraph.baas.core.util.crypto.x509.KeyUtil;
import org.web3j.crypto.ECKeyPair;

import java.io.File;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.treegraph.baas.core.constant.ConfluxConstant.*;
import static org.treegraph.baas.core.constant.GRpcConstant.*;
import static org.treegraph.baas.core.constant.X509CertConstant.ALGO_CONS;
import static org.treegraph.baas.core.constant.X509CertConstant.ALGO_NODE;
import static org.treegraph.baas.core.util.crypto.x509.KeyUtil.privateKeyFromBase64;
import static org.treegraph.baas.core.util.crypto.x509.KeyUtil.privateKeyToPem;

@Service(value = "rpcService")
//@Service(value = "gRpcService")
@AllArgsConstructor
@Slf4j
public class GRpcServiceImpl implements BlockchainRpcService{
	
    private final KeypairDas keypairDas;
    
    private final NodeCustomizedConfigDas nodeCustomizedConfigDas;
    
    private final VaultKeypairBuildService vaultKeypairBuildService;
	
	public MembershipEnum.Status sendMembershipChangeTransactionPlus(long membershipChangeId,
			List<NodeKeypairBo> committeeNodeKeypairs, List<NodeKeypairBo> validatorNodeKeypairs) {
    	MembershipEnum.Status status;
    	Long seq = IdWorker.generateId();

    	//1. select a proposer in committee
    	GrpcWeb3j cfx;
    	NodeKeypairBo proposerNodeKeypair;
    	try {
			proposerNodeKeypair = committeeNodeKeypairs.stream()
					.filter(item -> HeartbeatEnum.Status.GREEN == HeartbeatListener.getInstance()
							.getNode(item.getNode().getConsortiumId(), item.getNode().getId()).getStatus())
					.findFirst().orElseThrow(() -> new AlertException("alive.proposer.node.not.found"));
			NodePo proposerNode = proposerNodeKeypair.getNode();
			GRpcParaBo rpcPara = getGRpcParas(proposerNode.getConsortiumId(), proposerNode.getId());
			cfx = new GrpcWeb3j(rpcPara.getIp(), rpcPara.getGRpcPort(), rpcPara.getServerCertFile(),
					rpcPara.getRpcCertChainFile(), rpcPara.getRpcKeyFile());
		} catch(Exception e) {
    		log.error("[seq={}][membership change]select proposer error:", seq, e);
    		return MembershipEnum.Status.SELECT_PROPOSER_FAILURE;
    	} finally {
    		log.info("[seq={}][membership change]select proposer success!", seq);
    	}

    	//2. RPC name:cfx_getMembershipChangeRawTransaction
		// input :
		// ---Vector<public key> new_validators,// all validators in the next consensus round
		// ---public_key proposer
		// output:
		// ---string raw_json
    	String applyResponse = null;
    	try {
			List<String> validatorPublicKeys = validatorNodeKeypairs.stream().map(item -> {
				ECKeyPair ecKeypair = KeyUtil.loadECKeyPairFromBase64(item.getKeypair().getPublicKey(),
						item.getKeypair().getPrivateKey(), item.getKeypair().getAlgorithm());
				return "0x"+StringUtils.leftPad(ecKeypair.getPublicKey().toString(16), 128, '0');
			}).collect(Collectors.toList());
			ECKeyPair proposerEcKeypair = KeyUtil.loadECKeyPairFromBase64(
					proposerNodeKeypair.getKeypair().getPublicKey(), proposerNodeKeypair.getKeypair().getPrivateKey(),
					proposerNodeKeypair.getKeypair().getAlgorithm());
			String proposerPublicKey = "0x"+StringUtils.leftPad(proposerEcKeypair.getPublicKey().toString(16), 128, '0');
			applyResponse = cfx.getMembershipChangeRawTransaction(validatorPublicKeys, proposerPublicKey);
			Assert.isTrue(applyResponse != null, "membership change error");
		} catch(Exception e) {
    		log.error("[seq={}][membership change]request change error:", seq, e);
    		return MembershipEnum.Status.REQUEST_MEMBERSHIP_CHANGE_FAILURE;
    	} finally {
			log.info("[seq={}][membership change]txResponse--->{}", seq, applyResponse);
    	}
		
    	// 3. Keccak(raw_json) and ring signature
		// input :
		// ---Vector<(public key,private key)>, secp256k1(Keccak(raw_json))
		// output:
		// ---Vector<signature>
    	Map<KeypairPo, String> ringSignatureMap = Maps.newHashMap();
    	try {
			byte[] rawJsonBytes = applyResponse.getBytes(StandardCharsets.UTF_8);
			committeeNodeKeypairs.forEach(item -> {
				ECKeyPair ecKeypair = KeyUtil.loadECKeyPairFromBase64(item.getKeypair().getPublicKey(),
						item.getKeypair().getPrivateKey(), item.getKeypair().getAlgorithm());
				String signature = ConfluxUtils.signMessage(rawJsonBytes, ecKeypair);
				
				ringSignatureMap.put(item.getKeypair(), signature);
			});
		} catch(Exception e) {
    		log.error("[seq={}][membership change]ring signature error:", seq, e);
    		return MembershipEnum.Status.SIGNATURE_MEMBERSHIP_FAILURE;
    	} finally {
			log.info("[seq={}][membership change]ringSignatureMap--->{}", seq, JSON.toJSONString(ringSignatureMap));
    	}

		//4. RPC name: cfx_constructAndSendNewConsortiumMembershipTrans
		//input :
		//---string raw_json
		//---Vector<public key>
		//---Vector<signature>
		//output:
		//---string okay/fail
    	try {
			List<String> committeePublicKeys = Lists.newArrayList();
			List<String> ringSignatures = Lists.newArrayList();
			ringSignatureMap.forEach((key, value) -> {
				ECKeyPair ecKeypair = KeyUtil.loadECKeyPairFromBase64(key.getPublicKey(),
						key.getPrivateKey(), key.getAlgorithm());
				committeePublicKeys.add("0x" + StringUtils.leftPad(ecKeypair.getPublicKey().toString(16), 128, '0'));
				ringSignatures.add(value.substring(2));
			});
			cfx.constructAndSendNewConsortiumMemberTrans(applyResponse, committeePublicKeys, ringSignatures);
		} catch(Exception e) {
    		log.error("[seq={}][membership change]confirm change error:", seq, e);
    		return MembershipEnum.Status.CONFIRM_MEMBERSHIP_CHANGE_FAILURE;
    	} finally {
			log.info("[seq={}][membership change]txResponse--->null", seq);
    	}
    	
    	//5. delay query BFT epoch number
    	try {
			BizTimer.DELAY_TIMER.newTimeout(
					new CheckMembershipChangeTimerTask(membershipChangeId), 
					60, TimeUnit.SECONDS);
    		status = MembershipEnum.Status.LAUNCH_CHECK_THREAD_SUCCESS;
    	} catch(Exception e) {
    		log.error("[seq={}][membership change]async check error:", seq, e);
    		return MembershipEnum.Status.LAUNCH_CHECK_THREAD_FAILURE;
    	} finally {
    		log.info("[seq={}][membership change]async check started!", seq);
    	}

    	return status;
	}
    
	public BigInteger getBftEpochNumber(String consId, String nodeId) {
		BigInteger getBftEpochNumberResponse = null;
		try {
			GRpcParaBo rpcPara = getGRpcParas(consId, nodeId);
			GrpcWeb3j cfx = new GrpcWeb3j(rpcPara.getIp(), rpcPara.getGRpcPort(), rpcPara.getServerCertFile(),
					rpcPara.getRpcCertChainFile(), rpcPara.getRpcKeyFile());
			long membershipId = cfx.getBftMembershipId();
			getBftEpochNumberResponse = new BigInteger(Long.toString(membershipId));
		} catch(Exception e) {
			log.error("[get bftEpochNumber]error:", e);
			return BigInteger.valueOf(-1L);
		} finally {
			log.info("[get bftEpochNumber]txResponse--->{}", getBftEpochNumberResponse);
		}
		return getBftEpochNumberResponse;
	}
	
	public boolean updateCert(String consId, String consCertPem, List<String> membershipNodeIds) {
		try {
			String proposerNodeId = membershipNodeIds.stream()
					.filter(item -> HeartbeatListener.getInstance().getNode(consId, item)
							.getStatus() == HeartbeatEnum.Status.GREEN)
					.findFirst().orElseThrow(() -> new AlertException("alive.proposer.node.not.found"));
			NodeKeypairInfoBo proposerKeypair = keypairDas.findAll(consId, KeyType.NODE.getCode(), ALGO_NODE,
					Lists.newArrayList(proposerNodeId)).get(0);
			String grpcKeyPem = privateKeyToPem(privateKeyFromBase64(ALGO_NODE, proposerKeypair.getPrivateKey()));
			String grpcCertPem = proposerKeypair.getCertStr();
			
			GRpcParaBo rpcPara = getGRpcParas(consId, proposerNodeId);
			GrpcWeb3j cfx = new GrpcWeb3j(rpcPara.getIp(), rpcPara.getGRpcPort(), rpcPara.getServerCertFile(),
					rpcPara.getRpcCertChainFile(), rpcPara.getRpcKeyFile());
			cfx.updateConsortiumAndGrpcCredential(consCertPem, grpcKeyPem, grpcCertPem);
		} catch(Exception e) {
			log.error("[update credential]error:", e);
			return Boolean.FALSE;
		} finally {
			log.info("[update credential]txResponse--->null");
		}
		return Boolean.TRUE;
	}
	
	public String sendRawTransaction(String consId, String nodeId, String hexEncoded) {
		String txResult = null;
		try {
			GRpcParaBo rpcPara = getGRpcParas(consId, nodeId);
			GrpcWeb3j cfx = new GrpcWeb3j(rpcPara.getIp(), rpcPara.getGRpcPort(), rpcPara.getServerCertFile(),
					rpcPara.getRpcCertChainFile(), rpcPara.getRpcKeyFile());
			txResult = cfx.sendRawTransaction(hexEncoded);
		} catch(Exception e) {
			log.error("[get sendRawTransaction]error:", e);
		} finally {
			log.info("[get sendRawTransaction]txResponse--->{}", txResult);
		}
		return txResult;
	}
	
	private GRpcParaBo getGRpcParas(String consId, String nodeId) throws Exception {
		// get ip and gRpcPort
		Heartbeat heartbeat = HeartbeatListener.getInstance().getNode(consId, nodeId);
		String ip = heartbeat.getIp();
		String jsonRpcLocalHttpPortStr = nodeCustomizedConfigDas.findPropertyValueByName(consId, nodeId,
				PROPERTY_JSONRPC_LOC_PORT);
		int jsonRpcLocalHttpPort = Integer.parseInt(jsonRpcLocalHttpPortStr);
		int gRpcPort = TEST_SWITCH ? jsonRpcLocalHttpPort + TEST_RPC_PORT_INTERVAL : DEFAULT_GRPC_PORT;
		
		// server cert and rpc cert
		KeypairPo consKeypair = vaultKeypairBuildService.getConsortiumKeyPair(consId, null);
		String serverCertFile = consKeypair.getCertStr();
		String serverKeyFile = privateKeyToPem(privateKeyFromBase64(ALGO_CONS, consKeypair.getPrivateKey()));
		GRpcKeypairCertBo gRpcKeypair = vaultKeypairBuildService.geneNewRpcKeypair(consId, consKeypair, null,
				Lists.newArrayList(ip));
		
		// write to host
		String nodeCertPath = MessageFormat.format(NODE_CERT_Path, consId, nodeId);
		Map<String, String> certPathContentMap = Maps.newHashMap();
		certPathContentMap.put(SERVER_CERT_FILE, serverCertFile);
		certPathContentMap.put(SERVER_KEY_FILE, serverKeyFile);
		certPathContentMap.put(RPC_CERT_CHAIN_FILE, gRpcKeypair.getCert());
		certPathContentMap.put(RPC_KEY_FILE, gRpcKeypair.getPrivateKey());
		log.info("[get getGRpcParas]certPathContentMap--->{}", JSON.toJSONString(certPathContentMap));
		for (Entry<String, String> entry : certPathContentMap.entrySet()) {
			String file = entry.getKey();
			String content = entry.getValue();
			if (!FileUtil.isExistingFolder(new File(nodeCertPath))) {
				FileUtil.mkdirs(nodeCertPath);
			}
			if (!FileUtil.isExistingFolder(new File(nodeCertPath + file))) {
				FileUtil.writeBytes(nodeCertPath + file, content.getBytes(StandardCharsets.UTF_8));
			}
		}
		
		GRpcParaBo gRpcParaBo =  GRpcParaBo.builder()
				.ip(ip)
				.gRpcPort(gRpcPort)
				.serverCertFile(MessageFormat.format(nodeCertPath + SERVER_CERT_FILE, consId, nodeId))
				.rpcCertChainFile(MessageFormat.format(nodeCertPath + RPC_CERT_CHAIN_FILE, consId, nodeId))
				.rpcKeyFile(MessageFormat.format(nodeCertPath + RPC_KEY_FILE, consId, nodeId)).build();
		log.info("[get getGRpcParas]gRpcParaBo--->{}", JSON.toJSONString(gRpcParaBo));
		return gRpcParaBo;
	}
}
