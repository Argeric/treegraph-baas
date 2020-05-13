package org.treegraph.baas.web.rest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.treegraph.baas.api.dto.BaasResult;
import org.treegraph.baas.api.dto.request.CreateRoleRequest;
import org.treegraph.baas.api.dto.request.ListRoleRequest;
import org.treegraph.baas.api.dto.request.ModifyRoleRequest;
import org.treegraph.baas.api.dto.response.ListRoleResponse;
import org.treegraph.baas.core.service.RoleService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author zt
 * @date May 11, 2020
 * @version 1.0
 */
@RestController
@Api(tags = "角色模块", description = "权限角色管理")
@RequestMapping("/role")
public class RoleController {
	
	private RoleService roleService;
	
	@PostMapping("/list")
	@ApiOperation("角色权限列表")
	public BaasResult<ListRoleResponse> listRole(@RequestBody ListRoleRequest listRoleRequest) {
		ListRoleResponse roles = roleService.listRole(listRoleRequest);
		return BaasResult.success(roles);
	}

	@PostMapping("/create")
	@ApiOperation("创建角色")
	public BaasResult<Void> createRole(@RequestBody CreateRoleRequest createRoleRequest) {
	    roleService.createRole(createRoleRequest);
		return BaasResult.success();
	}
	
	@PostMapping("/modify")
	@ApiOperation("修改角色")
	public BaasResult<Void> modifyRole(@RequestBody ModifyRoleRequest modifyRoleRequest) {
		roleService.modifyRole(modifyRoleRequest);
		return BaasResult.success();
	}
}
