package org.treegraph.baas.core.task;

import java.math.BigInteger;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.treegraph.baas.core.das.NodeDas;
import org.treegraph.baas.core.domain.NodePo;
import org.treegraph.baas.core.service.impl.JsonRpcService;

import com.google.common.collect.Lists;

import lombok.extern.slf4j.Slf4j;

@Validated
@Service
@Slf4j
public class CheckMembershipChangeTask implements Runnable{
	
	private String membershipChangeId;
	
	@Autowired
    private NodeDas nodeDas;
	
	@Autowired
	private JsonRpcService jsonRpcService;
    
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
    		BigInteger bftEpochNumber = jsonRpcService.getBftEpochNumber(item.getConsortiumId(), item.getId());
	    	bftEpochNumbers.add(bftEpochNumber);
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
