package org.treegraph.baas.core.das;

import java.util.List;
import java.util.Optional;

import org.treegraph.baas.core.domain.RolePermissionPo;
import org.treegraph.baas.core.domain.RolePo;
import org.treegraph.baas.core.domain.UserRolePo;


public interface RoleDas {
	
	List<UserRolePo> findUserRolesByUserId(String userId);
	
	List<UserRolePo> findUserRolesByUserIds(List<String> userIds);
	
	Optional<RolePo> findById(String roleId);
	
	List<RolePo> findByIds(List<String> roleIds);
	
	boolean createRole(RolePo role, List<RolePermissionPo> rolePermissionList);
	
	boolean modifyRole(String roleId, RolePo role, List<Long> toDelPermIds, List<RolePermissionPo> toAddPerms);
	
	List<RolePo> findAll(String consId, String roleName);
	
	List<RolePo> findRolesByConsIds(List<String> consIds);
	
	Optional<RolePo> findConsAdminRole(String consId);
}
