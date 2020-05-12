package org.treegraph.baas.api.dto.request;

import java.util.List;

import lombok.Data;

@Data
public class ModifyRoleRequest {
	
	private String id;

	private String consId;
	
	private String name;
	
	private List<String> permissions;
}
