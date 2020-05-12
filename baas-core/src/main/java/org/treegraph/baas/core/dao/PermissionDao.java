package org.treegraph.baas.core.dao;

import java.util.List;

import org.treegraph.baas.core.domain.PermissionPo;
import org.treegraph.baas.core.domain.RolePermissionPo;

public interface PermissionDao {

	List<RolePermissionPo> findRolePermissionsByRoleIds(List<Long> roldIds);
	
	List<PermissionPo> findPermissionsByIds(List<Long> permissionIds);
}
