package org.treegraph.baas.core.das;

import java.util.List;
import java.util.Optional;

import org.treegraph.baas.core.domain.MembershipChangePo;
import org.treegraph.baas.core.domain.MembershipPo;
import org.treegraph.baas.core.enums.MembershipEnum;

public interface MembershipChangeDas {

  Optional<MembershipChangePo> findById(long id);

  boolean addMembershipChange(MembershipChangePo membershipChange,
      List<MembershipPo> membershipList);

  boolean modifyMembershipChangeStatus(List<Long> ids, MembershipEnum.Status status);

  List<MembershipChangePo> getMembershipChangeByStatus(List<Integer> statusList);
}
