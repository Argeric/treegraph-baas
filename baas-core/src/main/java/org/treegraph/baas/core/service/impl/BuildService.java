package org.treegraph.baas.core.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.treegraph.baas.api.dto.Role;
import org.treegraph.baas.common.enums.IntTypeEnum;
import org.treegraph.baas.common.util.WebHelper;
import org.treegraph.baas.core.das.PermissionDas;
import org.treegraph.baas.core.domain.ConsortiumPo;
import org.treegraph.baas.core.domain.RolePermissionPo;
import org.treegraph.baas.core.domain.RolePo;
import org.treegraph.baas.core.domain.UserInfoPo;
import org.treegraph.baas.core.domain.UserOrganizationPo;
import org.treegraph.baas.core.domain.UserRolePo;
import org.treegraph.baas.core.enums.ConsortiumEnum.EstablishStep;
import org.treegraph.baas.core.enums.UserEnum;
import org.treegraph.baas.core.service.bo.OrgBo;
import org.treegraph.baas.core.service.bo.RoleInfoBo;
import org.treegraph.baas.core.service.bo.UserInfoBo;
import org.treegraph.baas.core.service.bo.UserOrgBo;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Functions;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Validated
@Service
@AllArgsConstructor
@Slf4j
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
   						.consortiumId(consId)
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
	
	public UserInfoBo buildUser(String account, String name, String phone, String email, String pwd, List<Role> roles, List<OrgBo> orgs) {
		
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
		
		List<UserRolePo> userRoles = roles.stream().map(item -> UserRolePo.builder()
				.userId(user.getId())
				.roleId(item.getRoleId())
				.consortiumId(item.getConsId())
				.deleteFlag(IntTypeEnum.FALSE.getValue())
				.createTime(now)
				.createBy(operUser)
				.updateTime(now)
				.updateBy(operUser)
				.build()).collect(Collectors.toList());
		
		List<UserOrganizationPo> userOrgs = orgs.stream().map(item -> UserOrganizationPo.builder()
				.userId(user.getId())
				.consortiumId(item.getConsortiumId())
				.organizationName(item.getOrganizationName())
				.deleteFlag(IntTypeEnum.FALSE.getValue())
				.createTime(now)
				.updateTime(now)
				.build()).collect(Collectors.toList());
		
		return UserInfoBo.builder()
				.user(user)
				.userRoles(userRoles)
				.userOrgs(userOrgs)
				.build();
	}
	
	public List<UserRolePo> buildUserRoles(String userId, List<Role> roles) {
		LocalDateTime now = LocalDateTime.now();
		String operUser = WebHelper.getUserId();
		
		return roles.stream().map(item -> UserRolePo.builder()
				.userId(userId)
				.roleId(item.getRoleId())
				.consortiumId(item.getConsId())
				.deleteFlag(IntTypeEnum.FALSE.getValue())
				.createTime(now)
				.createBy(operUser)
				.updateTime(now)
				.updateBy(operUser)
				.build()).collect(Collectors.toList());
	}
	
	public UserOrgBo buildModifyUserOrg(List<UserOrganizationPo> dbUserOrgs, List<UserOrganizationPo> reqUserOrgs) {
		Set<String> dbOrgs = dbUserOrgs.stream().map(item -> this.buildUserOrgIdentifier(item))
				.collect(Collectors.toSet());
		Set<String> reqOrgs = reqUserOrgs.stream().map(item -> this.buildUserOrgIdentifier(item))
				.collect(Collectors.toSet());
		
		Set<String> intersetOrgs = Sets.newHashSet();
		intersetOrgs.addAll(dbOrgs);
		intersetOrgs.retainAll(reqOrgs);
		reqOrgs.removeAll(intersetOrgs);log.info("reqOrgs======>"+JSON.toJSONString(reqOrgs));
		dbOrgs.removeAll(intersetOrgs);log.info("dbOrgs======>"+JSON.toJSONString(dbOrgs));
		
		List<UserOrganizationPo> toAddUserOrgs = reqOrgs.stream().map(item -> this.buildUserOrg(item))
				.collect(Collectors.toList());
		Map<String, UserOrganizationPo> dbOrgMap = dbUserOrgs.stream()
				.collect(Collectors.toMap(item -> this.buildUserOrgIdentifier(item), Functions.identity()));
		List<Long> toDelUserOrgIds = dbOrgs.stream().map(item -> dbOrgMap.get(item).getId())
				.collect(Collectors.toList());

		return UserOrgBo.builder()
				.toAddUserOrgs(toAddUserOrgs)
				.toDelUserOrgIds(toDelUserOrgIds)
				.build();
	}
	
	private String buildUserOrgIdentifier(UserOrganizationPo usrOrg) {
		return new StringBuilder(usrOrg.getUserId()).append(",").append(usrOrg.getConsortiumId()).append(",")
				.append(usrOrg.getOrganizationName()).toString();
	}
	
	private UserOrganizationPo buildUserOrg(String identifier) {
		String[] params = identifier.split(",");
		System.out.println(JSON.toJSONString(params));
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

	public ConsortiumPo buildToUpdateCons(String consId, EstablishStep step) {
		return ConsortiumPo.builder()
				.consortiumId(consId)
				.establishStep(step.getCode())
				.updateTime(LocalDateTime.now())
				.updateBy(WebHelper.getUserId())
				.build();
	}
}
