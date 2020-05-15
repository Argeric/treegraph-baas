package org.treegraph.baas.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ListResponse {

	public long total;
	
	public int current;

	public int pageSize;
}
