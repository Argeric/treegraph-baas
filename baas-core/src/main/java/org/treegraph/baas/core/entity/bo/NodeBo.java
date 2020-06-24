package org.treegraph.baas.core.entity.bo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NodeBo {
    private String nodeIp;
    private int nodePort;
    private boolean runningStatus;
}
