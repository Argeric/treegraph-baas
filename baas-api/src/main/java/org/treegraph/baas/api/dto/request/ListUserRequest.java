package org.treegraph.baas.api.dto.request;

import org.treegraph.baas.api.dto.ListRequest;

import lombok.Data;

@Data
public class ListUserRequest extends ListRequest{
	
	private String account;
	
	private String name;
	
	private String email;
	
	private String phone;
}
