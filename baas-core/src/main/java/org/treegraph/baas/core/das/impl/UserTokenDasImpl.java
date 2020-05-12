package org.treegraph.baas.core.das.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.treegraph.baas.core.das.UserTokenDas;
import org.treegraph.baas.core.domain.UserInfoPo;
import org.treegraph.baas.core.domain.UserTokenPo;
import org.treegraph.baas.core.domain.UserTokenPoExample;
import org.treegraph.baas.core.mapper.UserTokenPoMapper;
import org.treegraph.baas.core.util.CacheTimeUtil;
import org.treegraph.baas.core.util.JwtUtils;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserTokenDasImpl implements UserTokenDas {

	private UserTokenPoMapper userTokenPoMapper;

	/**
	 * token的过期时间 到凌晨00:00
	 */
	@Override
	public String createToken(UserInfoPo user) {

		long expireSecs = CacheTimeUtil.getSecondToMorning();
		String token = JwtUtils.generateToken(user.getId(), user.getAccount(), user.getName(), expireSecs);

		UserTokenPo userToken = new UserTokenPo();
		userToken.setUserId(user.getId());
		userToken.setExpire((int) (expireSecs));
		userToken.setToken(token);
		userToken.setCreateTime(LocalDateTime.now());

		UserTokenPo existToken = findByUserId(user.getId());
		if (Objects.isNull(existToken)) {
			userTokenPoMapper.insertSelective(userToken);
		} else {
			userToken.setId(existToken.getId());
			userTokenPoMapper.updateByPrimaryKeySelective(userToken);
		}

		return token;
	}

	@Override
	public String updateByUserId(String token, String userId) {
		return null;
	}

	@Override
	public boolean deleteByUserId(String userId) {
		UserTokenPoExample userTokenPoExample = new UserTokenPoExample();
		userTokenPoExample.createCriteria().andUserIdEqualTo(userId);
		return userTokenPoMapper.deleteByExample(userTokenPoExample) > 0;
	}

	@Override
	public UserTokenPo findByUserId(String userId) {
		UserTokenPoExample userTokenPoExample = new UserTokenPoExample();
		userTokenPoExample.createCriteria().andUserIdEqualTo(userId);
		List<UserTokenPo> userTokens = userTokenPoMapper.selectByExample(userTokenPoExample);
		return userTokens.size() > 0 ? userTokens.get(0) : null;
	}
}
