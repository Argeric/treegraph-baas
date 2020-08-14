package org.treegraph.baas.core.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.treegraph.baas.core.constant.ConfluxConstant;
import org.treegraph.baas.core.constant.X509CertConstant;
import org.treegraph.baas.core.das.KeypairDas;
import org.treegraph.baas.core.das.bo.NodeKeypairBo;
import org.treegraph.baas.core.domain.KeypairPo;
import org.treegraph.baas.core.domain.NodePo;
import org.treegraph.baas.core.service.NodeCustomizedConfigService;
import org.treegraph.baas.core.util.ConfluxUtils;
import org.treegraph.baas.core.util.crypto.x509.KeyUtil;
import org.web3j.crypto.ECKeyPair;
import org.web3j.utils.Numeric;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import conflux.web3j.AccountManager;
import conflux.web3j.Cfx;
import conflux.web3j.Request;
import conflux.web3j.response.StringResponse;
import lombok.AllArgsConstructor;

@Validated
@Service
@AllArgsConstructor
public class RpcBlockchainService {
	
	private KeypairDas keypairDas;
	
	private NodeCustomizedConfigService nodeCustomizedConfigService;
	
    private HeartbeatService heartbeatService;
	
    private VaultKeypairBuildService vaultKeypairBuildService;

	public void sendMembershipChangeTransaction(String consortiumId, List<NodePo> existNodes, List<NodePo> addNodes,
			List<NodePo> removeNodes) throws IOException {
		// build rpc client
		List<NodeKeypairBo> existNodeKeypairs = existNodes.stream().map(
				item -> vaultKeypairBuildService.getNodeKeypair(consortiumId, X509CertConstant.DEFAULT_ALGO_NODE, item, null))
				.collect(Collectors.toList());
		Map<String, Integer> nodeIdRpcHttpPortMap = nodeCustomizedConfigService.getJsonRpcHttpPortMap(existNodes);
		Map<String, Boolean> heartbeatMap = getHeartbeatMap(existNodes, nodeIdRpcHttpPortMap);		
		NodeKeypairBo proposerNodeKeypair = existNodeKeypairs.stream()
				.filter(item -> {
					NodePo node = item.getNode();
					String key = ConfluxUtils.toIpAtPort(node.getNodeIp(), nodeIdRpcHttpPortMap.get(node.getId()));
					return heartbeatMap.get(key);
				})
				.findFirst().get();
		NodePo proposerNode = proposerNodeKeypair.getNode();
		Cfx cfx = Cfx.create(ConfluxUtils.toJsonRpcUri(proposerNode.getNodeIp(), nodeIdRpcHttpPortMap.get(proposerNode.getId())));
		
		/*
		 * 1. RPC name:cfx_getMembershipChangeRawTransaction 
		 * input : 
		 * ---Vector<public key> new_validators,// all validators in the next consensus round 
		 * ---public_key proposer 
		 * output: 
		 * ---string raw_json
		 */
		List<String> existPublicKeys = existNodeKeypairs.stream().map(item -> item.getKeypair().getPublicKey())
				.collect(Collectors.toList());
		String proposerPublicKey = proposerNodeKeypair.getKeypair().getPublicKey();
		Request<String, StringResponse> request = cfx.getCustomizedRequest(StringResponse.class,
				ConfluxConstant.RPC_ADD_DELETE_NODE, existPublicKeys, proposerPublicKey);
		StringResponse response = request.send();
		String rawJson = response.getResult();
		Assert.isTrue(rawJson != null, ConfluxConstant.RPC_ADD_DELETE_NODE + " error");
		
		/*
		 * 2. Keccak(raw_json) and ring signature
		 * input : 
		 * ---Vector<(public key,private key)>, secp256k1(Keccak(raw_json)) 
		 * output: 
		 * ---Vector<signature>
		 */
		List<KeypairPo> leftKeypairs = getLeftKeypairs(consortiumId, addNodes, removeNodes, existNodeKeypairs);
		Map<KeypairPo, String> ringSignatureMap = Maps.newHashMap();
		leftKeypairs.stream().forEach(item -> {
			ECKeyPair ecKeypair = KeyUtil.loadECKeyPairFromBase64(item.getPublicKey(), item.getPrivateKey(),
					item.getAlgorithm());
			String signature = AccountManager.signMessage(Numeric.hexStringToByteArray(rawJson), true, ecKeypair);
			ringSignatureMap.put(item, signature);
		});

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
			leftPublicKeys.add(item.getKey().getPublicKey());
			ringSignatures.add(item.getValue());
		});
		Request<String, StringResponse> requestConfirm = cfx.getCustomizedRequest(StringResponse.class,
				ConfluxConstant.RPC_ADD_DELETE_NODE_CONFIRM, rawJson, leftPublicKeys, ringSignatures);
		StringResponse responseConfirm = requestConfirm.send();
		Assert.isTrue(responseConfirm != null 
				&& responseConfirm.getResult() != null
				&& responseConfirm.getResult().equalsIgnoreCase("ok"),
				ConfluxConstant.RPC_ADD_DELETE_NODE_CONFIRM + "error");
	}

	private Map<String, Boolean> getHeartbeatMap(List<NodePo> existNodes, Map<String, Integer> nodeIdRpcPortMap) {
		Map<String, Boolean> heartbeatMap = heartbeatService.batchNodeHeartbeat(existNodes, nodeIdRpcPortMap);
		Set<Boolean> heartbeatSet = Sets.newHashSet(heartbeatMap.values());
		Assert.isTrue(heartbeatSet.contains(Boolean.TRUE), "blockchain not lunched!");
		return heartbeatMap;
	}

	private List<KeypairPo> getLeftKeypairs(String consortiumId, List<NodePo> addNodes, List<NodePo> removeNodes,
			List<NodeKeypairBo> existNodeKeypairs) {
		List<NodeKeypairBo> leftNodeKeypairs = Lists.newArrayList();
		if(CollectionUtils.isNotEmpty(removeNodes)) {
			Set<String> removeNodeIdSet = removeNodes.stream().map(NodePo::getId).collect(Collectors.toSet());
			List<NodeKeypairBo> afterRemoveNodeKeypairs = existNodeKeypairs.stream()
					.filter(item -> !removeNodeIdSet.contains(item.getNode().getId())).collect(Collectors.toList());
			leftNodeKeypairs.addAll(afterRemoveNodeKeypairs);
		}
		if(CollectionUtils.isNotEmpty(addNodes)) {
			List<NodeKeypairBo> newNodeKeypairs = addNodes.stream().map(
					item -> vaultKeypairBuildService.getNodeKeypair(consortiumId, X509CertConstant.DEFAULT_ALGO_NODE, item, null))
					.collect(Collectors.toList());
			keypairDas.addKeyPairInfo(newNodeKeypairs);
			leftNodeKeypairs.addAll(newNodeKeypairs);
		}
		List<KeypairPo> leftKeypairs = leftNodeKeypairs.stream().map(NodeKeypairBo::getKeypair).collect(Collectors.toList());
		return leftKeypairs;
	}
}
