package org.treegraph.baas.api.dto;

import java.util.List;

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
public class RoleDetailInfo {

  private String consId;

  private String consName;

  private String orgName;

  private List<Role> roles;
}
