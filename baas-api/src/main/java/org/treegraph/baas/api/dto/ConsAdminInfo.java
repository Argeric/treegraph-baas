package org.treegraph.baas.api.dto;

import lombok.Builder;
import lombok.Data;

/**
 * @author zt
 * @version 1.0
 * @date May 11, 2020
 */
@Data
@Builder
public class ConsAdminInfo {

  private String userId;

  private String account;

  private String name;

  private String email;

  private String phone;

  private String pwd;

  private String org;
}
