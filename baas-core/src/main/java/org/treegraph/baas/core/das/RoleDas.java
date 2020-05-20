package org.treegraph.baas.core.das;

import java.util.List;
import java.util.Optional;

import org.treegraph.baas.core.domain.RolePermissionPo;
import org.treegraph.baas.core.domain.RolePo;
import org.treegraph.baas.core.domain.UserRolePo;


public interface RoleDas {
	
	Optional<RolePo> findById(String roleId);
	
	List<RolePo> findByIds(List<String> roleIds);
	
	List<RolePo> findAll(String id, String consId, String roleName);
	
	List<RolePo> findByConsIds(List<String> consIds);
	
	Optional<RolePo> findConsAdminRole(String consId);
	
	List<UserRolePo> findUserRolesByUserId(String userId);
	
	List<UserRolePo> findUserRolesByUserIds(List<String> userIds);
	
	List<UserRolePo> findUserRolesForConsAdmin(List<String> userIds, String roleId);
	
	List<UserRolePo> findUserRolesByRoleId(String roleId);
	
	boolean createRole(RolePo role, List<RolePermissionPo> rolePermissionList);
	
	boolean modifyRole(String roleId, RolePo role, List<Long> toDelPermIds, List<RolePermissionPo> toAddPerms);

	boolean deleteRole(String roleId);
}
