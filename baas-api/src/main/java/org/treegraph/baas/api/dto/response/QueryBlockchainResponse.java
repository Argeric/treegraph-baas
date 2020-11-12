package org.treegraph.baas.api.dto.response;

import java.util.List;

import org.treegraph.baas.api.dto.BlockchainInfo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QueryBlockchainResponse {

  private List<BlockchainInfo> list;
}
