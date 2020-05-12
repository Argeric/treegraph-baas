package org.treegraph.baas.core.dao;

import java.util.List;

import org.treegraph.baas.core.domain.RolePo;
import org.treegraph.baas.core.domain.UserRolePo;


public interface RoleDao {


	List<UserRolePo> findUserRolesByUserId(String userId);
	
	List<RolePo> findRolesByIds(List<Long> roleIds);
}
