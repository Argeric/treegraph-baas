package org.treegraph.baas.api.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeleteNodeRequest {
	
	private String id;

	private String ip;
	
	private String port;
	
	private String org;
}
