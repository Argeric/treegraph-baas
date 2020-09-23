package org.treegraph.baas.core.task;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

import org.treegraph.baas.core.das.MembershipChangeDas;
import org.treegraph.baas.core.das.MembershipDas;
import org.treegraph.baas.core.das.NodeDas;
import org.treegraph.baas.core.domain.MembershipChangePo;
import org.treegraph.baas.core.domain.MembershipPo;
import org.treegraph.baas.core.domain.NodePo;
import org.treegraph.baas.core.enums.MembershipEnum;
import org.treegraph.baas.core.enums.NodeEnum.MembershipStatus;
import org.treegraph.baas.core.service.impl.JsonRpcService;
import org.treegraph.baas.core.util.SpringContextUtils;

import com.google.common.collect.Lists;

import io.netty.util.Timeout;
import io.netty.util.TimerTask;

public class CheckMembershipChangeTimerTask implements TimerTask{
	
	private Long membershipChangeId;
    
    public CheckMembershipChangeTimerTask(Long membershipChangeId) {
    	this.membershipChangeId = membershipChangeId;
    }
    
	@Override
	public void run(Timeout timeout) throws Exception {
		/**
		 * get service bean
		 */
		MembershipChangeDas membershipChangeDas = SpringContextUtils.getBean(MembershipChangeDas.class);
		NodeDas nodeDas = SpringContextUtils.getBean(NodeDas.class);
		JsonRpcService jsonRpcService = SpringContextUtils.getBean(JsonRpcService.class);
		MembershipDas membershipDas = SpringContextUtils.getBean(MembershipDas.class);
		
		/**
		 * get pre bftEpochNumber and committee nodes
		 */
		MembershipChangePo membershipChange = membershipChangeDas.findById(membershipChangeId)
				.orElseThrow(() -> new RuntimeException("membershipChange not found!"));
		BigInteger preBftEpochNumber = BigInteger.valueOf(membershipChange.getPreBftEpochNumber());
		List<String> committeeNodeIds = Lists.newArrayList(membershipChange.getCommitteeNodes().split(","));
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
    	boolean checkResult = Boolean.FALSE;
    	for(BigInteger bftEpochNumber : bftEpochNumbers){
    		checkResult = checkResult || bftEpochNumber.compareTo(preBftEpochNumber) > 0;
    	}
    	
    	/**
    	 * update membership status
    	 */
    	MembershipEnum.Type changeType = MembershipEnum.Type.getByCode(membershipChange.getChangeType());
		MembershipStatus nodeMembershipStatus = checkResult
				? (changeType == MembershipEnum.Type.ADD ? MembershipStatus.ADD_SUCCESS : MembershipStatus.REMOVE_SUCCESS)
				: (changeType == MembershipEnum.Type.ADD ? MembershipStatus.ADD_FAILURE : MembershipStatus.REMOVE_FAILURE);
		MembershipEnum.Status membershipChangeStatus = checkResult ? MembershipEnum.Status.CHECK_MEMBERSHIP_CHANGE_SUCCESS
				: MembershipEnum.Status.CHECK_MEMBERSHIP_CHANGE_FAILURE;
    	List<MembershipPo> memberships = membershipDas.findByMembershipChangeId(membershipChangeId);
    	List<String> nodeIds = memberships.stream().map(MembershipPo::getNodeId).collect(Collectors.toList());
    	nodeDas.modifyNodesMembershipStatus(nodeIds, nodeMembershipStatus, membershipChange.getId(), membershipChangeStatus);
    }
}
