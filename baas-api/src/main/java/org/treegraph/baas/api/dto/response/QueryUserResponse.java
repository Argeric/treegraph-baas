package org.treegraph.baas.api.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QueryUserResponse {

	private String id;
	
	private String account;
	
	private String name;
}
