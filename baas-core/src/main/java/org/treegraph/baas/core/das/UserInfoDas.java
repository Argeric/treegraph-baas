package org.treegraph.baas.core.das;

import java.util.List;

import org.treegraph.baas.core.domain.UserInfoPo;
import org.treegraph.baas.core.domain.UserRolePo;

public interface UserInfoDas {
	
	UserInfoPo findById(String userId);
	
    UserInfoPo findByAccountNo(String accountNo) ;
    
    boolean createUser(UserInfoPo user, List<UserRolePo> userRoles);
    
}
