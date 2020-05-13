package org.treegraph.baas.core.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.treegraph.baas.api.dto.RoleInfo;
import org.treegraph.baas.api.dto.UserInfo;
import org.treegraph.baas.api.dto.request.CreateUserRequest;
import org.treegraph.baas.api.dto.request.DeleteUserRequest;
import org.treegraph.baas.api.dto.request.ListUserRequest;
import org.treegraph.baas.api.dto.request.ModifyPwdRequest;
import org.treegraph.baas.api.dto.request.ModifyUserRequest;
import org.treegraph.baas.api.dto.request.QueryUserRequest;
import org.treegraph.baas.api.dto.response.ListUserResponse;
import org.treegraph.baas.api.dto.response.QueryUserResponse;
import org.treegraph.baas.common.enums.IntTypeEnum;
import org.treegraph.baas.common.util.WebHelper;
import org.treegraph.baas.core.das.RoleDas;
import org.treegraph.baas.core.das.UserInfoDas;
import org.treegraph.baas.core.domain.RolePo;
import org.treegraph.baas.core.domain.UserInfoPo;
import org.treegraph.baas.core.domain.UserRolePo;
import org.treegraph.baas.core.enums.UserEnum;
import org.treegraph.baas.core.exception.AlertException;
import org.treegraph.baas.core.service.UserService;
import org.treegraph.baas.core.util.CollectionUtils;
import org.treegraph.baas.core.util.DateTimeUtils;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;

@Validated
@Service
public class UserServicempl implements UserService {

	@Autowired
    private UserInfoDas userInfoDas;
    
	@Autowired
    private RoleDas roleDas;
	
	@Autowired
    private PasswordService passwordService;
    
	public boolean createUser(CreateUserRequest req) {

		List<RoleInfo> roles = validateRoleIds(req.getRoles());
		
		LocalDateTime now = LocalDateTime.now();
		String operUser = WebHelper.getUserId();
		UserInfoPo user = UserInfoPo.builder()
				.id(UUID.randomUUID().toString())
				.account(req.getAccount())
				.name(req.getName())
				.phone(req.getPhone())
				.password(passwordService.bcryptEncoder(req.getPwd()))
				.email(null)
				.status(UserEnum.Status.AVAILABLE.getCode())
				.deleteFlag(IntTypeEnum.FALSE.getValue())
				.createTime(now)
				.createBy(operUser)
				.updateTime(now)
				.updateBy(operUser)
				.build();
		
		List<String> roleIds = Lists.newArrayList();
		roles.stream().forEach(item -> roleIds.addAll(item.getRoleIds()));
		List<UserRolePo> userRoles = roleIds.stream().map(item -> UserRolePo.builder()
				.userId(user.getId())
				.roleId(item)
				.deleteFlag(IntTypeEnum.FALSE.getValue())
				.createTime(now)
				.createBy(operUser)
				.updateTime(now)
				.updateBy(operUser)
				.build()).collect(Collectors.toList());
		
		return userInfoDas.createUser(user, userRoles);
	}
	
	public boolean deleteUser(DeleteUserRequest req){
		String userId = req.getId();
		UserInfoPo user = UserInfoPo.builder()
				.id(userId)
				.deleteFlag(IntTypeEnum.TRUE.getValue())
				.updateTime(LocalDateTime.now())
				.updateBy(WebHelper.getUserId())
				.build();
		return userInfoDas.delUser(user);
	}
	
	public QueryUserResponse queryUser(QueryUserRequest req) {
		
		String account = req.getAccount();
		UserInfoPo user = userInfoDas.findByAccountNo(account);
		
		QueryUserResponse queryUserResponse = QueryUserResponse.builder()
				.id(user.getId())
				.account(account)
				.name(user.getName())
				.build();
		
		return queryUserResponse;
	}
	
