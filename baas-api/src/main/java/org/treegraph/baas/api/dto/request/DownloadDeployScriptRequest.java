package org.treegraph.baas.api.dto.request;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DownloadDeployScriptRequest {

  private String consId;

  private List<String> nodeId;
}




