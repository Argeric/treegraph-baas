package org.treegraph.baas.core.das;

import java.util.List;
import java.util.Optional;

import org.treegraph.baas.core.domain.RolePermissionPo;
import org.treegraph.baas.core.domain.RolePo;
import org.treegraph.baas.core.domain.UserInfoPo;
import org.treegraph.baas.core.domain.UserRolePo;

public interface UserInfoDas {

	Optional<UserInfoPo> findById(String userId);
	
	List<UserInfoPo> findByIds(List<String> userIds);

	Optional<UserInfoPo> findByAccountNo(String accountNo);
	
	List<UserInfoPo> findByAccountNoInLike(String accountNo);

	boolean createUser(UserInfoPo user, List<UserRolePo> userRoles);

	boolean delUser(UserInfoPo user);

	List<UserInfoPo> findAll(String account, String name, String email, String phone);

	boolean updateUser(UserInfoPo user);

	boolean modifyUser(String uesrId, UserInfoPo user, List<String> toDelRoleIds, List<UserRolePo> toAddUserRoles);

	boolean createConsAdmin(RolePo consAdminRole, List<RolePermissionPo> rolePermissions, List<UserInfoPo> users,
			List<UserRolePo> userRoles);
}
