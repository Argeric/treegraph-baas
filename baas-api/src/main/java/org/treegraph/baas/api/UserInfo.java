package org.treegraph.baas.api;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserInfo {
	
	private String id;
	
	private String name;
	
	private String account;
	
	private String phone;
	
	private String createTime;

	private int status;
}
