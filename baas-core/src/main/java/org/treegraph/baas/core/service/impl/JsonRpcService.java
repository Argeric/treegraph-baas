package org.treegraph.baas.core.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.treegraph.baas.core.constant.ConfluxConstant;
import org.treegraph.baas.core.das.bo.NodeKeypairBo;
import org.treegraph.baas.core.domain.KeypairPo;
import org.treegraph.baas.core.domain.NodePo;
import org.treegraph.baas.core.enums.NodeEnum;
import org.treegraph.baas.core.heartbeat.bo.Heartbeat;
import org.treegraph.baas.core.heartbeat.impl.HeartbeatLinstener;
import org.treegraph.baas.core.util.ConfluxUtils;
import org.treegraph.baas.core.util.crypto.x509.KeyUtil;
import org.web3j.crypto.ECKeyPair;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import conflux.web3j.Cfx;
import conflux.web3j.Request;
import conflux.web3j.response.StringResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Validated
@Service
@AllArgsConstructor
@Slf4j
public class JsonRpcService {
	
    private JsonRpcAsyncService jsonRpcAsyncService;
    
    public void sendMembershipChangeTransaction(String consortiumId, List<NodeKeypairBo> existNodeKeypairs, List<NodeKeypairBo> addNodeKeypairs,
			List<NodeKeypairBo> removeNodeKeypairs) throws IOException {
    	// get alive nodes
		List<NodeKeypairBo> aliveNodeKeypairs = existNodeKeypairs.stream()
				.filter(item -> HeartbeatLinstener.getInstance().getNodeStatus(item.getNode().getConsortiumId(),
						item.getNode().getId()) == NodeEnum.Status.RUNING)
				.collect(Collectors.toList());
		Assert.isTrue(CollectionUtils.isNotEmpty(aliveNodeKeypairs), "blockchain not lunched!");
    	
		// select a proposer in committee
    	List<NodeKeypairBo> committeeNodeKeypairs = getNodeKeypairForCommittee(aliveNodeKeypairs, removeNodeKeypairs);
		NodeKeypairBo proposerNodeKeypair = committeeNodeKeypairs.get(0);
		NodePo proposerNode = proposerNodeKeypair.getNode();
		Heartbeat heartbeat = HeartbeatLinstener.getInstance().getNode(proposerNode.getConsortiumId(), proposerNode.getId());
		String uri = ConfluxUtils.toJsonRpcUri(proposerNode.getNodeIp(), heartbeat.getPort());
		Cfx cfx = Cfx.create(uri, 1, 1, TimeUnit.SECONDS);
		
		/*
		 * 1. RPC name:cfx_getMembershipChangeRawTransaction 
		 * input : 
		 * ---Vector<public key> new_validators,// all validators in the next consensus round 
		 * ---public_key proposer 
		 * output: 
		 * ---string raw_json
		 */
		List<NodeKeypairBo> validatorNodeKeypairs = getNodeKeypairForValidator(aliveNodeKeypairs, addNodeKeypairs, removeNodeKeypairs);
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
		String applyResponse = applyRequest.sendAndGet();
		log.info("RpcBlockchainService uri--->{}, txRequest--->{}, txResponse--->{}", uri, JSON.toJSONString(applyRequest), applyResponse);
		Assert.isTrue(applyResponse != null, ConfluxConstant.RPC_ADD_DELETE_NODE + " error");
		
		/*
		 * 2. Keccak(raw_json) and ring signature
		 * input : 
		 * ---Vector<(public key,private key)>, secp256k1(Keccak(raw_json)) 
		 * output: 
		 * ---Vector<signature>
		 */
		byte[] rawJsonBytes = applyResponse.getBytes("UTF-8");
		Map<KeypairPo, String> ringSignatureMap = Maps.newHashMap();
		committeeNodeKeypairs.stream().forEach(item -> {
			ECKeyPair ecKeypair = KeyUtil.loadECKeyPairFromBase64(item.getKeypair().getPublicKey(), item.getKeypair().getPrivateKey(),
					item.getKeypair().getAlgorithm());
			String signature = ConfluxUtils.signMessage(rawJsonBytes, ecKeypair);
			
			ringSignatureMap.put(item.getKeypair(), signature);
		});
		log.info("RpcBlockchainService ringSignatureMap--->{}", JSON.toJSONString(ringSignatureMap));

		/*
		 * 3. RPC name: cfx_constructAndSendNewConsortiumMembershipTrans 
		 * input :
		 * ---string raw_json 
		 * ---Vector<public key> 
		 * ---Vector<signature> 
		 * output:
		 * ---string okay/fail
		 */
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
		log.info("RpcBlockchainService uri--->{}, txRequest--->{}, txResponse--->{}", uri, JSON.toJSONString(confirmRequest), confirmResponse);
		
		/*
		 * 4. RPC name: cfx_getBFTEpochNumber
		 * input :
		 * output:
		 * ---boolean true/false
		 */
		jsonRpcAsyncService.checkNodeClient(aliveNodeKeypairs, addNodeKeypairs, removeNodeKeypairs);
	}
    
	private List<NodeKeypairBo> getNodeKeypairForCommittee(List<NodeKeypairBo> existNodeKeypairs,
			List<NodeKeypairBo> removeNodeKeypairs) {
    	List<NodeKeypairBo> leftNodeKeypairs = Lists.newArrayList(existNodeKeypairs);
    	if(CollectionUtils.isNotEmpty(removeNodeKeypairs)) {
    		Set<String> toRemoveNodeIdSet = removeNodeKeypairs.stream().map(item -> item.getNode().getId()).collect(Collectors.toSet());
    		leftNodeKeypairs = leftNodeKeypairs.stream()
    				.filter(item -> !toRemoveNodeIdSet.contains(item.getNode().getId())).collect(Collectors.toList());
    	}
    	
    	return leftNodeKeypairs;
    }
    
    private List<NodeKeypairBo> getNodeKeypairForValidator(List<NodeKeypairBo> existNodeKeypairs, List<NodeKeypairBo> addNodeKeypairs,
    		List<NodeKeypairBo> removeNodeKeypairs) {
    	List<NodeKeypairBo> leftNodeKeypairs = Lists.newArrayList(existNodeKeypairs);
    	if(CollectionUtils.isNotEmpty(removeNodeKeypairs)) {
    		Set<String> toRemoveNodeIdSet = removeNodeKeypairs.stream().map(item -> item.getNode().getId()).collect(Collectors.toSet());
    		leftNodeKeypairs = leftNodeKeypairs.stream()
    				.filter(item -> !toRemoveNodeIdSet.contains(item.getNode().getId())).collect(Collectors.toList());
    	}
    	if(CollectionUtils.isNotEmpty(addNodeKeypairs)) {
    		leftNodeKeypairs.addAll(addNodeKeypairs);
    	}
    	
    	return leftNodeKeypairs;
    }
}
