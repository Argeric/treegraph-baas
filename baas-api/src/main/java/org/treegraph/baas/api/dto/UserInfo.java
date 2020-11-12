package org.treegraph.baas.api.dto;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserInfo {

  private String id;

  private String name;

  private String account;

  private String phone;

  private String email;

  private String createTime;

  private int status;

  private List<RoleDetailInfo> roles;
}
