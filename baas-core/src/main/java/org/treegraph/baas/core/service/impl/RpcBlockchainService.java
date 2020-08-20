package org.treegraph.baas.core.service.impl;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.treegraph.baas.core.constant.ConfluxConstant;
import org.treegraph.baas.core.das.bo.NodeKeypairBo;
import org.treegraph.baas.core.domain.KeypairPo;
import org.treegraph.baas.core.domain.NodePo;
import org.treegraph.baas.core.service.NodeCustomizedConfigService;
import org.treegraph.baas.core.util.ConfluxUtils;
import org.treegraph.baas.core.util.HexUtils;
import org.treegraph.baas.core.util.crypto.x509.KeyUtil;
import org.web3j.crypto.ECKeyPair;
import org.web3j.utils.Numeric;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import conflux.web3j.AccountManager;
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
public class RpcBlockchainService {
	
	private NodeCustomizedConfigService nodeCustomizedConfigService;
	
    private HeartbeatService heartbeatService;
    
    public void sendMembershipChangeTransaction(String consortiumId, List<NodeKeypairBo> existNodeKeypairs, List<NodeKeypairBo> addNodeKeypairs,
			List<NodeKeypairBo> removeNodeKeypairs) throws IOException {
		// build rpc client
    	List<NodePo> existNodes = existNodeKeypairs.stream().map(item -> item.getNode()).collect(Collectors.toList());
		Map<String, Integer> nodeIdRpcHttpPortMap = nodeCustomizedConfigService.getJsonRpcHttpPortMap(existNodes);
		//Map<String, Boolean> heartbeatMap = getHeartbeatMap(existNodes, nodeIdRpcHttpPortMap);	
		Map<String, Boolean> heartbeatMap = Maps.newHashMap();//mock
		heartbeatMap.put("101.200.183.108@12537", true);//mock
		NodeKeypairBo proposerNodeKeypair = existNodeKeypairs.stream()
				.filter(item -> {
					NodePo node = item.getNode();
					String key = ConfluxUtils.toIpAtPort(node.getNodeIp(), nodeIdRpcHttpPortMap.get(node.getId()));
					log.info("key--->{}, ", key);
					Boolean value = heartbeatMap.get(key);
					log.info("key--->{},value--->{} ", key, value);
					return value != null && value;
				})
				.findFirst().get();
		NodePo proposerNode = proposerNodeKeypair.getNode();
		String uri = ConfluxUtils.toJsonRpcUri(proposerNode.getNodeIp(), nodeIdRpcHttpPortMap.get(proposerNode.getId()));
		Cfx cfx = Cfx.create(uri);
		
		/*
		 * 1. RPC name:cfx_getMembershipChangeRawTransaction 
		 * input : 
		 * ---Vector<public key> new_validators,// all validators in the next consensus round 
		 * ---public_key proposer 
		 * output: 
		 * ---string raw_json
		 */
		List<String> existPublicKeys = existNodeKeypairs.stream().map(item -> {
			ECKeyPair ecKeypair = KeyUtil.loadECKeyPairFromBase64(item.getKeypair().getPublicKey(),
					item.getKeypair().getPrivateKey(), item.getKeypair().getAlgorithm());
			return "0x"+ecKeypair.getPublicKey().toString(16);
		}).collect(Collectors.toList());
		ECKeyPair proposerEcKeypair = KeyUtil.loadECKeyPairFromBase64(proposerNodeKeypair.getKeypair().getPublicKey(),
				proposerNodeKeypair.getKeypair().getPrivateKey(), proposerNodeKeypair.getKeypair().getAlgorithm());
		String proposerPublicKey = "0x"+proposerEcKeypair.getPublicKey().toString(16);
		Request<String, StringResponse> applyRequest = cfx.getCustomizedRequest(StringResponse.class,
				ConfluxConstant.RPC_ADD_DELETE_NODE, existPublicKeys, proposerPublicKey);
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
		String hexEncoded = "0x" + HexUtils.byteArrToHex(applyResponse.getBytes("UTF-8"));
		List<KeypairPo> leftNodeKeypairs = getLeftKeypairs(existNodeKeypairs, addNodeKeypairs, removeNodeKeypairs);
		Map<KeypairPo, String> ringSignatureMap = Maps.newHashMap();
		leftNodeKeypairs.stream().forEach(item -> {
			ECKeyPair ecKeypair = KeyUtil.loadECKeyPairFromBase64(item.getPublicKey(), item.getPrivateKey(),
					item.getAlgorithm());
			String signature = AccountManager.signMessage(Numeric.hexStringToByteArray(hexEncoded), true, ecKeypair);
			ringSignatureMap.put(item, signature);
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
		List<String> leftPublicKeys = Lists.newArrayList();
		List<String> ringSignatures = Lists.newArrayList();
		ringSignatureMap.entrySet().forEach(item -> {
			ECKeyPair ecKeypair = KeyUtil.loadECKeyPairFromBase64(item.getKey().getPublicKey(),
					item.getKey().getPrivateKey(), item.getKey().getAlgorithm());
			String publicKey = ecKeypair.getPublicKey().toString(16);
			publicKey = publicKey.length() == 127 ? "0" + publicKey : publicKey;
			leftPublicKeys.add("0x"+ publicKey);
			ringSignatures.add(item.getValue().substring(2));
		});
		Request<String, StringResponse> confirmRequest = cfx.getCustomizedRequest(StringResponse.class,
				ConfluxConstant.RPC_ADD_DELETE_NODE_CONFIRM, applyResponse, leftPublicKeys, ringSignatures);
		log.info("RpcBlockchainService uri--->{}, txRequest--->{}", uri, JSON.toJSONString(confirmRequest));
		String confirmResponse = confirmRequest.sendAndGet(3);
		log.info("RpcBlockchainService uri--->{}, txRequest--->{}, txResponse--->{}", uri, JSON.toJSONString(confirmRequest), confirmResponse);
		
		/*
		 * 4. RPC name: cfx_getBFTEpochNumber
		 * input :
		 * output:
		 * ---???
		 */
		checkNodeClient(existNodeKeypairs, addNodeKeypairs, removeNodeKeypairs, nodeIdRpcHttpPortMap);
	}
    
    public boolean checkNodeClient(List<NodeKeypairBo> existNodeKeypairs, List<NodeKeypairBo> addNodeKeypairs,
			List<NodeKeypairBo> removeNodeKeypairs, Map<String, Integer> nodeIdRpcHttpPortMap) {
    	int expectValue = existNodeKeypairs.size();
    	if(CollectionUtils.isNotEmpty(addNodeKeypairs)) {
    		expectValue += addNodeKeypairs.size();
    	}
    	Set<String> removedNodeIds = Sets.newHashSet();
    	if(CollectionUtils.isNotEmpty(removeNodeKeypairs)) {
    		expectValue -= removeNodeKeypairs.size();
    		removeNodeKeypairs.stream().forEach(item -> removedNodeIds.add(item.getNode().getId()));
    	}
    	
    	List<Integer> bftEpochNumbers = Lists.newArrayList();
    	existNodeKeypairs.stream().filter(item -> !removedNodeIds.contains(item.getNode().getId())).forEach(item -> {
	    	String uri = ConfluxUtils.toJsonRpcUri(item.getNode().getNodeIp(), nodeIdRpcHttpPortMap.get(item.getNode().getId()));
	    	Cfx cfx = Cfx.create(uri);
	    	log.info("checkNodeClient uri--->{}", uri);
	    	Request<BigInteger, BigIntResponse> getBftEpochNumberRequest = cfx.getCustomizedRequest(BigIntResponse.class, ConfluxConstant.RPC_GET_BFT_EPOCH_NUMBER);
	    	log.info("checkNodeClient uri--->{}, txRequest--->{}", uri, JSON.toJSONString(getBftEpochNumberRequest));
	    	BigInteger getBftEpochNumberResponse = getBftEpochNumberRequest.sendAndGet();
	    	bftEpochNumbers.add(getBftEpochNumberResponse.intValue());
	    	log.info("checkNodeClient uri--->{}, txRequest--->{}, txResponse--->{}", uri, JSON.toJSONString(getBftEpochNumberRequest), getBftEpochNumberResponse);
    	});
    	
    	boolean checkResult = Boolean.TRUE;
    	for(int bftEpochNumber : bftEpochNumbers){
    		checkResult = checkResult && (bftEpochNumber == expectValue);
    	}
    	
    	return checkResult;
    }

	private Map<String, Boolean> getHeartbeatMap(List<NodePo> existNodes, Map<String, Integer> nodeIdRpcPortMap) {
		Map<String, Boolean> heartbeatMap = heartbeatService.batchNodeHeartbeat(existNodes, nodeIdRpcPortMap);
		Set<Boolean> heartbeatSet = Sets.newHashSet(heartbeatMap.values());
		Assert.isTrue(heartbeatSet.contains(Boolean.TRUE), "blockchain not lunched!");
		return heartbeatMap;
	}
	
	private List<KeypairPo> getLeftKeypairs(List<NodeKeypairBo> existNodeKeypairs, List<NodeKeypairBo> addNodeKeypairs,
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
		List<KeypairPo> leftKeypairs = leftNodeKeypairs.stream().map(NodeKeypairBo::getKeypair).collect(Collectors.toList());
		return leftKeypairs;
	}
}
