package org.treegraph.baas.core.service.bo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GRpcParaBo {
	private String ip;
	private int gRpcPort;
    private String serverCertFile;
    private String rpcCertChainFile;
    private String rpcKeyFile;
}
