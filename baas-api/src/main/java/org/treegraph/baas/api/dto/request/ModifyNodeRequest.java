package org.treegraph.baas.api.dto.request;

import lombok.Data;

@Data
public class ModifyNodeRequest {

  private String id;

  private String ip;

  private String port;

  private String org;
}
