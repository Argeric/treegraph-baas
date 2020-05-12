package org.treegraph.baas.core.das.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.treegraph.baas.common.enums.IntTypeEnum;
import org.treegraph.baas.common.util.WebHelper;
import org.treegraph.baas.core.das.RoleDas;
import org.treegraph.baas.core.domain.RolePermissionPo;
import org.treegraph.baas.core.domain.RolePermissionPoExample;
import org.treegraph.baas.core.domain.RolePo;
import org.treegraph.baas.core.domain.RolePoExample;
import org.treegraph.baas.core.domain.RolePoExample.Criteria;
import org.treegraph.baas.core.domain.UserRolePo;
import org.treegraph.baas.core.domain.UserRolePoExample;
import org.treegraph.baas.core.mapper.RolePermissionPoMapper;
import org.treegraph.baas.core.mapper.RolePoMapper;
import org.treegraph.baas.core.mapper.UserRolePoMapper;
import org.treegraph.baas.core.util.CollectionUtils;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RoleDasImpl implements RoleDas {
	
	private UserRolePoMapper userRolePoMapper;

    private RolePoMapper rolePoMapper;
    
    private RolePermissionPoMapper rolePermissionPoMapper;
    
    public List<UserRolePo> findUserRolesByUserId(String userId){
    	
    	UserRolePoExample example = new UserRolePoExample();
    	example.createCriteria().andUserIdEqualTo(userId);
    	return userRolePoMapper.selectByExample(example);
    }
    
    public List<RolePo> findRolesByIds(List<String> roleIds){
    	
    	RolePoExample example = new RolePoExample();
    	example.createCriteria().andIdIn(roleIds);
    	return rolePoMapper.selectByExample(example);
    }
    
    public Optional<RolePo> findRolesById(String roleId){
    	
    	return rolePoMapper.selectByPrimaryKey(roleId);
    }
    
    public boolean createRole(RolePo role, List<RolePermissionPo> permissionList) {
    	
    	boolean succFlag =  Boolean.TRUE;
    	succFlag = succFlag && rolePoMapper.insert(role) > 0;
    	if(CollectionUtils.isNotEmpty(permissionList)) {
    		permissionList.stream().forEach(item -> item.setRoleId(role.getId()));
    		succFlag = succFlag && rolePermissionPoMapper.bulkInsert(permissionList) == permissionList.size();
    	}
    	return succFlag;
    }
    
    public boolean modifyRole(String roleId, RolePo role, List<Long> toDelPermIds, List<RolePermissionPo> toAddPerms) {
   		
    	boolean succFlag =  Boolean.TRUE;
    	if(role != null) {
    		succFlag = succFlag && rolePoMapper.updateByPrimaryKeySelective(role) > 0;
    	}
    	
    	if(CollectionUtils.isNotEmpty(toDelPermIds)) {
    		RolePermissionPo record = RolePermissionPo.builder()
    				.deleteFlag(IntTypeEnum.TRUE.getValue())
    				.updateBy(WebHelper.getUserId())
    				.updateTime(LocalDateTime.now())
    				.build();
    		RolePermissionPoExample example = new RolePermissionPoExample();
        	example.createCriteria().andRoleIdEqualTo(roleId).andPermissionIdIn(toDelPermIds);
        	succFlag = succFlag && rolePermissionPoMapper.updateByExampleSelective(record, example) == toDelPermIds.size();
    	}
    	
    	if(CollectionUtils.isNotEmpty(toAddPerms)) {
    		succFlag = succFlag && rolePermissionPoMapper.bulkInsert(toAddPerms) == toAddPerms.size();
    	}
    	
    	return succFlag;
    }
    
    public List<RolePo> findRoles(String consId, String roleName){
    	
    	RolePoExample example = new RolePoExample();
    	Criteria criteria = example.createCriteria();
    	if(consId != null) {
    		criteria.andConsortiumIdEqualTo(consId);
    	}
    	if(roleName != null) {
    		criteria.andRoleNameEqualTo(roleName);
    	}
    	return rolePoMapper.selectByExample(example);
    }
    
    public List<RolePo> findRolesByConsIds(List<String> consIds){
    	
    	RolePoExample example = new RolePoExample();
    	example.createCriteria().andConsortiumIdIn(consIds);
    	
    	return rolePoMapper.selectByExample(example);
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}
