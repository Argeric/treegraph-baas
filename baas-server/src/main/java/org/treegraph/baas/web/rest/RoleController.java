package org.treegraph.baas.web.rest;

import java.util.List;

import org.assertj.core.util.Lists;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.treegraph.baas.api.dto.BaasResult;
import org.treegraph.baas.api.dto.RolePermissionInfo;
import org.treegraph.baas.api.dto.request.CreateRoleRequest;
import org.treegraph.baas.api.dto.request.DeleteRoleRequest;
import org.treegraph.baas.api.dto.request.ListRoleRequest;
import org.treegraph.baas.api.dto.request.ModifyRoleRequest;
import org.treegraph.baas.api.dto.request.QueryRoleRequest;
import org.treegraph.baas.api.dto.response.ListRoleResponse;
import org.treegraph.baas.core.service.RoleService;
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
@Api(tags = "角色模块")
@RequestMapping("/role")
@AllArgsConstructor
public class RoleController {
	
	private final RoleService roleService;
	
	@PostMapping("/list")
	@ApiOperation("角色权限列表")
	public BaasResult<ListRoleResponse> listRole(@RequestBody ListRoleRequest listRoleRequest) {
		ListRoleResponse roles = roleService.listRole(listRoleRequest);
		return BaasResult.success(roles);
	}
	
	@PostMapping("/query")
	@ApiOperation("角色查询")
	public BaasResult<List<RolePermissionInfo>> queryRole(@RequestBody QueryRoleRequest queryRoleRequest) {
		List<RolePermissionInfo> roleList = roleService.queryRole(queryRoleRequest);
		return BaasResult.success(roleList);
	}

	@PostMapping("/create")
	@ApiOperation("创建角色")
	public BaasResult<Void> createRole(@RequestBody CreateRoleRequest createRoleRequest) {
		checkCreateRole(createRoleRequest);
	    roleService.createRole(createRoleRequest);
		return BaasResult.success();
	}
	
	@PostMapping("/modify")
	@ApiOperation("修改角色")
	public BaasResult<Void> modifyRole(@RequestBody ModifyRoleRequest modifyRoleRequest) {
		roleService.modifyRole(modifyRoleRequest);
		return BaasResult.success();
	}
	
	@PostMapping("/del")
	@ApiOperation("删除角色")
	public BaasResult<Void> deleteRole(@RequestBody DeleteRoleRequest deleteRoleRequest) {
		roleService.deleteRole(deleteRoleRequest);
		return BaasResult.success();
	}

	private void checkCreateRole(CreateRoleRequest createRoleRequest) {
		SysManager.checkOwnCons(Lists.newArrayList(createRoleRequest.getConsId()));
	}
}
