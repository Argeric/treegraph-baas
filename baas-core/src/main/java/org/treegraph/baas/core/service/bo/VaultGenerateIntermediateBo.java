package org.treegraph.baas.core.service.bo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VaultGenerateIntermediateBo {

  private String csr;
  private Long expiration;
  private String privateKey;
  private String privateKeyType;
  private String serialNumber;
}
