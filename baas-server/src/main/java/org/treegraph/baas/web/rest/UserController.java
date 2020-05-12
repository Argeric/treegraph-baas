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
import org.treegraph.baas.core.util.LogSkip;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zt
 * @date May 11, 2020
 * @version 1.0
 */
@Slf4j
@RestController
@Api(tags = "UserController", description = "用户管理")
@RequestMapping("/user")
public class UserController {

	 private UserService userService;

	@PostMapping("/list")
	@ApiOperation("用户列表")
	@LogSkip
	public BaasResult<ListUserResponse> listUser(@RequestBody ListUserRequest listUserRequest) {

		return null;
	}
	
	@PostMapping("/query")
	@ApiOperation("查询列表")
	@LogSkip
	public BaasResult<QueryUserResponse> queryUser(@RequestBody QueryUserRequest queryUserRequest) {

		return null;
	}

	@PostMapping("/create")
	@ApiOperation("创建用户")
	@LogSkip
	public BaasResult<Void> createUser(@RequestBody CreateUserRequest createUserRequest) {
		userService.createUser(createUserRequest);
		return BaasResult.success();
	}
	
	@PostMapping("/modify")
	@ApiOperation("修改用户")
	@LogSkip
	public BaasResult<Void> modifyUser(@RequestBody ModifyUserRequest modifyUserRequest) {

		return null;
	}
	
	@PostMapping("/pwd/modify")
	@ApiOperation("修改密码")
	@LogSkip
	public BaasResult<Void> modifyPwd(@RequestBody ModifyPwdRequest modifyPwdRequest) {

		return null;
	}
	
	@PostMapping("/del")
	@ApiOperation("删除用户")
	@LogSkip
	public BaasResult<Void> deleteUser(@RequestBody DeleteUserRequest deleteUserRequest) {

		return null;
	}
}
