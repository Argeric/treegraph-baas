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
public class RoleInfo {

  private String consId;

  private String orgName;

  private List<String> roleIds;
}
