package org.treegraph.baas.core.das.impl;

import java.time.LocalDateTime;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.treegraph.baas.core.das.UserOperLogDas;
import org.treegraph.baas.core.domain.UserOperLogPo;
import org.treegraph.baas.core.mapper.UserOperLogPoMapper;
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
	public void addOperLog(String userId, String userName, String ip, String operCode, String operInfo) {
		
		LocalDateTime now = LocalDateTime.now();
		UserOperLogPo  userOperLog =  UserOperLogPo.builder()
				.userId(userId)
				.userName(userName)
				.userIp(ip)
				.operCode(operCode)
				.operInfo(operInfo)
				.operTime(now)
				.createTime(now)
				.build();
				
		userOperLogPoMapper.insert(userOperLog);
	}
}
