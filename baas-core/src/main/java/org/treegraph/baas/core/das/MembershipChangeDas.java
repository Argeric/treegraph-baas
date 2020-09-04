package org.treegraph.baas.core.das;

import java.util.List;
import java.util.Optional;

import org.treegraph.baas.core.domain.MembershipChangePo;
import org.treegraph.baas.core.domain.MembershipPo;

public interface MembershipChangeDas {
	Optional<MembershipChangePo> findById(long id);
	boolean addMembershipChange(MembershipChangePo membershipChange, List<MembershipPo> membershipList);
}
