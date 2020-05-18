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

import io.micrometer.core.instrument.util.StringUtils;
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
    
    public List<UserInfoPo> findByIds(List<String> userIds) {
    	UserInfoPoExample example = new UserInfoPoExample();
    	example.createCriteria().andIdIn(userIds);
        return userInfoPoMapper.selectByExample(example);
    }

    public Optional<UserInfoPo> findByAccountNo(String accountNo) {
    	UserInfoPoExample userInfoPoExample = new UserInfoPoExample();
    	userInfoPoExample.createCriteria().andAccountEqualTo(accountNo)
    	.andDeleteFlagEqualTo(IntTypeEnum.FALSE.getValue());
        List<UserInfoPo> users = userInfoPoMapper.selectByExample(userInfoPoExample);
    	return Optional.ofNullable(users.size() > 0 ? users.get(0) : null);
    }
    
    public List<UserInfoPo> findByAccountNoInLike(String accountNo) {
    	UserInfoPoExample userInfoPoExample = new UserInfoPoExample();
    	userInfoPoExample.createCriteria().andAccountLike("%"+accountNo+"%")
    	.andDeleteFlagEqualTo(IntTypeEnum.FALSE.getValue());
        return userInfoPoMapper.selectByExample(userInfoPoExample);
    }
    
    public List<UserInfoPo> findAll(String account, String name, String email, String phone) {
    	
    	UserInfoPoExample userInfoPoExample = new UserInfoPoExample();
    	Criteria criteria = userInfoPoExample.createCriteria();
    	if(StringUtils.isNotBlank(account)) {
    		criteria.andAccountEqualTo(account);
    	}
    	if(StringUtils.isNotBlank(name)) {
    		criteria.andNameEqualTo(name);
    	}
    	if(StringUtils.isNotBlank(email)) {
    		criteria.andEmailEqualTo(email);
    	}
    	if(StringUtils.isNotBlank(phone)) {
    		criteria.andPhoneEqualTo(phone);
    	}
    	criteria.andDeleteFlagEqualTo(IntTypeEnum.FALSE.getValue());
    	
    	return userInfoPoMapper.selectByExample(userInfoPoExample);
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
