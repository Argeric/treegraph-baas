package org.treegraph.baas.core.service.bo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NodeBo {
    private String nodeIp;
    private int jsonRpcHttpPort;
    private boolean status;//运行状态
}
