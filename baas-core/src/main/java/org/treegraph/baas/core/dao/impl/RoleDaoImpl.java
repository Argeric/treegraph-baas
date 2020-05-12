package org.treegraph.baas.core.dao.impl;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.treegraph.baas.core.dao.RoleDao;
import org.treegraph.baas.core.domain.RolePo;
import org.treegraph.baas.core.domain.RolePoExample;
import org.treegraph.baas.core.domain.UserRolePo;
import org.treegraph.baas.core.domain.UserRolePoExample;
import org.treegraph.baas.core.mapper.RolePoMapper;
import org.treegraph.baas.core.mapper.UserRolePoMapper;

import com.google.common.collect.Lists;

@Service
public class RoleDaoImpl implements RoleDao {
	
	@Autowired
	private UserRolePoMapper userRolePoMapper;

    @Autowired
    private RolePoMapper rolePoMapper;
    
    public List<UserRolePo> findUserRolesByUserId(String userId){
    	
    	UserRolePoExample example = new UserRolePoExample();
    	example.createCriteria().andUserIdEqualTo(userId);
    	List<UserRolePo> userRoleList = userRolePoMapper.selectByExample(example);
    	
    	if(CollectionUtils.isEmpty(userRoleList)) {
    		return Lists.newArrayList();
    	}
    	
    	return userRoleList;
    }
    
    public List<RolePo> findRolesByIds(List<Long> roleIds){
    	
    	RolePoExample example = new RolePoExample();
    	example.createCriteria().andIdIn(roleIds);
    	List<RolePo> roleList = rolePoMapper.selectByExample(example);
    	
    	if(CollectionUtils.isEmpty(roleList)) {
    		return Lists.newArrayList();
    	}
    	
    	return roleList;
    }
}
