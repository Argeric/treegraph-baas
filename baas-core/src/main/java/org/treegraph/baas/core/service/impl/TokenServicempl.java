package org.treegraph.baas.core.service.impl;

import java.util.Calendar;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.treegraph.baas.core.dao.UserInfoDao;
import org.treegraph.baas.core.dao.UserTokenDao;
import org.treegraph.baas.core.domain.UserInfoPo;
import org.treegraph.baas.core.domain.UserTokenPo;
import org.treegraph.baas.core.service.TokenService;
import org.treegraph.baas.core.util.JwtUtils;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TokenServicempl implements TokenService {

    private UserInfoDao userInfoDao;
    
    private UserTokenDao userTokenDao;
	
    @Override
    public UserInfoPo checkToken(String token) {
    	
    	// parse token
    	Map<String, Object> paraMap = JwtUtils.validateToken(token);
    	
    	// check user
    	String userId = JwtUtils.getUid(paraMap);
    	String account = JwtUtils.getAccount(paraMap);
    	String name = JwtUtils.getName(paraMap);
    	UserInfoPo user = userInfoDao.findById(userId);
    	Assert.notNull(user, "用户不存在["+userId+"]");
    	Assert.isTrue(user.getAccount().equals(account), "用户账户不一致["+userId+"]");
    	Assert.isTrue(user.getName().equals(name), "用户姓名不一致["+userId+"]");
    	
    	// check token value
    	UserTokenPo userToken = userTokenDao.findByUserId(userId);
    	Assert.isTrue(userToken.getToken().equals(token), "token失效，请重新登陆["+userId+"]");
    	
    	// check token expire
    	int expireSecs = userToken.getExpire();
    	long diffSecs = (Calendar.getInstance().getTime().getTime() - userToken.getCreateTime().getTime())/1000;
    	Assert.isTrue(diffSecs - expireSecs > 0, "token失效，请重新登陆!["+userId+"]");
    	
    	return user;
    }
}
