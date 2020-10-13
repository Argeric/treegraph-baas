package org.treegraph.baas.core.service.impl;

import static org.treegraph.baas.core.constant.ConfluxConstant.RPC_ADD_DELETE_NODE;
import static org.treegraph.baas.core.constant.ConfluxConstant.RPC_ADD_DELETE_NODE_CONFIRM;
import static org.treegraph.baas.core.constant.ConfluxConstant.RPC_GET_BFT_EPOCH_NUMBER;
import static org.treegraph.baas.core.constant.ConfluxConstant.RPC_UPDATE_CERT;
import static org.treegraph.baas.core.constant.X509CertConstant.ALGO_NODE;
import static org.treegraph.baas.core.util.crypto.x509.KeyUtil.privateKeyFromBase64;
import static org.treegraph.baas.core.util.crypto.x509.KeyUtil.privateKeyToPem;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.treegraph.baas.core.component.BizTimer;
import org.treegraph.baas.core.das.KeypairDas;
import org.treegraph.baas.core.das.bo.NodeKeypairBo;
import org.treegraph.baas.core.das.bo.NodeKeypairInfoBo;
import org.treegraph.baas.core.domain.KeypairPo;
import org.treegraph.baas.core.domain.NodePo;
import org.treegraph.baas.core.enums.CryptoEnum.KeyType;
import org.treegraph.baas.core.enums.BlockchainEnum;
import org.treegraph.baas.core.enums.HeartbeatEnum;
import org.treegraph.baas.core.enums.MembershipEnum;
import org.treegraph.baas.core.exception.AlertException;
import org.treegraph.baas.core.heartbeat.bo.Heartbeat;
import org.treegraph.baas.core.heartbeat.impl.HeartbeatLinstener;
import org.treegraph.baas.core.service.BlockchainRpcService;
import org.treegraph.baas.core.task.CheckMembershipChangeTimerTask;
import org.treegraph.baas.core.util.ConfluxUtils;
import org.treegraph.baas.core.util.IdWorker;
import org.treegraph.baas.core.util.crypto.x509.KeyUtil;
import org.web3j.crypto.ECKeyPair;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import conflux.web3j.jsonrpc.Cfx;
import conflux.web3j.jsonrpc.Request;
import conflux.web3j.response.BigIntResponse;
import conflux.web3j.response.StringResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Validated
@Service
@AllArgsConstructor
@Slf4j
public class JsonRpcServiceImpl implements BlockchainRpcService{
	
    private KeypairDas keypairDas;
	
