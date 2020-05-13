package org.treegraph.baas.web.rest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.treegraph.baas.api.dto.BaasResult;
import org.treegraph.baas.api.dto.request.CreateUserRequest;
import org.treegraph.baas.api.dto.request.DeleteUserRequest;
import org.treegraph.baas.api.dto.request.ListUserRequest;
import org.treegraph.baas.api.dto.request.ModifyPwdRequest;
import org.treegraph.baas.api.dto.request.ModifyUserRequest;
import org.treegraph.baas.api.dto.request.QueryUserRequest;
import org.treegraph.baas.api.dto.response.ListUserResponse;
import org.treegraph.baas.api.dto.response.QueryUserResponse;
import org.treegraph.baas.core.service.UserService;

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

	@PostMapping("/list")
	@ApiOperation("用户列表")
	public BaasResult<ListUserResponse> listUser(@RequestBody ListUserRequest listUserRequest) {
		ListUserResponse data = userService.listUser(listUserRequest);
		return BaasResult.success(data);
	}
	
	@PostMapping("/query")
	@ApiOperation("查询列表")
	public BaasResult<QueryUserResponse> queryUser(@RequestBody QueryUserRequest queryUserRequest) {
		QueryUserResponse data = userService.queryUser(queryUserRequest);
		return BaasResult.success(data);
	}

	@PostMapping("/create")
	@ApiOperation("创建用户")
	public BaasResult<Void> createUser(@RequestBody CreateUserRequest createUserRequest) {
		userService.createUser(createUserRequest);
		return BaasResult.success();
	}
	
	@PostMapping("/modify")
	@ApiOperation("修改用户")
	public BaasResult<Void> modifyUser(@RequestBody ModifyUserRequest modifyUserRequest) {
		userService.modifyUser(modifyUserRequest);
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
	public BaasResult<Void> deleteUser(@RequestBody DeleteUserRequest deleteUserRequest) {
		userService.deleteUser(deleteUserRequest);
		return BaasResult.success();
	}
}
