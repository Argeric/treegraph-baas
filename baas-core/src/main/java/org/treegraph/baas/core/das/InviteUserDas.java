package org.treegraph.baas.core.das;

import java.util.List;
import java.util.Optional;

import org.treegraph.baas.core.domain.UserInvitePo;
import org.treegraph.baas.core.domain.UserRoleInvitePo;

public interface InviteUserDas {
	Optional<UserInvitePo> findById(Long inviteId);
	
	List<UserRoleInvitePo> findByInviteId(Long inviteId);
}
