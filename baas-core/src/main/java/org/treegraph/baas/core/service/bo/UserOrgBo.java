package org.treegraph.baas.core.service.bo;

import java.io.Serializable;
import java.util.List;

import org.treegraph.baas.core.domain.UserOrganizationPo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserOrgBo implements Serializable {

  private List<UserOrganizationPo> toAddUserOrgs;

  private List<Long> toDelUserOrgIds;

  private static final long serialVersionUID = 1L;
}