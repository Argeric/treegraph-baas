package org.treegraph.baas.api.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
