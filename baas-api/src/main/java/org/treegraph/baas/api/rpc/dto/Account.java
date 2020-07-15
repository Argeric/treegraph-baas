package org.treegraph.baas.api.rpc.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Account {
	
	private Long all;

	private String mined;
	
	private String incoming;
	
	private String outgoing;
	
	private String create;
}
