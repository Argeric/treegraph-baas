package org.treegraph.baas.api.dto.request;

import lombok.Data;

@Data
public class ListUserRequest {
	
	private int current;

	private int pageSize;
	
	private String account;
	
	private String name;
	
	private String email;
	
	private String phone;
}
