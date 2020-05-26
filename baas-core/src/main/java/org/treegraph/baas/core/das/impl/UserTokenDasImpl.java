package org.treegraph.baas.core.das.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.treegraph.baas.core.das.UserTokenDas;
import org.treegraph.baas.core.domain.UserInfoPo;
import org.treegraph.baas.core.domain.UserLoginTokenPo;
import org.treegraph.baas.core.domain.UserLoginTokenPoExample;
import org.treegraph.baas.core.mapper.UserLoginTokenPoMapper;
import org.treegraph.baas.core.util.CacheTimeUtil;
import org.treegraph.baas.core.util.JwtUtils;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserTokenDasImpl implements UserTokenDas {

	private UserLoginTokenPoMapper userLoginTokenPoMapper;

	/**
	 * token的过期时间 到凌晨00:00
	 */
	@Override
	public String createToken(UserInfoPo user) {

		long expireSecs = CacheTimeUtil.getSecondToMorning();
		String token = JwtUtils.generateToken(user.getId(), user.getAccount(), user.getName(), expireSecs);

		UserLoginTokenPo userToken = new UserLoginTokenPo();
		userToken.setUserId(user.getId());
		userToken.setExpire((int) (expireSecs));
		userToken.setToken(token);
		userToken.setCreateTime(LocalDateTime.now());

		UserLoginTokenPo existToken = findByUserId(user.getId());
		if (Objects.isNull(existToken)) {
			userLoginTokenPoMapper.insertSelective(userToken);
		} else {
			userToken.setId(existToken.getId());
			userLoginTokenPoMapper.updateByPrimaryKeySelective(userToken);
		}

		return token;
	}

	@Override
	public String updateByUserId(String token, String userId) {
		return null;
	}

	@Override
	public boolean deleteByUserId(String userId) {
		UserLoginTokenPoExample userTokenPoExample = new UserLoginTokenPoExample();
		userTokenPoExample.createCriteria().andUserIdEqualTo(userId);
		return userLoginTokenPoMapper.deleteByExample(userTokenPoExample) > 0;
	}

	@Override
	public UserLoginTokenPo findByUserId(String userId) {
		UserLoginTokenPoExample userTokenPoExample = new UserLoginTokenPoExample();
		userTokenPoExample.createCriteria().andUserIdEqualTo(userId);
		List<UserLoginTokenPo> userTokens = userLoginTokenPoMapper.selectByExample(userTokenPoExample);
		return userTokens.size() > 0 ? userTokens.get(0) : null;
	}
}
