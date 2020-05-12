package org.treegraph.baas.core.das;

import org.treegraph.baas.core.domain.UserInfoPo;

public interface UserInfoDas {
	
	UserInfoPo findById(String userId);
	
    UserInfoPo findByAccountNo(String accountNo) ;
    
}
