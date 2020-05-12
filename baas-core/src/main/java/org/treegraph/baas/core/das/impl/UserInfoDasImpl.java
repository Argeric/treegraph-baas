package org.treegraph.baas.core.das.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.treegraph.baas.core.das.UserInfoDas;
import org.treegraph.baas.core.domain.UserInfoPo;
import org.treegraph.baas.core.domain.UserInfoPoExample;
import org.treegraph.baas.core.mapper.UserInfoPoMapper;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserInfoDasImpl implements UserInfoDas{
	
    private UserInfoPoMapper userInfoPoMapper;
    
    public UserInfoPo findById(String userId) {
        return userInfoPoMapper.selectByPrimaryKey(userId);
    }

    public UserInfoPo findByAccountNo(String accountNo) {

    	UserInfoPoExample userInfoPoExample = new UserInfoPoExample();
    	userInfoPoExample.createCriteria().andAccountEqualTo(accountNo);
        List<UserInfoPo> users = userInfoPoMapper.selectByExample(userInfoPoExample);
    	return users.size() > 0 ? users.get(0) : null;
    }
}
