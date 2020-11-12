package org.treegraph.baas.core.service.bo;

import java.util.List;

import org.treegraph.baas.core.das.bo.NodeKeypairBo;
import org.treegraph.baas.core.domain.MembershipChangePo;
import org.treegraph.baas.core.domain.MembershipPo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MembershipChangeBo {

  private List<NodeKeypairBo> existDbNodeKeypairs;

  private List<NodeKeypairBo> toAddNodeKeypairs;

  private List<NodeKeypairBo> toInsertDbNodeKeypairs;

  private List<NodeKeypairBo> toRemoveNodeKeypairs;

  private List<NodeKeypairBo> membershipNodeKeypairs;

  private List<NodeKeypairBo> committeeNodeKeypairs;

  private List<NodeKeypairBo> validatorNodeKeypairs;

  private MembershipChangePo membershipChange;

  private List<MembershipPo> nodeMemberships;
}
