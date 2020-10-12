package org.treegraph.baas.web.rest;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.treegraph.baas.api.dto.AuthInfo;
import org.treegraph.baas.api.dto.BaasResult;
import org.treegraph.baas.api.dto.RoleInfo;
import org.treegraph.baas.api.dto.UserInfo;
import org.treegraph.baas.api.dto.request.AcceptInviteRequest;
import org.treegraph.baas.api.dto.request.CreateUserRequest;
import org.treegraph.baas.api.dto.request.DeleteUserRequest;
import org.treegraph.baas.api.dto.request.InviteUserRequest;
import org.treegraph.baas.api.dto.request.ListUserRequest;
import org.treegraph.baas.api.dto.request.ModifyPwdRequest;
import org.treegraph.baas.api.dto.request.ModifyUserRequest;
import org.treegraph.baas.api.dto.request.QueryUserRequest;
import org.treegraph.baas.api.dto.request.UnfrozenUserRequest;
import org.treegraph.baas.api.dto.response.ListUserResponse;
import org.treegraph.baas.core.service.LoginService;
import org.treegraph.baas.core.service.UserService;
import org.treegraph.baas.core.util.SysManager;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;

/**
 * @author zt
 * @date May 11, 2020
 * @version 1.0
 */
@RestController
@Api(tags = "用户模块", description = "用户管理")
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {

	private UserService userService;
	
	private LoginService loginService;

	@PostMapping("/list")
	@ApiOperation("用户列表")
	public BaasResult<ListUserResponse> listUser(@RequestBody ListUserRequest listUserRequest) {
		ListUserResponse data = userService.listUser(listUserRequest);
		return BaasResult.success(data);
	}
	
	@PostMapping("/query")
	@ApiOperation("用户查询")
	public BaasResult<List<UserInfo>> queryUser(@RequestBody QueryUserRequest queryUserRequest) {
		List<UserInfo> data = userService.queryUser(queryUserRequest);
		return BaasResult.success(data);
	}

	@PostMapping("/create")
	@ApiOperation("创建用户")
	public BaasResult<Void> createUser(@RequestBody CreateUserRequest createUserRequest) {
		checkCreatUser(createUserRequest.getRoles());
		userService.createUser(createUserRequest);
		return BaasResult.success();
	}
	
	@PostMapping("/modify")
	@ApiOperation("修改用户")
	public BaasResult<Void> modifyUser(@RequestBody ModifyUserRequest modifyUserRequest) {
		userService.modifyUser(modifyUserRequest);
		return BaasResult.success();
	}
	
	@PostMapping("/invite")
	@ApiOperation("邀请用户")
	public BaasResult<String> inviteUser(@RequestBody InviteUserRequest inviteUserRequest) {
		String inviteToken = userService.inviteUser(inviteUserRequest);
		return BaasResult.success(inviteToken);
	}
	
	@PostMapping("/invite/accept")
	@ApiOperation("邀请用户")
	public BaasResult<Void> acceptInvite(@RequestBody AcceptInviteRequest acceptInviteRequest) {
		userService.acceptInvite(acceptInviteRequest);
		return BaasResult.success();
	}
	
	@PostMapping("/pwd/modify")
	@ApiOperation("修改密码")
	public BaasResult<Void> modifyPwd(@RequestBody ModifyPwdRequest modifyPwdRequest) {
		userService.modifyPwd(modifyPwdRequest);
		return BaasResult.success();
	}
	
	@PostMapping("/del")
	@ApiOperation("删除用户")
	public BaasResult<Boolean> deleteUser(@RequestBody DeleteUserRequest deleteUserRequest) {
		checkDelUser(deleteUserRequest);
		boolean succFlag = userService.deleteUser(deleteUserRequest);
		return BaasResult.success(succFlag);
	}
	
	@PostMapping("/unfrozen")
	@ApiOperation("解冻用户")
	public BaasResult<Void> unfrozenUser(@RequestBody UnfrozenUserRequest unfrozenUserRequest) {
		userService.unfrozenUser(unfrozenUserRequest);
		return BaasResult.success();
	}
	
	private void checkCreatUser(List<RoleInfo> roles) {
		if(CollectionUtils.isNotEmpty(roles)) {
			List<String> consIds = roles.stream().map(RoleInfo::getConsId).collect(Collectors.toList());
			SysManager.checkOwnCons(consIds);
		}
	}

	private void checkDelUser(DeleteUserRequest deleteUserRequest) {
		List<AuthInfo> authInfos = loginService.getAuthInfos(deleteUserRequest.getId());
		if(CollectionUtils.isNotEmpty(authInfos)) {
			List<String> consIds = authInfos.stream().map(AuthInfo::getConsId).collect(Collectors.toList());
			SysManager.checkOwnCons(consIds);
		}
	}
}
