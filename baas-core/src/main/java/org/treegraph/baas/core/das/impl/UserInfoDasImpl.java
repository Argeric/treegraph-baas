package org.treegraph.baas.core.das.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.treegraph.baas.core.das.UserInfoDas;
import org.treegraph.baas.core.domain.UserInfoPo;
import org.treegraph.baas.core.domain.UserInfoPoExample;
import org.treegraph.baas.core.domain.UserRolePo;
import org.treegraph.baas.core.mapper.UserInfoPoMapper;
import org.treegraph.baas.core.mapper.UserRolePoMapper;
import org.treegraph.baas.core.util.CollectionUtils;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserInfoDasImpl implements UserInfoDas{
	
    private UserInfoPoMapper userInfoPoMapper;
    
    private UserRolePoMapper userRolePoMapper;
    
    public UserInfoPo findById(String userId) {
        return userInfoPoMapper.selectByPrimaryKey(userId);
    }

    public UserInfoPo findByAccountNo(String accountNo) {

    	UserInfoPoExample userInfoPoExample = new UserInfoPoExample();
    	userInfoPoExample.createCriteria().andAccountEqualTo(accountNo);
        List<UserInfoPo> users = userInfoPoMapper.selectByExample(userInfoPoExample);
    	return users.size() > 0 ? users.get(0) : null;
    }
    
    public boolean createUser(UserInfoPo user, List<UserRolePo> userRoles) {
    	
    	boolean succFlag = Boolean.TRUE;
    	
    	succFlag = succFlag && userInfoPoMapper.insert(user) > 0;
    	if(CollectionUtils.isNotEmpty(userRoles)) {
    		succFlag = succFlag && userRolePoMapper.bulkInsert(userRoles) == userRoles.size();
    	}
    	
    	return succFlag;
    }
}
