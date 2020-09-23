package org.treegraph.baas.core.das.bo;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zt
 * @date May 11, 2020
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PermissionInfoBo implements Serializable {
	private String consId;
	
	private String roleId;
	
	private Integer roleType;
	
	private String roleName;
	
	private String permId;
	
	private String permCode;
	
    private static final long serialVersionUID = 1L;
}
