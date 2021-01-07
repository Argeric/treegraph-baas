package org.treegraph.baas.api.dto.request;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DeployNodeRequest {

  private String consId;

  private String blockchainId;

  private List<String> nodeId;
}




