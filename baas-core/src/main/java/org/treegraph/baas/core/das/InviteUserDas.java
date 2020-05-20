package org.treegraph.baas.core.das;

import java.util.List;
import java.util.Optional;

import org.treegraph.baas.core.domain.InviteUserPo;
import org.treegraph.baas.core.domain.InviteUserRolePo;

public interface InviteUserDas {
	Optional<InviteUserPo> findById(Long inviteId);
	
	List<InviteUserRolePo> findByInviteId(Long inviteId);
}
