package org.treegraph.baas.api.dto.request;

import lombok.Data;

@Data
public class ListNodeRequest {
	
	private int current;

	private int pageSize;

	private String consId;
	
	private String ip;
}
