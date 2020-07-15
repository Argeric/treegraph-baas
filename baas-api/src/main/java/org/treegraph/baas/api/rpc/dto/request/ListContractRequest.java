package org.treegraph.baas.api.rpc.dto.request;

import lombok.Data;

@Data
public class ListContractRequest {

	private Integer pageSize;

	private Integer page;

	private String from;
	
	private Boolean reverse;
}
