package org.treegraph.baas.api.rpc.dto.response;

import java.util.List;

import org.treegraph.baas.api.rpc.dto.Transaction;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ListTransactionResponse {

	private Long total;
	
	private List<Transaction> list;
	
	private Long serverTimestamp;
	
	private Long minTime;
	
	private Long maxTime;
}
