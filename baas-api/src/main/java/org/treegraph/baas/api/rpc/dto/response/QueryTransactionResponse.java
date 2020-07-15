package org.treegraph.baas.api.rpc.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QueryTransactionResponse {

	private String blockHash;
	
	private Long transactionIndex;
	
	private String nonce;
	
	private String hash;
	
	private String from;
	
	private String to;
	
	private String value;
	
	private String data;
	
	private String gasPrice;
	
	private String gas;
	
	private Long contractCreated;
	
	private Long status;
	
	private String r;
	
	private String s;
	
	private Long v;
	
	private Long timestamp;
	
	private String storageLimit;
	
	private String chainId;
	
	private String epochHeight;
}
