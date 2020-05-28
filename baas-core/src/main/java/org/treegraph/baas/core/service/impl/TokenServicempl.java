package org.treegraph.baas.core.service.impl;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.treegraph.baas.common.util.PermHelper;
import org.treegraph.baas.common.util.WebHelper;
import org.treegraph.baas.core.das.InviteUserDas;
import org.treegraph.baas.core.das.PermissionDas;
import org.treegraph.baas.core.das.UserInfoDas;
import org.treegraph.baas.core.das.UserTokenDas;
import org.treegraph.baas.core.das.bo.PermissionInfoBo;
import org.treegraph.baas.core.domain.UserInfoPo;
import org.treegraph.baas.core.domain.UserInvitePo;
import org.treegraph.baas.core.domain.UserLoginTokenPo;
import org.treegraph.baas.core.enums.UserEnum;
import org.treegraph.baas.core.enums.UserEnum.PermissionPoint;
import org.treegraph.baas.core.exception.AlertException;
import org.treegraph.baas.core.service.TokenService;
import org.treegraph.baas.core.util.CollectionUtils;
import org.treegraph.baas.core.util.JwtUtils;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class TokenServicempl implements TokenService {

    private UserInfoDas userInfoDas;
    
    private UserTokenDas userTokenDas;
    
    private InviteUserDas inviteUserDas;
    
    private PermissionDas permissionDas;
    
    private static Set<String> URI_PERMISSION_WHITELIST = Sets.newHashSet();
    
    private static Map<String, String> URI_PERMISSION_MAP = Maps.newHashMap();
    
    static {
    	// white list
    	URI_PERMISSION_WHITELIST.add("/user/invite/accept");
    	URI_PERMISSION_WHITELIST.add("/user/pwd/modify");
    	// user module
    	URI_PERMISSION_MAP.put("/user/list", PermissionPoint.VIEW_CONS_USER.getCode());
    	URI_PERMISSION_MAP.put("/user/query", PermissionPoint.VIEW_CONS_USER.getCode());
    	URI_PERMISSION_MAP.put("/user/create", PermissionPoint.CREATE_CONS_USER.getCode());
    	URI_PERMISSION_MAP.put("/user/modify", PermissionPoint.UPDATE_CONS_USER.getCode());
    	URI_PERMISSION_MAP.put("/user/invite", PermissionPoint.CREATE_CONS_USER.getCode());
    	URI_PERMISSION_MAP.put("/user/invite/accept", null);
    	URI_PERMISSION_MAP.put("/user/pwd/modify", null);
    	URI_PERMISSION_MAP.put("/user/del", PermissionPoint.UPDATE_CONS_USER.getCode());
    	// role module
    	URI_PERMISSION_MAP.put("/role/list", "");
    	URI_PERMISSION_MAP.put("/role/query", "");
    	URI_PERMISSION_MAP.put("/role/create", "");
    	URI_PERMISSION_MAP.put("/role/modify", "");
    	URI_PERMISSION_MAP.put("/role/del", "");
    	// consortium module
    	URI_PERMISSION_MAP.put("/cons/list", PermissionPoint.VIEW_CONS.getCode());
    	URI_PERMISSION_MAP.put("/cons/query", PermissionPoint.VIEW_CONS.getCode());
    	URI_PERMISSION_MAP.put("/cons/create", PermissionPoint.CREATE_CONS.getCode());
    	URI_PERMISSION_MAP.put("/cons/modify", PermissionPoint.UPDATE_CONS.getCode());
    	URI_PERMISSION_MAP.put("/cons/del", PermissionPoint.UPDATE_CONS.getCode());
    	URI_PERMISSION_MAP.put("/cons/admin/create", PermissionPoint.CREATE_CONS_ADMIN.getCode());
    	URI_PERMISSION_MAP.put("/cons/blockchain/create", PermissionPoint.CREATE_BLOCKCHAIN.getCode());
    	URI_PERMISSION_MAP.put("/cons/node/create", PermissionPoint.CREATE_NODE.getCode());
    	URI_PERMISSION_MAP.put("/cons/script/download", PermissionPoint.CREATE_BLOCKCHAIN.getCode());
    	// blockchain module
    	URI_PERMISSION_MAP.put("/blockchain/list", PermissionPoint.VIEW_BLOCKCHAIN.getCode());
    	URI_PERMISSION_MAP.put("/blockchain/detail", PermissionPoint.VIEW_BLOCKCHAIN.getCode());
    	URI_PERMISSION_MAP.put("/blockchain/ca/list", PermissionPoint.GENERATE_CA.getCode());
    	URI_PERMISSION_MAP.put("/blockchain/ca/download", PermissionPoint.GENERATE_CA.getCode());
    	URI_PERMISSION_MAP.put("/blockchain/stop", PermissionPoint.UPDATE_BLOCKCHAIN.getCode());
    	URI_PERMISSION_MAP.put("/blockchain/start", PermissionPoint.UPDATE_BLOCKCHAIN.getCode());
    	// node module
    	URI_PERMISSION_MAP.put("/node/list", PermissionPoint.VIEW_BLOCKCHAIN.getCode());
    	URI_PERMISSION_MAP.put("/node/detail", PermissionPoint.VIEW_BLOCKCHAIN.getCode());
    	URI_PERMISSION_MAP.put("/node/log", PermissionPoint.VIEW_NODE_LOG.getCode());
    	URI_PERMISSION_MAP.put("/node/modify", PermissionPoint.UPDATE_NODE.getCode());
    }
	
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
    	UserLoginTokenPo userToken = userTokenDas.findByUserId(userId);
    	Assert.isTrue(userToken.getToken().equals(token), "token失效，请重新登陆["+userId+"]");
    	
    	// check token expire
    	int expireSecs = userToken.getExpire();
    	long loginSecs = LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8")) - userToken.getCreateTime().toEpochSecond(ZoneOffset.of("+8"));
    	Assert.isTrue(loginSecs - expireSecs < 0, "token失效，请重新登陆!["+userId+"]");
    	
    	return user;
    }
    
    public UserInvitePo checkInvitToken(String token) {
    	// parse token
    	Map<String, Object> paraMap = JwtUtils.validateInviteToken(token);
    	
    	// check invite
    	Long inviteId = Long.parseLong(JwtUtils.getInviteId(paraMap));
    	String inviter = JwtUtils.getInviter(paraMap);
    	String accepter = JwtUtils.getAccepter(paraMap);
    	UserInvitePo invite = inviteUserDas.findById(inviteId).orElseThrow(() -> new AlertException("邀请不存在["+inviteId+"]"));
    	Assert.isTrue(invite.getInviter().equals(inviter), "邀请人不一致["+invite.getInviter()+"]");
    	Assert.isTrue(invite.getAccepter().equals(accepter), "被邀请人不一致["+invite.getAccepter()+"]");
    	Assert.isTrue(inviter.equals(WebHelper.getUserId()), "被邀请人和登陆用户不一致["+invite.getInviter()+"]["+WebHelper.getUserId()+"]");
    	
    	// check token expire
    	int expireSecs = invite.getExpire();
    	long loginSecs = LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8")) - invite.getCreateTime().toEpochSecond(ZoneOffset.of("+8"));
    	Assert.isTrue(loginSecs - expireSecs < 0, "邀请失效，请重新让管理员发邀请!["+inviteId+"]");
    	
    	return invite;
    }
    
    public void authPermission(String userId, String uri) {
    	if(URI_PERMISSION_WHITELIST.contains(uri)) {
    		return;
    	}
    	
    	boolean sysAdminFlag = Boolean.FALSE;
    	Set<String> consAdminSet = Sets.newHashSet();
    	Set<String> consIdSet = Sets.newHashSet();
    	Map<String, Set<String>> consPermMap = Maps.newHashMap();
    	List<PermissionInfoBo> permInfos = permissionDas.findPermissionInfoByUserId(userId);
    	for(PermissionInfoBo permInfo:permInfos){
    		Integer roleType = permInfo.getRoleType();
    		String consId = permInfo.getConsId();
    		String permCode = permInfo.getPermCode();
    		if(roleType == UserEnum.Role.SYS_ADMIN.getCode()) {
    			sysAdminFlag = Boolean.TRUE;
    		}
    		if(roleType == UserEnum.Role.CONSORTIUM_ADMIN.getCode()) {
    			consAdminSet.add(consId);
    		}
    		consIdSet.add(consId);
    		Set<String> permSet = consPermMap.get(consId);
    		if(permSet == null) {
    			permSet = Sets.newHashSet();
    			consPermMap.put(consId, permSet);
    		}
    		permSet.add(permCode);
    	}
    	String consIdsCond = getConsId(consIdSet);
    	PermHelper.init(sysAdminFlag, consAdminSet, consIdSet, consPermMap, consIdsCond);
    	
    	Set<String> permCodeSet = Sets.newHashSet();
    	consPermMap.values().stream().forEach(item -> permCodeSet.addAll(item));
    	String permCode = URI_PERMISSION_MAP.get(uri);
    	boolean permit = permCodeSet.contains(permCode);
    	log.info("permit======>"+permit+"\npermCode======>"+permCode+"\npermCodeSet======>"+JSON.toJSONString(permCodeSet));
    	//Assert.isTrue(permit, "没有此访问权限");
    }
    
	public String getConsId(Set<String> consIds) {
		if(CollectionUtils.isEmpty(consIds)) {
			return null;
		}
		StringJoiner cond = new StringJoiner(",", "(", ")");
		consIds.stream().forEach(item -> cond.add(new StringJoiner("", "'", "'").add(item).toString()));
		return cond.toString();
	}
}
