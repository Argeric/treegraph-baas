package org.treegraph.baas.core.das;

import java.util.List;
import java.util.Optional;

import org.treegraph.baas.core.domain.ConsortiumPo;
import org.treegraph.baas.core.domain.UserInvitePo;
import org.treegraph.baas.core.domain.UserRoleInvitePo;
import org.treegraph.baas.core.domain.RolePermissionPo;
import org.treegraph.baas.core.domain.RolePo;
import org.treegraph.baas.core.domain.UserInfoPo;
import org.treegraph.baas.core.domain.UserOrganizationPo;
import org.treegraph.baas.core.domain.UserRolePo;

public interface UserInfoDas {
	Optional<UserInfoPo> findById(String userId);
	
	List<UserInfoPo> findByIds(List<String> userIds);

	Optional<UserInfoPo> findByAccount(String account);
	
	List<UserInfoPo> findByAccountLike(String account);
	
	List<UserOrganizationPo> findUserOrgsByUserIds(List<String> userIds);
	
	long count();

	boolean createUser(UserInfoPo user, List<UserRolePo> userRoles, List<UserOrganizationPo> userOrgs);
	
	boolean createUserRoles(List<UserRolePo> userRoles);
	
	boolean inviteUser(UserInvitePo userInvite, List<UserRoleInvitePo> userRoleInvites);
	
	boolean updateInviteUserById(UserInvitePo userInvite);

	boolean delUser(UserInfoPo user, List<Long> userRoleIds);

	List<UserInfoPo> findAll(String account, String name, String email, String phone);
	
	boolean updateUser(UserInfoPo user);

	boolean modifyUser(String uesrId, UserInfoPo user, List<Long> toDelUserRoleIds, List<UserRolePo> toAddUserRoles,
			List<Long> toDelUserOrgIds, List<UserOrganizationPo> toAddUserOrgs);

	boolean createConsAdmin(RolePo consAdminRole, List<RolePermissionPo> rolePermissions, List<UserInfoPo> users,
			List<UserRolePo> userRoles, List<UserOrganizationPo> userOrgs, ConsortiumPo cons);
}
