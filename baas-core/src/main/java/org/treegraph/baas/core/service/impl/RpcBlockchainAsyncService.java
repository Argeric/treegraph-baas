package org.treegraph.baas.core.service.impl;

import java.math.BigInteger;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.treegraph.baas.core.constant.ConfluxConstant;
import org.treegraph.baas.core.das.bo.NodeKeypairBo;
import org.treegraph.baas.core.heartbeat.bo.Heartbeat;
import org.treegraph.baas.core.heartbeat.impl.HeartbeatLinstener;
import org.treegraph.baas.core.util.ConfluxUtils;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import conflux.web3j.Cfx;
import conflux.web3j.Request;
import conflux.web3j.response.BigIntResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Validated
@Service
@AllArgsConstructor
@Slf4j
public class RpcBlockchainAsyncService {
	
	@Async
    public void checkNodeClient(List<NodeKeypairBo> existNodeKeypairs, List<NodeKeypairBo> addNodeKeypairs,
			List<NodeKeypairBo> removeNodeKeypairs) {
    	/**
    	 * get expect epoch number
    	 */
    	int expectValue = existNodeKeypairs.size();
    	if(CollectionUtils.isNotEmpty(addNodeKeypairs)) {
    		expectValue += addNodeKeypairs.size();
    	}
    	Set<String> removedNodeIds = Sets.newHashSet();
    	if(CollectionUtils.isNotEmpty(removeNodeKeypairs)) {
    		expectValue -= removeNodeKeypairs.size();
    		removeNodeKeypairs.stream().forEach(item -> removedNodeIds.add(item.getNode().getId()));
    	}
    	
    	/**
    	 * wait 60 sec, and then send request
    	 */
    	try {
			TimeUnit.SECONDS.sleep(60);
		} catch (InterruptedException e) {
		}
    	log.info("checkNodeClient sleeped--->60sec");
    	List<Integer> bftEpochNumbers = Lists.newArrayList();
    	existNodeKeypairs.stream().filter(item -> !removedNodeIds.contains(item.getNode().getId())).forEach(item -> {
    		Heartbeat heartbeat = HeartbeatLinstener.getInstance().getNode(item.getNode().getConsortiumId(), item.getNode().getId());
	    	String uri = ConfluxUtils.toJsonRpcUri(item.getNode().getNodeIp(), heartbeat.getPort());
	    	Cfx cfx = Cfx.create(uri);
	    	log.info("checkNodeClient uri--->{}", uri);
	    	Request<BigInteger, BigIntResponse> getBftEpochNumberRequest = cfx.getCustomizedRequest(BigIntResponse.class, ConfluxConstant.RPC_GET_BFT_EPOCH_NUMBER);
	    	log.info("checkNodeClient uri--->{}, txRequest--->{}", uri, JSON.toJSONString(getBftEpochNumberRequest));
	    	BigInteger getBftEpochNumberResponse = getBftEpochNumberRequest.sendAndGet();
	    	bftEpochNumbers.add(getBftEpochNumberResponse.intValue());
	    	log.info("checkNodeClient uri--->{}, txRequest--->{}, txResponse--->{}", uri, JSON.toJSONString(getBftEpochNumberRequest), getBftEpochNumberResponse);
    	});
    	
    	/**
    	 * check bftEpochNumber with expect value
    	 */
    	boolean checkResult = Boolean.TRUE;
    	for(int bftEpochNumber : bftEpochNumbers){
    		checkResult = checkResult && (bftEpochNumber == expectValue);
    	}
    	log.info("checkNodeClient checkResult--->{}", checkResult);
    }
}
