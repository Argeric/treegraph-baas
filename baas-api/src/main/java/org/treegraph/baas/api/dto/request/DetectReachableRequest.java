package org.treegraph.baas.api.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DetectReachableRequest {

  private String consId;

  private String blockchainId;
}




