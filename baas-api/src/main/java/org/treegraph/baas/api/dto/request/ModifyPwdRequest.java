package org.treegraph.baas.api.dto.request;

import lombok.Data;

@Data
public class ModifyPwdRequest {

  private String id;

  private String oldPwd;

  private String newPwd;
}
