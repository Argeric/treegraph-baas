package org.treegraph.baas.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @author zt
 * @version 1.0
 * @date May 11, 2020
 */
@Data
@AllArgsConstructor
@Builder
public class Role {

  private String consId;

  private String roleId;

  private String roleName;
}
