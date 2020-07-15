package org.treegraph.baas.api.rpc.dto.response;

import org.treegraph.baas.api.rpc.dto.Account;
import org.treegraph.baas.api.rpc.dto.Contract;
import org.treegraph.baas.api.rpc.dto.Token;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QueryAddressResponse {

	private String balance;
	
	private Account account;
	
	private Contract contract;
	
	private Token token;
}
