package org.treegraph.baas.api.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReachableInfo {
    private String consId;
    private String blockId;
    private String nodeId;
    private Integer reachableCode;
}
