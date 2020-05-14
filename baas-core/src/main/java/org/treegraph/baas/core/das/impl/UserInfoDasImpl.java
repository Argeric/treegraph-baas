package org.treegraph.baas.core.das.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.treegraph.baas.common.enums.IntTypeEnum;
import org.treegraph.baas.common.util.WebHelper;
import org.treegraph.baas.core.das.UserInfoDas;
import org.treegraph.baas.core.domain.RolePermissionPo;
import org.treegraph.baas.core.domain.RolePo;
import org.treegraph.baas.core.domain.UserInfoPo;
import org.treegraph.baas.core.domain.UserInfoPoExample;
import org.treegraph.baas.core.domain.UserInfoPoExample.Criteria;
import org.treegraph.baas.core.domain.UserRolePo;
import org.treegraph.baas.core.domain.UserRolePoExample;
import org.treegraph.baas.core.mapper.RolePermissionPoMapper;
import org.treegraph.baas.core.mapper.RolePoMapper;
import org.treegraph.baas.core.mapper.UserInfoPoMapper;
import org.treegraph.baas.core.mapper.UserRolePoMapper;
import org.treegraph.baas.core.util.CollectionUtils;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserInfoDasImpl implements UserInfoDas{
	
    private UserInfoPoMapper userInfoPoMapper;
    
    private UserRolePoMapper userRolePoMapper;
    
    private RolePoMapper rolePoMapper;
    
    private RolePermissionPoMapper rolePermissionPoMapper;
    
    public Optional<UserInfoPo> findById(String userId) {
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
    
    public boolean delUser(UserInfoPo user) {
    	return userInfoPoMapper.updateByPrimaryKeySelective(user) > 0;
    }
    
    public List<UserInfoPo> findUsers(String account, String name, String email, String phone) {
    	
    	UserInfoPoExample userInfoPoExample = new UserInfoPoExample();
    	Criteria criteria = userInfoPoExample.createCriteria();
    	if(account != null) {
    		criteria.andAccountEqualTo(account);
    	}
    	if(name != null) {
    		criteria.andNameEqualTo(name);
    	}
    	if(email != null) {
    		criteria.andEmailEqualTo(email);
    	}
    	if(phone != null) {
    		criteria.andPhoneEqualTo(phone);
    	}
    	
    	return userInfoPoMapper.selectByExample(userInfoPoExample);
    }
    
    public boolean updateUser(UserInfoPo user) {
    	return userInfoPoMapper.updateByPrimaryKeySelective(user) > 0;
    }
    
    public boolean modifyUser(String uesrId, UserInfoPo user, List<String> toDelRoleIds, List<UserRolePo> toAddUserRoles) {
    	
    	boolean succFlag = Boolean.TRUE;
    	
    	if(user != null) {
    		succFlag = succFlag && userInfoPoMapper.updateByPrimaryKeySelective(user) > 0;
    	}
    	if(CollectionUtils.isNotEmpty(toDelRoleIds)) {
    		UserRolePo record = UserRolePo.builder().deleteFlag(IntTypeEnum.TRUE.getValue())
    				.updateTime(LocalDateTime.now()).updateBy(WebHelper.getUserId()).build();
    		UserRolePoExample example = new UserRolePoExample();
    		example.createCriteria().andUserIdEqualTo(uesrId).andRoleIdIn(toDelRoleIds);
    		succFlag = succFlag && userRolePoMapper.updateByExampleSelective(record, example) == toDelRoleIds.size();
    	}
    	if(CollectionUtils.isNotEmpty(toAddUserRoles)) {
    		succFlag = succFlag && userRolePoMapper.bulkInsert(toAddUserRoles) == toAddUserRoles.size();
    	}
    	
    	return succFlag;
    }
    
	public boolean createConsAdmin(RolePo role, List<RolePermissionPo> permissionList, List<UserInfoPo> users,
			List<UserRolePo> userRoles) {
		
    	boolean succFlag =  Boolean.TRUE;
    	if(role != null) {
    		succFlag = succFlag && rolePoMapper.insert(role) > 0;
    	}
    	if(CollectionUtils.isNotEmpty(permissionList)) {
    		succFlag = succFlag && rolePermissionPoMapper.bulkInsert(permissionList) == permissionList.size();
    	}
    	if(CollectionUtils.isNotEmpty(users)) {
    		succFlag = succFlag && userInfoPoMapper.bulkInsert(users) == users.size();
    	}
    	if(CollectionUtils.isNotEmpty(userRoles)) {
    		succFlag = succFlag && userRolePoMapper.bulkInsert(userRoles) == userRoles.size();
    	}
    	
    	
    	return succFlag;
	}
}