	public MembershipEnum.Status sendMembershipChangeTransactionPlus(long membershipChangeId,
			List<NodeKeypairBo> committeeNodeKeypairs, List<NodeKeypairBo> validatorNodeKeypairs) throws IOException {
    	MembershipEnum.Status status = null;
    	Long seq = IdWorker.generateId();
    	/**
    	 * 1. select a proposer in committee
    	 */
    	String uri = null;
    	Cfx cfx = null;
    	NodeKeypairBo proposerNodeKeypair = null;
    	try {
			proposerNodeKeypair = committeeNodeKeypairs.stream()
					.filter(item -> HeartbeatEnum.Status.GREEN == HeartbeatLinstener.getInstance()
							.getNode(item.getNode().getConsortiumId(), item.getNode().getId()).getStatus())
					.findFirst().orElseThrow(() -> new AlertException("alive.proposer.node.not.found"));
			NodePo proposerNode = proposerNodeKeypair.getNode();
			Heartbeat heartbeat = HeartbeatLinstener.getInstance().getNode(proposerNode.getConsortiumId(),
					proposerNode.getId());
			uri = ConfluxUtils.toUri(heartbeat.getIp(), heartbeat.getPort());
			cfx = Cfx.create(uri, 1, 1, TimeUnit.SECONDS);
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
    	Request<String, StringResponse> applyRequest = null;
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
			applyRequest = cfx.getCustomizedRequest(StringResponse.class,
					RPC_ADD_DELETE_NODE, validatorPublicKeys, proposerPublicKey);
			applyResponse = applyRequest.sendAndGet();
			Assert.isTrue(applyResponse != null, RPC_ADD_DELETE_NODE + " error");
			status = MembershipEnum.Status.REQUEST_MEMBERSHIP_CHANGE_SUCCESS;
    	} catch(Exception e) {
    		log.error("[seq={}][membership change]request change error:", seq, e);
    		return MembershipEnum.Status.REQUEST_MEMBERSHIP_CHANGE_FAILURE;
    	} finally {
			log.info("[seq={}][membership change]uri--->{}, txRequest--->{}, txResponse--->{}", seq, uri,
					JSON.toJSONString(applyRequest), applyResponse);
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
    	Request<String, StringResponse> confirmRequest = null;
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
			confirmRequest = cfx.getCustomizedRequest(StringResponse.class,
					RPC_ADD_DELETE_NODE_CONFIRM, applyResponse, committeePublicKeys, ringSignatures);
			confirmResponse = confirmRequest.sendAndGet();
			status = MembershipEnum.Status.CONFIRM_MEMBERSHIP_CHANGE_SUCCESS;
    	} catch(Exception e) {
    		log.error("[seq={}][membership change]confirm change error:", seq, e);
    		return MembershipEnum.Status.CONFIRM_MEMBERSHIP_CHANGE_FAILURE;
    	} finally {
			log.info("[seq={}][membership change]uri--->{}, txRequest--->{}, txResponse--->{}", seq, uri,
					JSON.toJSONString(confirmRequest), confirmResponse);
    	}
    	
    	/**
    	 * 5. delay query BFT epoch number
    	 */
    	try {
			BizTimer.DELAY_TIMER.newTimeout(
					new CheckMembershipChangeTimerTask(membershipChangeId, BlockchainEnum.RpcType.JSONRPC), 
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
		String uri = null;
		Request<BigInteger, BigIntResponse> getBftEpochNumberRequest = null;
		BigInteger getBftEpochNumberResponse = null;
		try {
			Heartbeat heartbeat = HeartbeatLinstener.getInstance().getNode(consId, nodeId);
			uri = ConfluxUtils.toUri(heartbeat.getIp(), heartbeat.getPort());
			Cfx cfx = Cfx.create(uri, 1, 1, TimeUnit.SECONDS);
			getBftEpochNumberRequest = cfx.getCustomizedRequest(BigIntResponse.class, RPC_GET_BFT_EPOCH_NUMBER);
			getBftEpochNumberResponse = getBftEpochNumberRequest.sendAndGet();
		} catch(Exception e) {
			log.error("[get bftEpochNumber]error:", e);
			return BigInteger.valueOf(-1L);
		} finally {
			log.info("[get bftEpochNumber]uri--->{}, txRequest--->{}, txResponse--->{}", uri,
					JSON.toJSONString(getBftEpochNumberRequest), getBftEpochNumberResponse);
		}
		return getBftEpochNumberResponse;
	}
	
	public boolean updateCert(String consId, String consCertPem, List<String> membershipNodeIds) {
		String uri = null;
		Request<String, StringResponse> req = null;
		String resp = null;
		try {
			String proposerNodeId = membershipNodeIds.stream()
					.filter(item -> HeartbeatLinstener.getInstance().getNode(consId, item)
							.getStatus() == HeartbeatEnum.Status.GREEN)
					.findFirst().orElseThrow(() -> new AlertException("alive.proposer.node.not.found"));
			NodeKeypairInfoBo proposerKeypair = keypairDas.findAll(consId, KeyType.NODE.getCode(), ALGO_NODE,
					Lists.newArrayList(proposerNodeId)).get(0);
			String grpcKeyPem = privateKeyToPem(privateKeyFromBase64(ALGO_NODE, proposerKeypair.getPrivateKey()));
			String grpcCertPem = proposerKeypair.getCertStr();
			
			Heartbeat heartbeat = HeartbeatLinstener.getInstance().getNode(consId, proposerNodeId);
			uri = ConfluxUtils.toUri(heartbeat.getIp(), heartbeat.getPort());
			Cfx cfx = Cfx.create(uri, 1, 1, TimeUnit.SECONDS);
			req = cfx.getCustomizedRequest(StringResponse.class, RPC_UPDATE_CERT, consCertPem, grpcKeyPem, grpcCertPem);
			resp = req.sendAndGet();
		} catch(Exception e) {
			log.error("[update credential]error:", e);
			return Boolean.FALSE;
		} finally {
			log.info("[update credential]uri--->{}, txRequest--->{}, txResponse--->{}", uri,
					JSON.toJSONString(req), resp);
		}
		return Boolean.TRUE;
	}
}
