package org.treegraph.baas.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ListRequest {
	
	private int current;

	private int pageSize;
}
