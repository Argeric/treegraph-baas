package org.treegraph.baas.api.dto.response;

import java.io.Serializable;
import java.util.List;

import org.treegraph.baas.api.dto.AuthInfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class LoginResponse implements Serializable {

  private static final long serialVersionUID = 3318359473138119777L;

  private String userId;

  private String account;

  private String name;

  private String email;

  private String phone;

  private String token;

  private boolean isFirstLogin;

  private List<AuthInfo> auth;
}
