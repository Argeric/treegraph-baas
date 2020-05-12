package org.treegraph.baas.core.dao.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.treegraph.baas.core.dao.UserInfoDao;
import org.treegraph.baas.core.domain.UserInfoPo;
import org.treegraph.baas.core.domain.UserInfoPoExample;
import org.treegraph.baas.core.mapper.UserInfoPoMapper;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserInfoDaoImpl implements UserInfoDao{
	
    private UserInfoPoMapper userInfoPoMapper;
    
    public UserInfoPo findById(String userId) {
        return userInfoPoMapper.selectByPrimaryKey(userId);
    }

    public UserInfoPo findByAccountNo(String accountNo) {

    	UserInfoPoExample userInfoPoExample = new UserInfoPoExample();
    	userInfoPoExample.createCriteria().andAccountEqualTo(accountNo);
        List<UserInfoPo> users = userInfoPoMapper.selectByExample(userInfoPoExample);
        
        if(CollectionUtils.isEmpty(users)) {
        	return null;
        }

    	return users.get(0);
    }
}
