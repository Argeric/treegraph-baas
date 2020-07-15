package org.treegraph.baas.api.rpc.dto.request;

import lombok.Data;

@Data
public class ListTransactionRequest {

	private Integer pageSize;

	private Integer page;

	private String blockHash;
	
	private String accountAddress;
	
	private String txType;

	private String startTime;
	
	private String endTime;
	
	private Boolean reverse;
}
