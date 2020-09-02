package org.treegraph.baas.core.task;

import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.treegraph.baas.core.constant.ConfluxConstant;
import org.treegraph.baas.core.das.NodeDas;
import org.treegraph.baas.core.domain.NodePo;
import org.treegraph.baas.core.heartbeat.bo.Heartbeat;
import org.treegraph.baas.core.heartbeat.impl.HeartbeatLinstener;
import org.treegraph.baas.core.util.ConfluxUtils;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;

import conflux.web3j.Cfx;
import conflux.web3j.Request;
import conflux.web3j.response.BigIntResponse;
import lombok.extern.slf4j.Slf4j;

@Validated
@Service
@Slf4j
public class CheckMembershipChangeTask implements Runnable{
	
	private String membershipChangeId;
	
	@Autowired
    private NodeDas nodeDas;
    
    public CheckMembershipChangeTask(String membershipChangeId) {
    	this.membershipChangeId = membershipChangeId;
    }
	
    public void run() {
		
		/**
		 * get committee nodes
		 */
    	//TODO QUERY FORM MEMBERSHIP_CHANGE TABLE
		List<String> committeeNodeIds = Lists.newArrayList();
		BigInteger preBftEpochNumber = BigInteger.ZERO;
		List<NodePo> committeeNodes = nodeDas.findByIds(committeeNodeIds);
		
    	/**
    	 * wait 60 sec, and then send request
    	 */
    	List<BigInteger> bftEpochNumbers = Lists.newArrayList();
    	committeeNodes.stream().forEach(item -> {
    		Heartbeat heartbeat = HeartbeatLinstener.getInstance().getNode(item.getConsortiumId(), item.getId());
	    	String uri = ConfluxUtils.toJsonRpcUri(heartbeat.getIp(), heartbeat.getPort());
	    	Cfx cfx = Cfx.create(uri, 1, 1, TimeUnit.SECONDS);
	    	Request<BigInteger, BigIntResponse> getBftEpochNumberRequest = cfx.getCustomizedRequest(BigIntResponse.class, ConfluxConstant.RPC_GET_BFT_EPOCH_NUMBER);
	    	log.info("checkNodeClient uri--->{}, txRequest--->{}", uri, JSON.toJSONString(getBftEpochNumberRequest));
	    	BigInteger getBftEpochNumberResponse = getBftEpochNumberRequest.sendAndGet();
	    	log.info("checkNodeClient uri--->{}, txRequest--->{}, txResponse--->{}", uri, JSON.toJSONString(getBftEpochNumberRequest), getBftEpochNumberResponse);
	    	bftEpochNumbers.add(getBftEpochNumberResponse);
    	});
    	
    	/**
    	 * check bftEpochNumber with expect value
    	 */
    	boolean checkResult = Boolean.TRUE;
    	for(BigInteger bftEpochNumber : bftEpochNumbers){
    		checkResult = checkResult && bftEpochNumber.compareTo(preBftEpochNumber) > 0;
    	}
    	log.info("checkNodeClient checkResult--->{}", checkResult);
    }
}
