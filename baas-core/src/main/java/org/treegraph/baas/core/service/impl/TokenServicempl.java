package org.treegraph.baas.core.service.impl;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.treegraph.baas.core.das.UserInfoDas;
import org.treegraph.baas.core.das.UserTokenDas;
import org.treegraph.baas.core.domain.UserInfoPo;
import org.treegraph.baas.core.domain.UserTokenPo;
import org.treegraph.baas.core.exception.AlertException;
import org.treegraph.baas.core.service.TokenService;
import org.treegraph.baas.core.util.JwtUtils;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TokenServicempl implements TokenService {

    private UserInfoDas userInfoDas;
    
    private UserTokenDas userTokenDas;
	
    @Override
    public UserInfoPo checkToken(String token) {
    	
    	// parse token
    	Map<String, Object> paraMap = JwtUtils.validateToken(token);
    	
    	// check user
    	String userId = JwtUtils.getUid(paraMap);
    	String account = JwtUtils.getAccount(paraMap);
    	String name = JwtUtils.getName(paraMap);
    	UserInfoPo user = userInfoDas.findById(userId).orElseThrow(() -> new AlertException("用户不存在["+userId+"]"));
    	Assert.isTrue(user.getAccount().equals(account), "用户账户不一致["+userId+"]");
    	Assert.isTrue(user.getName().equals(name), "用户姓名不一致["+userId+"]");
    	
    	// check token value
    	UserTokenPo userToken = userTokenDas.findByUserId(userId);
    	Assert.isTrue(userToken.getToken().equals(token), "token失效，请重新登陆["+userId+"]");
    	
    	// check token expire
    	int expireSecs = userToken.getExpire();
    	long loginSecs = LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8")) - userToken.getCreateTime().toEpochSecond(ZoneOffset.of("+8"));
    	Assert.isTrue(loginSecs - expireSecs < 0, "token失效，请重新登陆!["+userId+"]");
    	
    	return user;
    }
}
