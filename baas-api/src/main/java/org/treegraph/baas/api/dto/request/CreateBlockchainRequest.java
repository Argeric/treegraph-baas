package org.treegraph.baas.api.dto.request;

import lombok.Data;

@Data
public class CreateBlockchainRequest {
	
	private String name;

	private String consId;
	
	private String alertEmail;
	
	private String consensus;
	
	private String algorithm;
}
