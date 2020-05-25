package org.treegraph.baas.api.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QueryDashboardResponse {

	private Long consCount;
	
	private Long blockchainCount;
	
	private Long nodeCount;
	
	private Long userCount;
}
