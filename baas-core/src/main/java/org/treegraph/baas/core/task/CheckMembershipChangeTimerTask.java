package org.treegraph.baas.core.task;

import java.math.BigInteger;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.treegraph.baas.core.das.MembershipChangeDas;
import org.treegraph.baas.core.das.NodeDas;
import org.treegraph.baas.core.domain.MembershipChangePo;
import org.treegraph.baas.core.domain.NodePo;
import org.treegraph.baas.core.service.impl.JsonRpcService;

import com.google.common.collect.Lists;

import io.netty.util.Timeout;
import io.netty.util.TimerTask;

@Validated
@Service
public class CheckMembershipChangeTimerTask implements TimerTask{
	
	private Long membershipChangeId;
	
	@Autowired
	private MembershipChangeDas membershipChangeDas;
	
	@Autowired
    private NodeDas nodeDas;
	
	@Autowired
	private JsonRpcService jsonRpcService;
    
    public CheckMembershipChangeTimerTask(Long membershipChangeId) {
    	this.membershipChangeId = membershipChangeId;
    }
    
	@Override
	public void run(Timeout timeout) throws Exception {
		/**
		 * get committee nodes
		 */
		MembershipChangePo membershipChange = membershipChangeDas.findById(membershipChangeId)
				.orElseThrow(() -> new RuntimeException("membershipChange not found!"));
		List<String> committeeNodeIds = Lists.newArrayList(membershipChange.getCommitteeNodes().split(","));
		BigInteger preBftEpochNumber = BigInteger.valueOf(membershipChange.getPreBftEpochNumber());
		List<NodePo> committeeNodes = nodeDas.findByIds(committeeNodeIds);
		
    	/**
    	 * send request
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
    }
}
