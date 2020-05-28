package org.treegraph.baas.core.das.impl;

import java.util.List;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.treegraph.baas.core.das.UserOperLogDas;
import org.treegraph.baas.core.domain.UserOperLogPo;
import org.treegraph.baas.core.domain.UserOperLogPoExample;
import org.treegraph.baas.core.domain.UserOperLogPoExample.Criteria;
import org.treegraph.baas.core.mapper.primary.UserOperLogPoMapper;
import org.treegraph.baas.core.util.LogSkip;

import lombok.AllArgsConstructor;

/**
 * @author zt
 * @date May 11, 2020
 * @version 1.0
 */
@Service
@AllArgsConstructor
public class UserOperLogDasImpl implements UserOperLogDas{
	
	private UserOperLogPoMapper userOperLogPoMapper;
	
	@Async
	@LogSkip
	public boolean addOperLog(UserOperLogPo userOperLog) {
		return userOperLogPoMapper.insert(userOperLog) > 0;
	}
	
	@LogSkip
	public List<UserOperLogPo> findAllOperLog(String userId, String userName, String operCode, String ip) {
		UserOperLogPoExample example = new UserOperLogPoExample();
		Criteria criteria = example.createCriteria();
		if (userId != null) {
			criteria.andUserIdEqualTo(userId);
		}
		if (userName != null) {
			criteria.andUserNameEqualTo(userName);
		}
		if (operCode != null) {
			criteria.andOperCodeEqualTo(operCode);
		}
		if (ip != null) {
			criteria.andUserIpEqualTo(ip);
		}
		example.setOrderByClause("create_time desc");
		return userOperLogPoMapper.selectByExample(example);
	}
}
