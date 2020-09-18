package org.treegraph.baas.core.das;

import java.util.List;

import org.treegraph.baas.core.domain.MembershipPo;

public interface MembershipDas {
	List<MembershipPo> findByMembershipChangeId(long membershipChangeId);
}
