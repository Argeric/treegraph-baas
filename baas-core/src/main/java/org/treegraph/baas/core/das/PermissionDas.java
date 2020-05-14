package org.treegraph.baas.core.das;

import java.util.List;
import java.util.Map;

import org.treegraph.baas.core.domain.PermissionPo;
import org.treegraph.baas.core.domain.RolePermissionPo;

public interface PermissionDas {
	
	List<RolePermissionPo> findRolePermissionsByRoleId(String roldId);

	List<RolePermissionPo> findRolePermissionsByRoleIds(List<String> roldIds);
	
	List<PermissionPo> findPermissionsByIds(List<Long> permissionIds);
	
	List<PermissionPo> findAllPermissions();
	
	Map<String, Long> validatePermissions(List<String> reqPermissions);
	
	
}
