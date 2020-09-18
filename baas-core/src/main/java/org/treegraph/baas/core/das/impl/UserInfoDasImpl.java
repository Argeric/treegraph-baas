package org.treegraph.baas.core.das.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.treegraph.baas.common.enums.IntTypeEnum;
import org.treegraph.baas.common.util.PermHelper;
import org.treegraph.baas.common.util.WebHelper;
import org.treegraph.baas.core.das.UserInfoDas;
import org.treegraph.baas.core.domain.ConsortiumPo;
import org.treegraph.baas.core.domain.RolePermissionPo;
import org.treegraph.baas.core.domain.RolePo;
import org.treegraph.baas.core.domain.UserInfoPo;
import org.treegraph.baas.core.domain.UserInfoPoExample;
import org.treegraph.baas.core.domain.UserInfoPoExample.Criteria;
import org.treegraph.baas.core.domain.UserInvitePo;
import org.treegraph.baas.core.domain.UserOrganizationPo;
import org.treegraph.baas.core.domain.UserOrganizationPoExample;
import org.treegraph.baas.core.domain.UserRoleInvitePo;
import org.treegraph.baas.core.domain.UserRolePo;
import org.treegraph.baas.core.domain.UserRolePoExample;
import org.treegraph.baas.core.mapper.primary.ConsortiumPoMapper;
import org.treegraph.baas.core.mapper.primary.RolePermissionPoMapper;
import org.treegraph.baas.core.mapper.primary.RolePoMapper;
import org.treegraph.baas.core.mapper.primary.UserInfoPoMapper;
import org.treegraph.baas.core.mapper.primary.UserInvitePoMapper;
import org.treegraph.baas.core.mapper.primary.UserOrganizationPoMapper;
import org.treegraph.baas.core.mapper.primary.UserRoleInvitePoMapper;
import org.treegraph.baas.core.mapper.primary.UserRolePoMapper;
import org.treegraph.baas.core.util.CacheTimeUtil;
import org.treegraph.baas.core.util.JwtUtils;

import com.google.common.collect.Lists;

