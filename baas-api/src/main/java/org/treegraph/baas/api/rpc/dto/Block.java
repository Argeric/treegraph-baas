package org.treegraph.baas.api.rpc.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Block {
	
	private String epochNumber;

	private String hash;
	
	private String miner;
	
	private String gasLimit;
	
	private String difficulty;
	
	private String timestamp;
	
	private Long transactionCount;
	
	private Long newTransactionCount;
	
	private Long blockIndex;
	
	private String pivotHash;
	
	private String syncTimestamp;
}
