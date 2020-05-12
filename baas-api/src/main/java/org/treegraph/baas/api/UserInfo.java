package org.treegraph.baas.api;

import lombok.Data;

@Data
public class UserInfo {
	
	private String id;
	
	private String name;
	
	private String account;
	
	private String phone;
	
	private String createTime;

	private int status;
}
