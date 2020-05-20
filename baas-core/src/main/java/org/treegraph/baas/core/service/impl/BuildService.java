package org.treegraph.baas.core.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.treegraph.baas.common.enums.IntTypeEnum;
import org.treegraph.baas.common.util.WebHelper;
import org.treegraph.baas.core.das.PermissionDas;
import org.treegraph.baas.core.domain.RolePermissionPo;
import org.treegraph.baas.core.domain.RolePo;
import org.treegraph.baas.core.domain.UserInfoPo;
import org.treegraph.baas.core.domain.UserOrganizationPo;
import org.treegraph.baas.core.domain.UserRolePo;
import org.treegraph.baas.core.enums.UserEnum;
import org.treegraph.baas.core.service.bo.RoleInfoBo;
import org.treegraph.baas.core.service.bo.UserInfoBo;
import org.treegraph.baas.core.util.CollectionUtils;

import com.google.common.collect.Lists;

import lombok.AllArgsConstructor;

@Validated
@Service
@AllArgsConstructor
public class BuildService {
	
    private PermissionDas permissionDas;
	
    private PasswordService passwordService;

	public RoleInfoBo buildRole(int roleType, String roleName, String consId, List<String> reqPermissions) {
		
   		LocalDateTime now = LocalDateTime.now();
   		String oper = WebHelper.getUserId();
   		RolePo role = RolePo.builder()
   				.id(UUID.randomUUID().toString())
   				.consortiumId(consId)
   				.roleName(roleName)
   				.roleType(roleType)
   				.deleteFlag(IntTypeEnum.FALSE.getValue())
   				.createTime(now)
   				.createBy(oper)
   				.updateTime(now)
   				.updateBy(oper)
   				.build();
   		
   		List<RolePermissionPo> rolePermissionList = Lists.newArrayList();
		if(CollectionUtils.isNotEmpty(reqPermissions)) {
			Map<String, Long> permissionCodeIdMap = permissionDas.validatePermissions(reqPermissions);
   			reqPermissions.stream().forEach(item -> {
   				RolePermissionPo rolePerm = RolePermissionPo.builder()
   						.roleId(role.getId())
   						.permissionId(permissionCodeIdMap.get(item))
   						.deleteFlag(IntTypeEnum.FALSE.getValue())
   						.createTime(now)
   						.createBy(oper)
   						.updateTime(now)
   						.updateBy(oper)
   						.build();
   				rolePermissionList.add(rolePerm);
   			});
   		}
		
		return RoleInfoBo.builder()
				.role(role)
				.rolePermissionList(rolePermissionList)
				.build();
	}
	
	public UserInfoBo buildUser(String account, String name, String phone, String email, String pwd, List<String> roleIds) {
		
		LocalDateTime now = LocalDateTime.now();
		String operUser = WebHelper.getUserId();
		UserInfoPo user = UserInfoPo.builder()
				.id(UUID.randomUUID().toString())
				.account(account)
				.name(name)
				.phone(phone)
				.email(email)
				.password(passwordService.bcryptEncoder(pwd))
				.status(UserEnum.Status.AVAILABLE.getCode())
				.deleteFlag(IntTypeEnum.FALSE.getValue())
				.createTime(now)
				.createBy(operUser)
				.updateTime(now)
				.updateBy(operUser)
				.build();
		
		List<UserRolePo> userRoles = roleIds.stream().map(item -> UserRolePo.builder()
				.userId(user.getId())
				.roleId(item)
				.deleteFlag(IntTypeEnum.FALSE.getValue())
				.createTime(now)
				.createBy(operUser)
				.updateTime(now)
				.updateBy(operUser)
				.build()).collect(Collectors.toList());
		
		return UserInfoBo.builder()
				.user(user)
				.userRoles(userRoles)
				.build();
	}
	
	public List<UserRolePo> buildUserRoles(String userId, List<String> roleIds) {
		LocalDateTime now = LocalDateTime.now();
		String operUser = WebHelper.getUserId();
		
		return roleIds.stream().map(roleId -> UserRolePo.builder()
				.userId(userId)
				.roleId(roleId)
				.deleteFlag(IntTypeEnum.FALSE.getValue())
				.createTime(now)
				.createBy(operUser)
				.updateTime(now)
				.updateBy(operUser)
				.build()).collect(Collectors.toList());
	}
	
	public String buildUserOrgIdentifier(UserOrganizationPo usrOrg) {
		return new StringBuilder(usrOrg.getUserId()).append(",").append(usrOrg.getConsortiumId()).append(",")
				.append(usrOrg.getOrganizationName()).toString();
	}
	
	public UserOrganizationPo buildUserOrg(String identifier) {
		String[] params = StringUtils.split(identifier, ",");
		LocalDateTime now = LocalDateTime.now();
		return UserOrganizationPo.builder()
				.userId(params[0])
				.consortiumId(params[1])
				.organizationName(params[2])
				.deleteFlag(IntTypeEnum.FALSE.getValue())
				.createTime(now)
				.updateTime(now)
				.build();
	}
}
