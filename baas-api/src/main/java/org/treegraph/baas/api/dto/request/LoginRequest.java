package org.treegraph.baas.api.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginRequest {

  private String account;

  private String password;
}
