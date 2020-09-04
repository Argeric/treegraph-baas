package org.treegraph.baas.core.das.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.treegraph.baas.core.das.MembershipChangeDas;
import org.treegraph.baas.core.domain.MembershipChangePo;
import org.treegraph.baas.core.domain.MembershipPo;
import org.treegraph.baas.core.mapper.primary.MembershipChangePoMapper;
import org.treegraph.baas.core.mapper.primary.MembershipPoMapper;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MembershipChangeDasImpl implements MembershipChangeDas {

	private MembershipChangePoMapper membershipChangePoMapper;
	
	private MembershipPoMapper membershipPoMapper;
	
	public Optional<MembershipChangePo> findById(long id) {
		return membershipChangePoMapper.selectByPrimaryKey(id);
	}
	
	public boolean addMembershipChange(MembershipChangePo membershipChange, List<MembershipPo> membershipList) {
		boolean succFlag =  Boolean.TRUE;
		// add membershipChange
		succFlag = succFlag && membershipChangePoMapper.insert(membershipChange) > 0;
		// add membershipList
		membershipList.stream().forEach(item -> item.setMembershipChangeId(membershipChange.getId()));
		succFlag = succFlag && membershipPoMapper.bulkInsert(membershipList) == membershipList.size();
		return succFlag;
	}
}
