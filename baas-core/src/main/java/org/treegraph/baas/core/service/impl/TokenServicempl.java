package org.treegraph.baas.core.service.impl;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.treegraph.baas.common.util.WebHelper;
import org.treegraph.baas.core.das.InviteUserDas;
import org.treegraph.baas.core.das.PermissionDas;
import org.treegraph.baas.core.das.UserInfoDas;
import org.treegraph.baas.core.das.UserTokenDas;
import org.treegraph.baas.core.domain.InviteUserPo;
import org.treegraph.baas.core.domain.UserInfoPo;
import org.treegraph.baas.core.domain.UserTokenPo;
import org.treegraph.baas.core.exception.AlertException;
import org.treegraph.baas.core.service.TokenService;
import org.treegraph.baas.core.util.JwtUtils;

import com.google.common.collect.Sets;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TokenServicempl implements TokenService {

    private UserInfoDas userInfoDas;
    
    private UserTokenDas userTokenDas;
    
    private InviteUserDas inviteUserDas;
    
    private PermissionDas permissionDas;
	
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
    
    public InviteUserPo checkInvitToken(String token) {
    	
    	// parse token
    	Map<String, Object> paraMap = JwtUtils.validateInviteToken(token);
    	
    	// check invite
    	Long inviteId = Long.parseLong(JwtUtils.getInviteId(paraMap));
    	String inviter = JwtUtils.getInviter(paraMap);
    	String accepter = JwtUtils.getAccepter(paraMap);
    	InviteUserPo invite = inviteUserDas.findById(inviteId).orElseThrow(() -> new AlertException("邀请不存在["+inviteId+"]"));
    	Assert.isTrue(invite.getInviter().equals(inviter), "邀请人不一致["+invite.getInviter()+"]");
    	Assert.isTrue(invite.getAccepter().equals(accepter), "被邀请人不一致["+invite.getAccepter()+"]");
    	Assert.isTrue(inviter.equals(WebHelper.getUserId()), "被邀请人和登陆用户不一致["+invite.getInviter()+"]["+WebHelper.getUserId()+"]");
    	
    	// check token expire
    	int expireSecs = invite.getExpire();
    	long loginSecs = LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8")) - invite.getCreateTime().toEpochSecond(ZoneOffset.of("+8"));
    	Assert.isTrue(loginSecs - expireSecs < 0, "邀请失效，请重新让管理员发邀请!["+inviteId+"]");
    	
    	return invite;
    }
    
    public boolean authPermission(String userId, String uri) {
    	List<String> permCodeList = permissionDas.findPermissionCodeByUserId(userId);
    	Set<String> permCodeSet = Sets.newHashSet(permCodeList);
    	return false;
    }
}
