package org.treegraph.baas.api.dto.request;

import java.util.List;

import org.treegraph.baas.api.RoleInfo;

import lombok.Data;

@Data
public class CreateUserRequest {

	private String account;
	
	private String pwd;
	
	private String name;
	
	private String phone;
	
	private List<RoleInfo> roles;
}
