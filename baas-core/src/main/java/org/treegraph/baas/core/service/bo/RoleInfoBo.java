package org.treegraph.baas.core.service.bo;

import java.io.Serializable;
import java.util.List;

import org.treegraph.baas.core.domain.RolePermissionPo;
import org.treegraph.baas.core.domain.RolePo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleInfoBo implements Serializable {
   
	private List<RolePermissionPo> rolePermissionList;
	
    private RolePo role;

    private static final long serialVersionUID = 1L;
}