	public ListUserResponse listUser(ListUserRequest req){
		
		PageHelper.startPage(req.getCurrent(), req.getPageSize());
		List<UserInfoPo> userList = userInfoDas.findUsers(req.getAccount(), req.getName(), req.getEmail(),
				req.getPhone());
		PageInfo<UserInfoPo> pageInfo = new PageInfo<>(userList);
		
		List<UserInfoPo> list = pageInfo.getList();
		List<UserInfo> users = list.stream().map(item -> UserInfo.builder()
						.id(item.getId())
						.account(item.getAccount())
						.name(item.getName())
						.phone(item.getPhone())
						.status(item.getStatus())
						.createTime(DateTimeUtils.formatTime(item.getCreateTime(),DateTimeUtils.FORMAT_yyyyMMddHHmmss))
						.build())
				.collect(Collectors.toList());
		
		ListUserResponse listUserResponse = ListUserResponse.builder()
				.total(pageInfo.getTotal())
				.list(users)
				.build();
		
		return listUserResponse;
	}
	
	public boolean modifyPwd(ModifyPwdRequest req){
		
		String userId = req.getId();
		String oldPwd = req.getOldPwd();
		String newPwd = req.getNewPwd();
		
		UserInfoPo user = userInfoDas.findById(userId).orElseThrow(() -> new AlertException("user.not.found"));
		boolean matches = passwordService.matches(oldPwd, user.getPassword());
		Assert.isTrue(matches, "旧密码不正确");
		
		UserInfoPo updateUser = UserInfoPo.builder()
				.id(userId)
				.password(passwordService.bcryptEncoder(newPwd))
				.updateTime(LocalDateTime.now())
				.updateBy(WebHelper.getUserId())
				.build();
		return userInfoDas.updateUser(updateUser);
	}
	
	public boolean modifyUser(ModifyUserRequest req){
		
		UserInfoPo updateUser = UserInfoPo.builder()
				.id(req.getId())
				.password(req.getPwd() != null ? passwordService.bcryptEncoder(req.getPwd()) : null)
				.account(req.getAccount())
				.name(req.getName())
				.phone(req.getPhone())
				.updateTime(LocalDateTime.now())
				.updateBy(WebHelper.getUserId())
				.build();
		
		List<RoleInfo> roles = validateRoleIds(req.getRoles());
		List<String> reqRoleIds = Lists.newArrayList();
		roles.stream().forEach(item -> reqRoleIds.addAll(item.getRoleIds()));
		List<UserRolePo> dbUserRoles = roleDas.findUserRolesByUserId(req.getId());
		List<String> dbRoleIds = dbUserRoles.stream().map(UserRolePo::getRoleId).collect(Collectors.toList());
		List<String> interset = Lists.newArrayList();
		interset.addAll(reqRoleIds);
		interset.retainAll(dbRoleIds);
		reqRoleIds.removeAll(interset);
		dbRoleIds.removeAll(interset);
		
		
		LocalDateTime now = LocalDateTime.now();
		String operUser = WebHelper.getUserId();
		List<UserRolePo> toAddUserRoles = reqRoleIds.stream().map(item -> UserRolePo.builder()
				.userId(req.getId())
				.roleId(item)
				.deleteFlag(IntTypeEnum.FALSE.getValue())
				.createTime(now)
				.createBy(operUser)
				.updateTime(now)
				.updateBy(operUser)
				.build()).collect(Collectors.toList());
		
		return userInfoDas.modifyUser(req.getId(), updateUser, dbRoleIds, toAddUserRoles);
	}

	private List<RoleInfo> validateRoleIds(List<RoleInfo> roles) {
		
		if(CollectionUtils.isEmpty(roles)) {
			return Lists.newArrayList();
		}
		
		List<String> consIds = roles.stream().map(RoleInfo::getConsId).collect(Collectors.toList());
		List<RolePo> dbRoles = roleDas.findRolesByConsIds(consIds);
		Map<String, List<String>> dbConsIdRoldIdMap = dbRoles.stream().collect(
				Collectors.groupingBy(RolePo::getConsortiumId, Collectors.mapping(RolePo::getId, Collectors.toList())));
		
		roles.stream().forEach(item -> {
			List<String> dbRoleIdList = dbConsIdRoldIdMap.get(item.getConsId());
			Assert.notEmpty(dbRoleIdList, "给用户所授角色,联盟无此角色[" + item.getConsId() + "]");
			
			List<String> reqRoleIdList = item.getRoleIds();
			reqRoleIdList.stream()
					.forEach(roleId -> Assert.isTrue(dbRoleIdList.contains(roleId), "给用户所授角色无效[" + roleId + "]"));
		});
		
		return roles;
	}
}
