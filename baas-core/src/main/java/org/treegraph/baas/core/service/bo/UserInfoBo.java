package org.treegraph.baas.core.service.bo;

import java.io.Serializable;
import java.util.List;

import org.treegraph.baas.core.domain.UserInfoPo;
import org.treegraph.baas.core.domain.UserRolePo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoBo implements Serializable {
   
	private List<UserRolePo> userRoles;
	
    private UserInfoPo user;

    private static final long serialVersionUID = 1L;
}