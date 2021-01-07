package org.treegraph.baas.core.service.bo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VaultSignIntermediateBo {

  private String certificate;
  private String issuingCa;
  private String serialNumber;
}
