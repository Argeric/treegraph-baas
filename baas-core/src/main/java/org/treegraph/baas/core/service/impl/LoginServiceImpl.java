package org.treegraph.baas.core.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.treegraph.baas.api.dto.AuthInfo;
import org.treegraph.baas.api.dto.response.LoginResponse;
import org.treegraph.baas.core.constant.UserConstant;
import org.treegraph.baas.core.das.PermissionDas;
import org.treegraph.baas.core.das.RoleDas;
import org.treegraph.baas.core.das.UserInfoDas;
import org.treegraph.baas.core.das.UserTokenDas;
import org.treegraph.baas.core.das.bo.PermissionInfoBo;
import org.treegraph.baas.core.domain.*;
import org.treegraph.baas.core.enums.LogEnum.Login;
import org.treegraph.baas.core.enums.UserEnum;
import org.treegraph.baas.core.exception.AlertException;
import org.treegraph.baas.core.service.LogService;
import org.treegraph.baas.core.service.LoginService;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class LoginServiceImpl implements LoginService {

	@Autowired
    private UserInfoDas userInfoDas;
    
	@Autowired
    private UserTokenDas userTokenDas;
	
	@Autowired
    private RoleDas roleDas;
	
	@Autowired
    private PermissionDas permissionDas;
    
	@Autowired
    private PasswordService passwordService;
    
	@Autowired
    private LogService logService;
    
    @Value("${password.default}")
    private String defaultPassword;

    @Override
    public LoginResponse login(String accountNo, String password, HttpServletRequest request) {
    	
		UserInfoPo user = userInfoDas.findByAccount(accountNo)
				.orElseThrow(() -> new AlertException("用户不存在"));
        Assert.isTrue(UserEnum.Status.isAvailable(user.getStatus()), "该用户已冻结，请联系管理员解冻");
        
        boolean isSysAdmin = isSysAdmin(user.getId());
        if(isSysAdmin) {
        	boolean flag = passwordService.matches(password, user.getPassword());
        	log.info("user:{},passwordService.matches:{}", user.getId(), flag);
        	Assert.isTrue(flag, "密码错误");
        } else {
	        UserInfoPo userUpdate = validateTryTimes(password, user);
	        log.info("user:{},userUpdate:{}", user.getId(), JSON.toJSONString(userUpdate));
	        Assert.isTrue(UserEnum.Status.isAvailable(userUpdate.getStatus()), "该用户已冻结，请联系管理员解冻");
	        log.info("user:{},userUpdate1:{}", user.getId(), JSON.toJSONString(userUpdate));
			Assert.isTrue(userUpdate.getTryTimes() == 0,
					"密码错误,共" + UserConstant.LOGIN_MAX_TRY_TIMES + "次尝试机会，还剩"
							+ (UserConstant.LOGIN_MAX_TRY_TIMES - userUpdate.getTryTimes()) + "次尝试");
			log.info("user:{},userUpdate2:{}", user.getId(), JSON.toJSONString(userUpdate));
        }
        
        boolean needChangePwd = password.equals(defaultPassword);

		return loginAfter(user, needChangePwd, request);
    }

	private UserInfoPo validateTryTimes(String password, UserInfoPo user) {
		boolean isMatch = passwordService.matches(password, user.getPassword());
		log.info("user:{},isMatch:{}", user.getId(), JSON.toJSONString(isMatch));
        int tryTimes = isMatch ? 0 : user.getTryTimes() + 1;
        int status = tryTimes >= UserConstant.LOGIN_MAX_TRY_TIMES ? UserEnum.Status.FROZEN.getCode() : user.getStatus();
        log.info("user:{},tryTimes:{},status:{}", user.getId(), tryTimes, status);
        UserInfoPo userUpdate = UserInfoPo.builder().id(user.getId()).tryTimes(tryTimes).status(status).build();
        log.info("user:{},userUpdate:{}", user.getId(), JSON.toJSONString(userUpdate));
        userInfoDas.updateUser(userUpdate);
		return userUpdate;
	}

    private LoginResponse loginAfter(UserInfoPo user, boolean needChangePwd,
									 HttpServletRequest request) {
    	LoginResponse  userDetailInfo = LoginResponse.builder()
    			.account(user.getAccount())
    			.email(user.getEmail())
    			.name(user.getName())
    			.phone(user.getPhone())
    			.userId(user.getId())
    			.isFirstLogin(needChangePwd)
    			.build();

    	List<AuthInfo> authInfo = getAuthInfo(user.getId());
    	userDetailInfo.setAuth(authInfo);
        String token = userTokenDas.createToken(user);
        userDetailInfo.setToken(token);

        logService.addLoginLog(user.getId(), user.getName(), Login.LOGIN, request);
        return userDetailInfo;
    }
    
    @SuppressWarnings("unused")
	private List<AuthInfo> getAuthInfo0(UserInfoPo user) {

		List<AuthInfo> authInfoList = new ArrayList<>();
		List<UserRolePo> userRoles = roleDas.findUserRolesByUserId(user.getId());
		if(CollectionUtils.isEmpty(userRoles)) {
			return authInfoList;
		}
		List<String> roleIds = userRoles.stream().map(UserRolePo::getRoleId).collect(Collectors.toList());
		List<RolePo> roles = roleDas.findByIds(roleIds);
		Map<String, List<RolePo>> groupedRoleIds = roles.stream()
				.collect(Collectors.groupingBy(RolePo::getConsortiumId));

		groupedRoleIds.forEach((key, roleListGrouped) -> {
			Set<String> roleIdSet = Sets.newHashSet();
			Set<String> roleNameSet = Sets.newHashSet();
			roleListGrouped.forEach(item -> {
				roleIdSet.add(item.getId());
				roleNameSet.add(item.getRoleName());
			});

			List<RolePermissionPo> rolePermissions = permissionDas
					.findRolePermissionsByRoleIds(Lists.newArrayList(roleIdSet));
			List<Long> permissionIds = rolePermissions.stream().map(RolePermissionPo::getPermissionId)
					.collect(Collectors.toList());
			List<PermissionPo> permissions = permissionDas.findByIds(permissionIds);
			List<String> permissionCodes = permissions.stream().map(PermissionPo::getPermissionCode)
					.collect(Collectors.toList());

			AuthInfo authInfo = AuthInfo.builder().consId(key).roles(Lists.newArrayList(roleNameSet))
					.permissions(permissionCodes).build();
			authInfoList.add(authInfo);
		});

		return authInfoList;
	}
    
    public List<AuthInfo> getAuthInfo(String userId) {
		List<PermissionInfoBo> permInfoBoList = permissionDas.findPermissionInfoByUserId(userId);
		Map<String, List<PermissionInfoBo>> permMap = permInfoBoList.stream()
				.collect(Collectors.groupingBy(PermissionInfoBo::getConsId));

		return permMap.entrySet().stream().map(item -> {
			Set<String> roleNames = Sets.newHashSet();
			Set<String> permCodes = Sets.newHashSet();
			List<PermissionInfoBo> permInfoList = item.getValue();
			permInfoList.forEach(permInfo -> {
				roleNames.add(permInfo.getRoleName());
				permCodes.add(permInfo.getPermCode());
			});
			return AuthInfo.builder()
					.consId(item.getKey())
					.roles(Lists.newArrayList(roleNames))
					.permissions(Lists.newArrayList(permCodes))
					.build();
		}).collect(Collectors.toList());
	}
    
    public boolean isSysAdmin(String userId) {
    	List<PermissionInfoBo> permInfoList = permissionDas.findPermissionInfoByUserId(userId);
    	for(PermissionInfoBo permInfo:permInfoList){
    		Integer roleType = permInfo.getRoleType();
    		if(roleType == UserEnum.Role.SYS_ADMIN.getCode()) {
    			return Boolean.TRUE;
    		}
    	}
    	return  Boolean.FALSE;
    }
}
