package org.treegraph.baas.api.dto.request;

import lombok.Data;

@Data
public class ModifyConsRequest {
	
	private String id;

	private String name;
	
	private String remark;
	
	private String contact;
	
	private String phone;
	
	private String email;
	
	private String avatar;
}
