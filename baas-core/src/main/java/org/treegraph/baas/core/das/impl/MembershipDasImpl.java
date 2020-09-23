package org.treegraph.baas.core.das.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.treegraph.baas.core.das.MembershipDas;
import org.treegraph.baas.core.domain.MembershipPo;
import org.treegraph.baas.core.domain.MembershipPoExample;
import org.treegraph.baas.core.mapper.primary.MembershipPoMapper;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MembershipDasImpl implements MembershipDas {

	private MembershipPoMapper membershipPoMapper;
	
	public List<MembershipPo> findByMembershipChangeId(long membershipChangeId) {
		MembershipPoExample example = new MembershipPoExample();
		MembershipPoExample.Criteria criteria = example.createCriteria();
		criteria.andMembershipChangeIdEqualTo(membershipChangeId);
		return membershipPoMapper.selectByExample(example);
	}
}