import io.micrometer.core.instrument.util.StringUtils;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserInfoDasImpl implements UserInfoDas{
	
    private UserInfoPoMapper userInfoPoMapper;
    
    private UserRolePoMapper userRolePoMapper;
    
    private UserOrganizationPoMapper userOrganizationPoMapper;
    
    private RolePoMapper rolePoMapper;
    
    private RolePermissionPoMapper rolePermissionPoMapper;
    
    private UserInvitePoMapper userInvitePoMapper;
    
    private UserRoleInvitePoMapper userRoleInvitePoMapper;
    
    private ConsortiumPoMapper consortiumPoMapper;
    
    public Optional<UserInfoPo> findById(String userId) {
        return userInfoPoMapper.selectByPrimaryKey(userId);
    }
    
    public Optional<UserInfoPo> findByAccount(String account) {
    	UserInfoPoExample example = new UserInfoPoExample();
    	example.createCriteria().andAccountEqualTo(account)
    	.andDeleteFlagEqualTo(IntTypeEnum.FALSE.getValue());
    	List<UserInfoPo> users = userInfoPoMapper.selectByExample(example);
    	return Optional.ofNullable(users.size() > 0 ? users.get(0) : null);
    }
    
    public List<UserInfoPo> findByIds(List<String> userIds) {
    	if(CollectionUtils.isEmpty(userIds)) {
			return Lists.newArrayList();
		}
    	UserInfoPoExample example = new UserInfoPoExample();
    	example.createCriteria().andIdIn(userIds);
    	example.setOrderByClause("create_time desc");
        return userInfoPoMapper.selectByExample(example);
    }
    
    public List<UserInfoPo> findByAccountLike(String account) {
    	UserInfoPoExample example = new UserInfoPoExample();
    	example.createCriteria().andAccountLike("%"+account+"%")
    	.andDeleteFlagEqualTo(IntTypeEnum.FALSE.getValue());
    	example.setOrderByClause("create_time desc");
        return userInfoPoMapper.selectByExample(example);
    }
    
    public List<UserInfoPo> findAll(String account, String name, String email, String phone) {
		if (!PermHelper.getSysAdminFlag()) {
			return userInfoPoMapper.selectUserList(account, name, email, phone,
					Lists.newArrayList(PermHelper.getConsIdSet()), WebHelper.getUserId());
		}
    	
    	UserInfoPoExample example = new UserInfoPoExample();
    	Criteria criteria = example.createCriteria();
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
    	example.setOrderByClause("create_time desc");
    	return userInfoPoMapper.selectByExample(example);
    }
    
	public List<UserOrganizationPo> findUserOrgsByUserIds(List<String> userIds) {
		if(CollectionUtils.isEmpty(userIds)) {
			return Lists.newArrayList();
		}
		UserOrganizationPoExample example = new UserOrganizationPoExample();
		example.createCriteria().andUserIdIn(userIds)
		.andDeleteFlagEqualTo(IntTypeEnum.FALSE.getValue());
		return userOrganizationPoMapper.selectByExample(example);
	}    
	
	public long count() {
		UserInfoPoExample example = new UserInfoPoExample();
		example.createCriteria().andDeleteFlagEqualTo(IntTypeEnum.FALSE.getValue());
		return userInfoPoMapper.countByExample(example);
	}
    
    public boolean createUser(UserInfoPo user, List<UserRolePo> userRoles, List<UserOrganizationPo> userOrgs) {
    	boolean succFlag = Boolean.TRUE;
    	
    	succFlag = succFlag && userInfoPoMapper.insert(user) > 0;
    	if(CollectionUtils.isNotEmpty(userRoles)) {
    		succFlag = succFlag && userRolePoMapper.bulkInsert(userRoles) == userRoles.size();
    	}
    	if(CollectionUtils.isNotEmpty(userOrgs)) {
    		succFlag = succFlag && userOrganizationPoMapper.bulkInsert(userOrgs) == userOrgs.size();
    	}
    	
    	return succFlag;
    }
    
    public boolean createUserRoles(List<UserRolePo> userRoles) {
    	boolean succFlag = Boolean.TRUE;
    	
    	if(CollectionUtils.isNotEmpty(userRoles)) {
    		succFlag = succFlag && userRolePoMapper.bulkInsert(userRoles) == userRoles.size();
    	}
    	
    	return succFlag;
    }
    
    
	public boolean inviteUser(UserInvitePo userInvite, List<UserRoleInvitePo> userRoleInvites) {
		boolean succFlag = Boolean.TRUE;

		succFlag = succFlag && userInvitePoMapper.insert(userInvite) > 0;
		
		if (CollectionUtils.isNotEmpty(userRoleInvites)) {
			userRoleInvites.stream().forEach(item -> item.setInviteId(userInvite.getId()));
			succFlag = succFlag && userRoleInvitePoMapper.bulkInsert(userRoleInvites) == userRoleInvites.size();
		}
		
		long expireSecs = CacheTimeUtil.getSecondToMorning();
		String token = JwtUtils.generateInviteToken(userInvite.getId(), userInvite.getInviter(),
				userInvite.getAccepter(), expireSecs);
		UserInvitePo updateInviteUser = UserInvitePo.builder()
				.id(userInvite.getId())
				.token(token)
				.expire((int)expireSecs)
				.build();
		succFlag = succFlag && userInvitePoMapper.updateByPrimaryKeySelective(updateInviteUser) > 0;
		
		return succFlag;
	}
	
	public boolean updateInviteUserById(UserInvitePo userInvite) {
		return userInvitePoMapper.updateByPrimaryKeySelective(userInvite) > 0;
	}
    
    public boolean delUser(UserInfoPo user, List<Long> userRoleIds) {
    	boolean succFlag = Boolean.TRUE;
    	succFlag = succFlag && userInfoPoMapper.updateByPrimaryKeySelective(user) > 0;
    	
    	if(CollectionUtils.isNotEmpty(userRoleIds)) {
	    	UserRolePo record = UserRolePo.builder().deleteFlag(IntTypeEnum.TRUE.getValue())
	    			.updateTime(LocalDateTime.now()).updateBy(WebHelper.getUserId()).build();
	    	UserRolePoExample example = new UserRolePoExample();
	    	example.createCriteria().andIdIn(userRoleIds);
	    	succFlag = succFlag && userRolePoMapper.updateByExampleSelective(record, example) == userRoleIds.size();
    	}
    	
    	return succFlag;
    }
    
    public boolean updateUser(UserInfoPo user) {
    	return userInfoPoMapper.updateByPrimaryKeySelective(user) > 0;
    }
    
	public boolean modifyUser(String uesrId, UserInfoPo user, List<Long> toDelUserRoleIds,
			List<UserRolePo> toAddUserRoles, List<Long> toDelUserOrgIds, List<UserOrganizationPo> toAddUserOrgs) {
    	boolean succFlag = Boolean.TRUE;
    	if(user != null) {
    		succFlag = succFlag && userInfoPoMapper.updateByPrimaryKeySelective(user) > 0;
    	}
    	
    	if(CollectionUtils.isNotEmpty(toDelUserRoleIds)) {
    		UserRolePo record = UserRolePo.builder().deleteFlag(IntTypeEnum.TRUE.getValue())
    				.updateTime(LocalDateTime.now()).updateBy(WebHelper.getUserId()).build();
    		UserRolePoExample example = new UserRolePoExample();
    		example.createCriteria().andIdIn(toDelUserRoleIds);
    		succFlag = succFlag && userRolePoMapper.updateByExampleSelective(record, example) == toDelUserRoleIds.size();
    	}
    	if(CollectionUtils.isNotEmpty(toAddUserRoles)) {
    		succFlag = succFlag && userRolePoMapper.bulkInsert(toAddUserRoles) == toAddUserRoles.size();
    	}
    	
    	if(CollectionUtils.isNotEmpty(toDelUserOrgIds)) {
    		UserOrganizationPo record = UserOrganizationPo.builder().deleteFlag(IntTypeEnum.TRUE.getValue())
    				.updateTime(LocalDateTime.now()).build();
    		UserOrganizationPoExample example = new UserOrganizationPoExample();
    		example.createCriteria().andIdIn(toDelUserOrgIds);
    		succFlag = succFlag && userOrganizationPoMapper.updateByExampleSelective(record, example) == toDelUserOrgIds.size();
    	}
    	if(CollectionUtils.isNotEmpty(toAddUserOrgs)) {
    		succFlag = succFlag && userOrganizationPoMapper.bulkInsert(toAddUserOrgs) == toAddUserOrgs.size();
    	}
    	
    	return succFlag;
    }
    
	public boolean createConsAdmin(RolePo role, List<RolePermissionPo> permissionList, List<UserInfoPo> users,
			List<UserRolePo> userRoles, List<UserOrganizationPo> userOrgs, ConsortiumPo cons) {
		
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
    	if(CollectionUtils.isNotEmpty(userOrgs)) {
    		succFlag = succFlag && userOrganizationPoMapper.bulkInsert(userOrgs) == userOrgs.size();
    	}
    	succFlag = succFlag && consortiumPoMapper.updateByPrimaryKeySelective(cons) > 0;
    	
    	return succFlag;
	}
}
