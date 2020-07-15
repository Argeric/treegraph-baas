package org.treegraph.baas.api.rpc.dto;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Token {
	
	private String name;

	private String symbol;
	
	private BigDecimal decimals;
	
	private String totalSupply;
	
	private Long granularity;
	
	private Boolean isERC721;
}
