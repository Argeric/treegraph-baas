package org.treegraph.baas.core.das.bo;

import java.util.Set;

import lombok.Builder;
import lombok.Data;

/**
 * @author zt
 * @version 1.0
 * @date May 27, 2020
 */
@Data
@Builder
public class RolePermissionInfoBo {

  private String roleId;

  private Integer roleType;

  private String roleName;

  private String consId;

  private Set<String> permissions;
}
