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
public class RoleInfo {

	private String consId;
	
	private String orgName;
	
	private List<String> roleIds;
}
