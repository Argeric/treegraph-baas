package org.treegraph.baas.api.dto;

import java.util.List;

import lombok.Builder;
import lombok.Data;

/**
 * @author zt
 * @version 1.0
 * @date May 11, 2020
 */
@Data
@Builder
public class RoleDetailInfo {

  private String consId;

  private String consName;

  private String orgName;

  private List<Role> roles;
}
