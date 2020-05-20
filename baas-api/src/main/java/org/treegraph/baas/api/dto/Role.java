package org.treegraph.baas.api.dto;

import lombok.Builder;
import lombok.Data;

/**
 * @author zt
 * @date May 11, 2020
 * @version 1.0
 */
@Data
@Builder
public class Role {

	private String roleId;
	
	private String roleName;
}
