package org.treegraph.baas.api.dto.request;

import java.util.List;

import org.treegraph.baas.api.dto.NodeInfo;

import lombok.Data;

@Data
public class AddNodesRequest {
	
	private String consId;
	
	private String blockchainId;
	
	private List<NodeInfo> nodes;
}
