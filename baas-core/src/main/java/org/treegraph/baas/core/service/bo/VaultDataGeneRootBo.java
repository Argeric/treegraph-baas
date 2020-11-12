package org.treegraph.baas.core.service.bo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VaultDataGeneRootBo {

  private String certificate;
  private Long expiration;
  private String issuing_ca;
  private String private_key;
  private String private_key_type;
  private String serial_number;
}
