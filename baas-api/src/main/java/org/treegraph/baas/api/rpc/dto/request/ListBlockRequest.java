package org.treegraph.baas.api.rpc.dto.request;

import lombok.Data;

@Data
public class ListBlockRequest {

	private Integer pageSize;

	private Integer page;

	private String epochNumber;
	
	private String refereeBy;
	
	private String miner;

	private Boolean reverse;
}
