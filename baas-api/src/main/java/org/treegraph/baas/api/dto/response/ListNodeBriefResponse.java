package org.treegraph.baas.api.dto.response;

import lombok.Builder;
import lombok.Data;
import org.treegraph.baas.api.dto.NodeInfo;

import java.util.List;

@Data
@Builder
public class ListNodeBriefResponse {

  private List<NodeInfo> list;
}
