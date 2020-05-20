package org.treegraph.baas.core.service;

import java.util.List;

import org.treegraph.baas.api.dto.RolePermissionInfo;
import org.treegraph.baas.api.dto.request.CreateRoleRequest;
import org.treegraph.baas.api.dto.request.DeleteRoleRequest;
import org.treegraph.baas.api.dto.request.ListRoleRequest;
import org.treegraph.baas.api.dto.request.ModifyRoleRequest;
import org.treegraph.baas.api.dto.request.QueryRoleRequest;
import org.treegraph.baas.api.dto.response.ListRoleResponse;

public interface RoleService {
	
	ListRoleResponse listRole(ListRoleRequest req);
	
	List<RolePermissionInfo> queryRole(QueryRoleRequest req);

	boolean createRole(CreateRoleRequest req);
	
	boolean modifyRole(ModifyRoleRequest req);
	
	boolean deleteRole(DeleteRoleRequest req);
}
