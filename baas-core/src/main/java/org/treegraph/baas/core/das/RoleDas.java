package org.treegraph.baas.core.das;

import java.util.List;
import java.util.Optional;

import org.treegraph.baas.core.domain.RolePermissionPo;
import org.treegraph.baas.core.domain.RolePo;
import org.treegraph.baas.core.domain.UserRolePo;


public interface RoleDas {
	
	List<UserRolePo> findUserRolesByUserId(String userId);
	
	Optional<RolePo> findRolesById(String roleId);
	
	List<RolePo> findRolesByIds(List<String> roleIds);
	
	boolean createRole(RolePo role, List<RolePermissionPo> rolePermissionList);
	
	boolean modifyRole(String roleId, RolePo role, List<Long> toDelPermIds, List<RolePermissionPo> toAddPerms);
	
	List<RolePo> findRoles(String consId, String roleName);
	
	List<RolePo> findRolesByConsIds(List<String> consIds);
}
