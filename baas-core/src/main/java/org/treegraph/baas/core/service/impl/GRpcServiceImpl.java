package org.treegraph.baas.core.service.impl;

import static org.treegraph.baas.core.constant.ConfluxConstant.DEFAULT_GRPC_PORT;
import static org.treegraph.baas.core.constant.ConfluxConstant.PROPERTY_JSONRPC_LOC_PORT;
import static org.treegraph.baas.core.constant.ConfluxConstant.TEST_RPC_PORT_INTERVAL;
import static org.treegraph.baas.core.constant.ConfluxConstant.TEST_SWITCH;
import static org.treegraph.baas.core.constant.GRpcConstant.NODE_CERT_Path;
import static org.treegraph.baas.core.constant.GRpcConstant.RPC_CERT_CHAIN_FILE;
import static org.treegraph.baas.core.constant.GRpcConstant.RPC_KEY_FILE;
import static org.treegraph.baas.core.constant.GRpcConstant.SERVER_CERT_FILE;
import static org.treegraph.baas.core.constant.GRpcConstant.SERVER_KEY_FILE;
import static org.treegraph.baas.core.constant.X509CertConstant.ALGO_CONS;
import static org.treegraph.baas.core.constant.X509CertConstant.ALGO_NODE;
import static org.treegraph.baas.core.util.crypto.x509.KeyUtil.privateKeyFromBase64;
import static org.treegraph.baas.core.util.crypto.x509.KeyUtil.privateKeyToPem;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
import org.treegraph.baas.core.heartbeat.impl.HeartbeatLinstener;
import org.treegraph.baas.core.service.BlockchainRpcService;
import org.treegraph.baas.core.service.bo.GRpcParaBo;
import org.treegraph.baas.core.task.CheckMembershipChangeTimerTask;
import org.treegraph.baas.core.util.ConfluxUtils;
import org.treegraph.baas.core.util.IdWorker;
import org.treegraph.baas.core.util.crypto.x509.KeyUtil;
import org.web3j.crypto.ECKeyPair;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import conflux.web3j.grpc.GrpcWeb3j;
import jodd.io.FileUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service(value = "rpcService")
//@Service(value = "gRpcService")
@AllArgsConstructor
@Slf4j
public class GRpcServiceImpl implements BlockchainRpcService{
	
    private KeypairDas keypairDas;
    
    private NodeCustomizedConfigDas nodeCustomizedConfigDas;
    
    private VaultKeypairBuildService vaultKeypairBuildService;
	
	public MembershipEnum.Status sendMembershipChangeTransactionPlus(long membershipChangeId,
			List<NodeKeypairBo> committeeNodeKeypairs, List<NodeKeypairBo> validatorNodeKeypairs) throws IOException {
    	MembershipEnum.Status status = null;
    	Long seq = IdWorker.generateId();
    	/**
    	 * 1. select a proposer in committee
    	 */
    	GrpcWeb3j cfx = null;
    	NodeKeypairBo proposerNodeKeypair = null;
    	try {
			proposerNodeKeypair = committeeNodeKeypairs.stream()
					.filter(item -> HeartbeatEnum.Status.GREEN == HeartbeatLinstener.getInstance()
							.getNode(item.getNode().getConsortiumId(), item.getNode().getId()).getStatus())
					.findFirst().orElseThrow(() -> new AlertException("alive.proposer.node.not.found"));
			NodePo proposerNode = proposerNodeKeypair.getNode();
			GRpcParaBo rpcPara = getGRpcParas(proposerNode.getConsortiumId(), proposerNode.getId());
			cfx = new GrpcWeb3j(rpcPara.getIp(), rpcPara.getGRpcPort(), rpcPara.getServerCertFile(),
					rpcPara.getRpcCertChainFile(), rpcPara.getRpcKeyFile());
			status = MembershipEnum.Status.SELECT_PROPOSER_SUCCESS;
    	} catch(Exception e) {
    		log.error("[seq={}][membership change]select proposer error:", seq, e);
    		return MembershipEnum.Status.SELECT_PROPOSER_FAILURE;
    	} finally {
    		log.info("[seq={}][membership change]select proposer success!", seq);
    	}
		
		/**
		 * 2. RPC name:cfx_getMembershipChangeRawTransaction 
		 * input : 
		 * ---Vector<public key> new_validators,// all validators in the next consensus round 
		 * ---public_key proposer 
		 * output: 
		 * ---string raw_json
		 */
    	//Request<String, StringResponse> applyRequest = null;
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
			status = MembershipEnum.Status.REQUEST_MEMBERSHIP_CHANGE_SUCCESS;
    	} catch(Exception e) {
    		log.error("[seq={}][membership change]request change error:", seq, e);
    		return MembershipEnum.Status.REQUEST_MEMBERSHIP_CHANGE_FAILURE;
    	} finally {
			log.info("[seq={}][membership change]txResponse--->{}", seq, applyResponse);
    	}
		
		/**
		 * 3. Keccak(raw_json) and ring signature
		 * input : 
		 * ---Vector<(public key,private key)>, secp256k1(Keccak(raw_json)) 
		 * output: 
		 * ---Vector<signature>
		 */
    	Map<KeypairPo, String> ringSignatureMap = Maps.newHashMap();
    	try {
			byte[] rawJsonBytes = applyResponse.getBytes("UTF-8");
			committeeNodeKeypairs.stream().forEach(item -> {
				ECKeyPair ecKeypair = KeyUtil.loadECKeyPairFromBase64(item.getKeypair().getPublicKey(),
						item.getKeypair().getPrivateKey(), item.getKeypair().getAlgorithm());
				String signature = ConfluxUtils.signMessage(rawJsonBytes, ecKeypair);
				
				ringSignatureMap.put(item.getKeypair(), signature);
			});
			status = MembershipEnum.Status.SIGNATURE_MEMBERSHIP_SUCCESS;
    	} catch(Exception e) {
    		log.error("[seq={}][membership change]ring signature error:", seq, e);
    		return MembershipEnum.Status.SIGNATURE_MEMBERSHIP_FAILURE;
    	} finally {
			log.info("[seq={}][membership change]ringSignatureMap--->{}", seq, JSON.toJSONString(ringSignatureMap));
    	}

