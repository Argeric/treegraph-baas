package org.treegraph.baas.core.service.bo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NodeKeyBo {
    private String nodeIp;
    private int nodePort;
    private boolean status;//运行状态
}
