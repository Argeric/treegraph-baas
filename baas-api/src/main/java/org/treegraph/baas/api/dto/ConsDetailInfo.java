package org.treegraph.baas.api.dto;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConsDetailInfo {
	
	private String id;

	private String name;
	
	private String blockchainId;
	
	private String blockchainName;
	
	private List<NodeInfo> nodes;
}
