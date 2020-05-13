package org.treegraph.baas.core.das;

import java.util.List;
import java.util.Optional;

import org.treegraph.baas.core.domain.UserInfoPo;
import org.treegraph.baas.core.domain.UserRolePo;

public interface UserInfoDas {
	
	Optional<UserInfoPo> findById(String userId);
	
    UserInfoPo findByAccountNo(String accountNo) ;
    
    boolean createUser(UserInfoPo user, List<UserRolePo> userRoles);
    
    boolean delUser(UserInfoPo user);
    
    List<UserInfoPo> findUsers(String account, String name, String email, String phone);
    
    boolean updateUser(UserInfoPo user);
    
    boolean modifyUser(String uesrId, UserInfoPo user, List<String> toDelRoleIds, List<UserRolePo> toAddUserRoles);
}
