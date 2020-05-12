package org.treegraph.baas.api;

import java.util.List;

import lombok.Builder;
import lombok.Data;

/**
 * @author zt
 * @date May 11, 2020
 * @version 1.0
 */
@Data
@Builder
public class RolePermissionInfo {
	
	private String id;

	private String name;
	
	private String consId;
	
	private String consName;
	
	private String createTime;
	
	private List<String> permissions;
}
