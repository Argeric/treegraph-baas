package org.treegraph.baas.api.dto;

import lombok.Data;

@Data
public class ListRequest {
	
	private int current;

	private int pageSize;
}
