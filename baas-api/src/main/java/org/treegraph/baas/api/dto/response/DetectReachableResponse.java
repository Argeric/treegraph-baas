package org.treegraph.baas.api.dto.response;

import lombok.Builder;
import lombok.Data;
import org.treegraph.baas.api.dto.BlockchainInfo;
import org.treegraph.baas.api.dto.ReachableInfo;

import java.util.List;

@Data
@Builder
public class DetectReachableResponse {

  private List<ReachableInfo> list;
}
