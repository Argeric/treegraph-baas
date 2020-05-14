package org.treegraph.baas.core.service;

import org.treegraph.baas.api.dto.request.CreateRoleRequest;
import org.treegraph.baas.api.dto.request.ListRoleRequest;
import org.treegraph.baas.api.dto.request.ModifyRoleRequest;
import org.treegraph.baas.api.dto.response.ListRoleResponse;

public interface RoleService {
	
	ListRoleResponse listRole(ListRoleRequest req);

	boolean createRole(CreateRoleRequest req);
	
	boolean modifyRole(ModifyRoleRequest req);
}
