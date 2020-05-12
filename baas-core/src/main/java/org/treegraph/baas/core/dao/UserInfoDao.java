package org.treegraph.baas.core.dao;

import org.treegraph.baas.core.domain.UserInfoPo;

public interface UserInfoDao {
	
	UserInfoPo findById(String userId);
	
    UserInfoPo findByAccountNo(String accountNo) ;
    
}
