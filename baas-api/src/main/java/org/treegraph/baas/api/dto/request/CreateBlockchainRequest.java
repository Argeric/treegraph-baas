package org.treegraph.baas.api.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateBlockchainRequest {

  private String name;

  private String consId;

  private String alertEmail;

  private String consensus;

  private String algorithm;
}
