package org.treegraph.baas.core.das;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.treegraph.baas.core.das.bo.PermissionInfoBo;
import org.treegraph.baas.core.domain.PermissionPo;
import org.treegraph.baas.core.domain.RolePermissionPo;

public interface PermissionDas {
	Optional<PermissionPo> findById(Long permissionId);
	
	List<PermissionPo> findByIds(List<Long> permissionIds);
	
	List<PermissionPo> findAll();
	
	List<RolePermissionPo> findRolePermissionsByRoleId(String roleId);

	List<RolePermissionPo> findRolePermissionsByRoleIds(List<String> roleIds);
	
	Map<String, Long> validatePermissions(List<String> reqPermissions);
	
	List<String> findPermissionCodeByUserId(String userId);
	
	List<PermissionInfoBo> findPermissionInfoByUserId(String userId);
}
