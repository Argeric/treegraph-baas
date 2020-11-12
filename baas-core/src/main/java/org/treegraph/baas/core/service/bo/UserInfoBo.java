package org.treegraph.baas.core.service.bo;

import java.io.Serializable;
import java.util.List;

import org.treegraph.baas.core.domain.UserInfoPo;
import org.treegraph.baas.core.domain.UserOrganizationPo;
import org.treegraph.baas.core.domain.UserRolePo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoBo implements Serializable {

  private UserInfoPo user;

  private List<UserRolePo> userRoles;

  private List<UserOrganizationPo> userOrgs;

  private static final long serialVersionUID = 1L;
}