package org.treegraph.baas.core.service.bo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GRpcKeypairCertBo {

  private String consId;
  private String cert;
  private String privateKey;
}
