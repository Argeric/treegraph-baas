package org.treegraph.baas.api.rpc.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QueryBlockResponse {

	private Long epochNumber;
	
	private String hash;
	
	private String parentHash;
	
	private String[] refereeHashes;
	
	private String nonce;
	
	private String miner;
	
	private String gasLimit;
	
	private String difficulty;
	
	private Long height;
	
	private Long size;
	
	private Long blame;
	
	private Boolean adaptive;
	
	private Boolean stable;
	
	private Long timestamp;
	
	private String deferredLogsBloomHash;
	
	private String deferredReceiptsRoot;
	
	private String deferredStateRoot;
	
	private String transactionsRoot;
	
	private Long transactionCount;
	
	private Long blockIndex;
	
	private String pivotHash;
}
