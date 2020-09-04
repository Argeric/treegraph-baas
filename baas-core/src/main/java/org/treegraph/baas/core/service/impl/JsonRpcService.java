package org.treegraph.baas.core.service.impl;

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
import org.treegraph.baas.core.constant.ConfluxConstant;
import org.treegraph.baas.core.das.bo.NodeKeypairBo;
import org.treegraph.baas.core.domain.KeypairPo;
import org.treegraph.baas.core.domain.NodePo;
import org.treegraph.baas.core.enums.MembershipEnum;
import org.treegraph.baas.core.heartbeat.bo.Heartbeat;
import org.treegraph.baas.core.heartbeat.impl.HeartbeatLinstener;
import org.treegraph.baas.core.task.CheckMembershipChangeTimerTask;
import org.treegraph.baas.core.util.ConfluxUtils;
import org.treegraph.baas.core.util.crypto.x509.KeyUtil;
import org.web3j.crypto.ECKeyPair;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import conflux.web3j.Cfx;
import conflux.web3j.Request;
import conflux.web3j.response.BigIntResponse;
import conflux.web3j.response.StringResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Validated
@Service
@AllArgsConstructor
@Slf4j
public class JsonRpcService {
	
	public MembershipEnum.Status sendMembershipChangeTransactionPlus(long membershipChangeId,
			List<NodeKeypairBo> aliveNodeKeypairs, List<NodeKeypairBo> committeeNodeKeypairs,
			List<NodeKeypairBo> validatorNodeKeypairs) throws IOException {
    	MembershipEnum.Status status = null;
    	/**
    	 * 1. select a proposer in committee
    	 */
    	String uri = null;
    	Cfx cfx = null;
    	NodeKeypairBo proposerNodeKeypair = null;
    	try {
			proposerNodeKeypair = committeeNodeKeypairs.get(0);
			NodePo proposerNode = proposerNodeKeypair.getNode();
			Heartbeat heartbeat = HeartbeatLinstener.getInstance().getNode(proposerNode.getConsortiumId(), proposerNode.getId());
			uri = ConfluxUtils.toJsonRpcUri(heartbeat.getIp(), heartbeat.getPort());
			cfx = Cfx.create(uri, 1, 1, TimeUnit.SECONDS);
			status = MembershipEnum.Status.SELECT_PROPOSER_SUCCESS;
    	} catch(Exception e) {
    		return MembershipEnum.Status.SELECT_PROPOSER_FAILURE;
    	}
		
		/**
		 * 2. RPC name:cfx_getMembershipChangeRawTransaction 
		 * input : 
		 * ---Vector<public key> new_validators,// all validators in the next consensus round 
		 * ---public_key proposer 
		 * output: 
		 * ---string raw_json
		 */
    	String applyResponse = null;
    	try {
			List<String> validatorPublicKeys = validatorNodeKeypairs.stream().map(item -> {
				ECKeyPair ecKeypair = KeyUtil.loadECKeyPairFromBase64(item.getKeypair().getPublicKey(),
						item.getKeypair().getPrivateKey(), item.getKeypair().getAlgorithm());
				return "0x"+StringUtils.leftPad(ecKeypair.getPublicKey().toString(16), 128, '0');
			}).collect(Collectors.toList());
			ECKeyPair proposerEcKeypair = KeyUtil.loadECKeyPairFromBase64(proposerNodeKeypair.getKeypair().getPublicKey(),
					proposerNodeKeypair.getKeypair().getPrivateKey(), proposerNodeKeypair.getKeypair().getAlgorithm());
			String proposerPublicKey = "0x"+StringUtils.leftPad(proposerEcKeypair.getPublicKey().toString(16), 128, '0');
			Request<String, StringResponse> applyRequest = cfx.getCustomizedRequest(StringResponse.class,
					ConfluxConstant.RPC_ADD_DELETE_NODE, validatorPublicKeys, proposerPublicKey);
			log.info("RpcBlockchainService uri--->{}, txRequest--->{}", uri, JSON.toJSONString(applyRequest));
			applyResponse = applyRequest.sendAndGet();
			log.info("RpcBlockchainService uri--->{}, txRequest--->{}, txResponse--->{}", uri, JSON.toJSONString(applyRequest), applyResponse);
			Assert.isTrue(applyResponse != null, ConfluxConstant.RPC_ADD_DELETE_NODE + " error");
			status = MembershipEnum.Status.REQUEST_MEMBERSHIP_CHANGE_SUCCESS;
    	} catch(Exception e) {
    		return MembershipEnum.Status.REQUEST_MEMBERSHIP_CHANGE_FAILURE;
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
				ECKeyPair ecKeypair = KeyUtil.loadECKeyPairFromBase64(item.getKeypair().getPublicKey(), item.getKeypair().getPrivateKey(),
						item.getKeypair().getAlgorithm());
				String signature = ConfluxUtils.signMessage(rawJsonBytes, ecKeypair);
				
				ringSignatureMap.put(item.getKeypair(), signature);
			});
			status = MembershipEnum.Status.SIGNATURE_MEMBERSHIP_SUCCESS;
			log.info("RpcBlockchainService ringSignatureMap--->{}", JSON.toJSONString(ringSignatureMap));
    	} catch(Exception e) {
    		return MembershipEnum.Status.SIGNATURE_MEMBERSHIP_FAILURE;
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
    	try {
			List<String> committeePublicKeys = Lists.newArrayList();
			List<String> ringSignatures = Lists.newArrayList();
			ringSignatureMap.entrySet().forEach(item -> {
				ECKeyPair ecKeypair = KeyUtil.loadECKeyPairFromBase64(item.getKey().getPublicKey(),
						item.getKey().getPrivateKey(), item.getKey().getAlgorithm());
				committeePublicKeys.add("0x"+ StringUtils.leftPad(ecKeypair.getPublicKey().toString(16), 128, '0'));
				ringSignatures.add(item.getValue().substring(2));
			});
			Request<String, StringResponse> confirmRequest = cfx.getCustomizedRequest(StringResponse.class,
					ConfluxConstant.RPC_ADD_DELETE_NODE_CONFIRM, applyResponse, committeePublicKeys, ringSignatures);
			log.info("RpcBlockchainService uri--->{}, txRequest--->{}", uri, JSON.toJSONString(confirmRequest));
			String confirmResponse = confirmRequest.sendAndGet();
			status = MembershipEnum.Status.CONFIRM_MEMBERSHIP_CHANGE_SUCCESS;
			log.info("RpcBlockchainService uri--->{}, txRequest--->{}, txResponse--->{}", uri, JSON.toJSONString(confirmRequest), confirmResponse);
    	} catch(Exception e) {
    		return MembershipEnum.Status.CONFIRM_MEMBERSHIP_CHANGE_FAILURE;
    	}
    	
    	/**
    	 * 5. delay query BFT epoch number
    	 */
    	try {
    		BizTimer.DELAY_TIMER.newTimeout(new CheckMembershipChangeTimerTask(membershipChangeId), 60, TimeUnit.SECONDS);
    		status = MembershipEnum.Status.LAUNCH_CHECK_THREAD_SUCCESS;
    	} catch(Exception e) {
    		return MembershipEnum.Status.LAUNCH_CHECK_THREAD_FAILURE;
    	}

    	return status;
	}
    
	public BigInteger getBftEpochNumber(String consId, String nodeId) {
		Heartbeat heartbeat = HeartbeatLinstener.getInstance().getNode(consId, nodeId);
		String uri = ConfluxUtils.toJsonRpcUri(heartbeat.getIp(), heartbeat.getPort());
		Cfx cfx = Cfx.create(uri, 1, 1, TimeUnit.SECONDS);
		Request<BigInteger, BigIntResponse> getBftEpochNumberRequest = cfx.getCustomizedRequest(BigIntResponse.class, ConfluxConstant.RPC_GET_BFT_EPOCH_NUMBER);
		log.info("checkNodeClient uri--->{}, txRequest--->{}", uri, JSON.toJSONString(getBftEpochNumberRequest));
		BigInteger getBftEpochNumberResponse = getBftEpochNumberRequest.sendAndGet();
		log.info("checkNodeClient uri--->{}, txRequest--->{}, txResponse--->{}", uri, JSON.toJSONString(getBftEpochNumberRequest), getBftEpochNumberResponse);
		return getBftEpochNumberResponse;
	}
}
