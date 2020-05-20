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
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.treegraph.baas.api.dto.AuthInfo;
import org.treegraph.baas.api.dto.response.LoginResponse;
import org.treegraph.baas.core.das.PermissionDas;
import org.treegraph.baas.core.das.RoleDas;
import org.treegraph.baas.core.das.UserInfoDas;
import org.treegraph.baas.core.das.UserTokenDas;
import org.treegraph.baas.core.domain.PermissionPo;
import org.treegraph.baas.core.domain.RolePermissionPo;
import org.treegraph.baas.core.domain.RolePo;
import org.treegraph.baas.core.domain.UserInfoPo;
import org.treegraph.baas.core.domain.UserRolePo;
import org.treegraph.baas.core.enums.LogEnum.Login;
import org.treegraph.baas.core.exception.AlertException;
import org.treegraph.baas.core.enums.UserEnum;
import org.treegraph.baas.core.service.LoginService;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

@Validated
@Service
public class LoginServicempl implements LoginService {

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
        Assert.isTrue(UserEnum.Status.isAvailable(user.getStatus()), "用户已禁用");
        Assert.state(passwordService.matches(password, user.getPassword()), "密码错误");
        
        boolean needChangePwd = password.equals(defaultPassword);
        LoginResponse userDetailInfo = loginAfter(user, needChangePwd, Login.LOGIN, request);
        
        return userDetailInfo;
    }

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
        String token = userTokenDas.createToken(user);
        userDetailInfo.setToken(token);

        logService.addLoginLog(user.getId(), login, request);

        return userDetailInfo;
    }
    
    private List<AuthInfo> getAuthInfos(UserInfoPo user) {

		List<AuthInfo> authInfos = new ArrayList<>();
		List<UserRolePo> userRoles = roleDas.findUserRolesByUserId(user.getId());
		if(CollectionUtils.isEmpty(userRoles)) {
			return authInfos;
		}
		List<String> roleIds = userRoles.stream().map(item -> item.getRoleId()).collect(Collectors.toList());
		List<RolePo> roles = roleDas.findByIds(roleIds);
		Map<String, List<RolePo>> groupedRoleIds = roles.stream()
				.collect(Collectors.groupingBy(RolePo::getConsortiumId));

		groupedRoleIds.entrySet().stream().forEach(entry -> {
			List<RolePo> roleListGrouped = entry.getValue();
			Set<String> roleIdSet = Sets.newHashSet();
			Set<String> roleNameSet = Sets.newHashSet();
			roleListGrouped.stream().forEach(item -> {
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

			AuthInfo authInfo = AuthInfo.builder().consId(entry.getKey()).roles(Lists.newArrayList(roleNameSet))
					.permissions(permissionCodes).build();
			authInfos.add(authInfo);
		});

		return authInfos;
	}
}