		/**
		 * 4. RPC name: cfx_constructAndSendNewConsortiumMembershipTrans 
		 * input :
		 * ---string raw_json 
		 * ---Vector<public key> 
		 * ---Vector<signature> 
		 * output:
		 * ---string okay/fail
		 */
    	String confirmResponse = null;
    	try {
			List<String> committeePublicKeys = Lists.newArrayList();
			List<String> ringSignatures = Lists.newArrayList();
			ringSignatureMap.entrySet().forEach(item -> {
				ECKeyPair ecKeypair = KeyUtil.loadECKeyPairFromBase64(item.getKey().getPublicKey(),
						item.getKey().getPrivateKey(), item.getKey().getAlgorithm());
				committeePublicKeys.add("0x"+ StringUtils.leftPad(ecKeypair.getPublicKey().toString(16), 128, '0'));
				ringSignatures.add(item.getValue().substring(2));
			});
			cfx.constructAndSendNewConsortiumMemberTrans(applyResponse, committeePublicKeys, ringSignatures);
			status = MembershipEnum.Status.CONFIRM_MEMBERSHIP_CHANGE_SUCCESS;
    	} catch(Exception e) {
    		log.error("[seq={}][membership change]confirm change error:", seq, e);
    		return MembershipEnum.Status.CONFIRM_MEMBERSHIP_CHANGE_FAILURE;
    	} finally {
			log.info("[seq={}][membership change]txResponse--->{}", seq, confirmResponse);
    	}
    	
    	/**
    	 * 5. delay query BFT epoch number
    	 */
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
					.filter(item -> HeartbeatLinstener.getInstance().getNode(consId, item)
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
			log.info("[update credential]txResponse--->{}");
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
			return txResult;
		} finally {
			log.info("[get sendRawTransaction]txResponse--->{}", txResult);
		}
		return txResult;
	}
	
	private GRpcParaBo getGRpcParas(String consId, String nodeId) throws Exception {
		// get ip and gRpcPort
		Heartbeat heartbeat = HeartbeatLinstener.getInstance().getNode(consId, nodeId);
		String ip = heartbeat.getIp();
		String jsonRpcLocalHttpPortStr = nodeCustomizedConfigDas.findPropertyValueByName(consId, nodeId,
				PROPERTY_JSONRPC_LOC_PORT);
		int jsonRpcLocalHttpPort = Integer.parseInt(jsonRpcLocalHttpPortStr);
		int gRpcPort = TEST_SWITCH ? jsonRpcLocalHttpPort + TEST_RPC_PORT_INTERVAL : DEFAULT_GRPC_PORT;
		
		// server cert 
		NodeKeypairBo nodeKeypair = vaultKeypairBuildService.getNodeKeypair(consId, ALGO_CONS,
				NodePo.builder().id(nodeId).build(), null);
		String serverCertFile = nodeKeypair.getKeypair().getCertStr();
		String serverKeyFile = privateKeyToPem(
				privateKeyFromBase64(ALGO_CONS, nodeKeypair.getKeypair().getPrivateKey()));

		// rpc cert
		KeypairPo consKeyPair = vaultKeypairBuildService.getConsortiumKeyPair(consId, ALGO_CONS, null);
		NodeKeypairBo userKeypair = vaultKeypairBuildService.geneNodeKeypair(consId, ALGO_CONS,
				NodePo.builder().id(nodeId).build(), consKeyPair);
		String rpcCertChainFile = userKeypair.getKeypair().getCertStr();
		String rpcKeyFile = privateKeyToPem(
				privateKeyFromBase64(ALGO_CONS, userKeypair.getKeypair().getPrivateKey()));
		
		// write to host
		String nodeCertPath = MessageFormat.format(NODE_CERT_Path, consId, nodeId);
		Map<String, String> certPathContentMap = Maps.newHashMap();
		certPathContentMap.put(SERVER_CERT_FILE, serverCertFile);
		certPathContentMap.put(SERVER_KEY_FILE, serverKeyFile);
		certPathContentMap.put(RPC_CERT_CHAIN_FILE, rpcCertChainFile);
		certPathContentMap.put(RPC_KEY_FILE, rpcKeyFile);
		Iterator<Entry<String, String>> iter = certPathContentMap.entrySet().iterator();
		while(iter.hasNext()) {
			Entry<String, String> enty = iter.next();
			String file = enty.getKey();
			String content = enty.getValue();
			if(!FileUtil.isExistingFolder(new File(nodeCertPath))) {
				FileUtil.mkdirs(nodeCertPath);
			}
			if(!FileUtil.isExistingFolder(new File(nodeCertPath + file))) {
				FileUtil.writeBytes(nodeCertPath + file, content.getBytes("UTF-8"));
			}
		}
		
		return GRpcParaBo.builder()
				.ip(ip)
				.gRpcPort(gRpcPort)
				.serverCertFile(MessageFormat.format(nodeCertPath + SERVER_CERT_FILE, consId, nodeId))
				.rpcCertChainFile(MessageFormat.format(nodeCertPath + RPC_CERT_CHAIN_FILE, consId, nodeId))
				.rpcKeyFile(MessageFormat.format(nodeCertPath + RPC_KEY_FILE, consId, nodeId)).build();
	}
}
