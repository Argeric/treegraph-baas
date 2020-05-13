package org.treegraph.baas.api.dto.request;

import lombok.Data;

@Data
public class ListConsRequest {
	
	private int current;

	private int pageSize;
}
