package org.treegraph.baas.core.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.treegraph.baas.api.dto.AuthInfo;
import org.treegraph.baas.api.dto.response.LoginResponse;
import org.treegraph.baas.core.dao.PermissionDao;
import org.treegraph.baas.core.dao.RoleDao;
import org.treegraph.baas.core.dao.UserInfoDao;
import org.treegraph.baas.core.dao.UserTokenDao;
import org.treegraph.baas.core.domain.PermissionPo;
import org.treegraph.baas.core.domain.RolePermissionPo;
import org.treegraph.baas.core.domain.RolePo;
import org.treegraph.baas.core.domain.UserInfoPo;
import org.treegraph.baas.core.domain.UserRolePo;
import org.treegraph.baas.core.enums.LogEnum.Login;
import org.treegraph.baas.core.enums.UserEnum;
import org.treegraph.baas.core.service.LoginService;
import org.treegraph.baas.core.util.LogSkip;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

@Validated
@Service
public class LoginServicempl implements LoginService {

	@Autowired
    private UserInfoDao userInfoDao;
    
	@Autowired
    private UserTokenDao userTokenDao;
	
	@Autowired
    private RoleDao roleDao;
	
	@Autowired
    private PermissionDao permissionDao;
    
	@Autowired
    private PasswordService passwordService;
    
	@Autowired
    private LogService logService;
    
    @Value("${password.default}")
    private String defaultPassword;

    @Override
    @LogSkip
    public LoginResponse login(String accountNo, String password, HttpServletRequest request) {
    	
    	UserInfoPo user = userInfoDao.findByAccountNo(accountNo);
    	Assert.notNull(user, "用户不存在");
        Assert.isTrue(UserEnum.Status.isAvailable(user.getStatus()), "用户已禁用");
        Assert.state(passwordService.matches(password, user.getPassword()), "密码错误");
        
        boolean needChangePwd = password.equals(defaultPassword);
        LoginResponse userDetailInfo = loginAfter(user, needChangePwd, Login.LOGIN, request);
        
        return userDetailInfo;
    }

    @LogSkip
    private LoginResponse loginAfter(UserInfoPo user, boolean needChangePwd, Login login,
        HttpServletRequest request) {
    	
    	LoginResponse  userDetailInfo = LoginResponse.builder()
    			.account(user.getAccount())
    			.email(user.getEmail())
    			.name(user.getName())
    			.phone(user.getPhone())
    			.userId(user.getId())
    			.isFirstLogin(needChangePwd)
    			.build();

    	List<AuthInfo> authInfo = getAuthInfos(user);
    	userDetailInfo.setAuth(authInfo);
        String token = userTokenDao.createToken(user);
        userDetailInfo.setToken(token);

        logService.addLoginLog(user.getId(), login, request);

        return userDetailInfo;
    }
    
	public List<AuthInfo> getAuthInfos(UserInfoPo user) {

		List<AuthInfo> authInfos = new ArrayList<>();

		List<UserRolePo> userRoles = roleDao.findUserRolesByUserId(user.getId());
		List<Long> roleIds = userRoles.stream().map(item -> item.getRoleId()).collect(Collectors.toList());
		List<RolePo> roles = roleDao.findRolesByIds(roleIds);
		Map<String, List<RolePo>> groupedRoleIds = roles.stream()
				.collect(Collectors.groupingBy(RolePo::getConsortiumId));

		groupedRoleIds.entrySet().stream().forEach(entry -> {
			List<RolePo> roleListGrouped = entry.getValue();
			Set<Long> roleIdSet = Sets.newHashSet();
			Set<String> roleNameSet = Sets.newHashSet();
			roleListGrouped.stream().forEach(item -> {
				roleIdSet.add(item.getId());
				roleNameSet.add(item.getRoleName());
			});

			List<RolePermissionPo> rolePermissions = permissionDao
					.findRolePermissionsByRoleIds(Lists.newArrayList(roleIdSet));
			List<Long> permissionIds = rolePermissions.stream().map(RolePermissionPo::getPermissionId)
					.collect(Collectors.toList());
			List<PermissionPo> permissions = permissionDao.findPermissionsByIds(permissionIds);
			List<String> permissionCodes = permissions.stream().map(PermissionPo::getPermissionCode)
					.collect(Collectors.toList());

			AuthInfo authInfo = AuthInfo.builder().consId(entry.getKey()).roles(Lists.newArrayList(roleNameSet))
					.permissions(permissionCodes).build();
			authInfos.add(authInfo);
		});

		return authInfos;
	}
}
