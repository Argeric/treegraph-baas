package org.treegraph.baas.core.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.treegraph.baas.core.dao.PermissionDao;
import org.treegraph.baas.core.domain.PermissionPo;
import org.treegraph.baas.core.domain.PermissionPoExample;
import org.treegraph.baas.core.domain.RolePermissionPo;
import org.treegraph.baas.core.domain.RolePermissionPoExample;
import org.treegraph.baas.core.mapper.PermissionPoMapper;
import org.treegraph.baas.core.mapper.RolePermissionPoMapper;
import org.treegraph.baas.core.util.CollectionUtils;

import com.google.common.collect.Lists;

@Service
public class PermissionDaoImpl implements PermissionDao {

  @Autowired
  private RolePermissionPoMapper rolePermissionPoMapper;

  @Autowired
  private PermissionPoMapper permissionPoMapper;

  @Override
  public List<RolePermissionPo> findRolePermissionsByRoleIds(List<Long> roldIds){
	  
	  RolePermissionPoExample example = new RolePermissionPoExample();
	  example.createCriteria().andRoleIdIn(roldIds);
	  
	  List<RolePermissionPo> rolePermissions = rolePermissionPoMapper.selectByExample(example);
	  if(CollectionUtils.isEmpty(rolePermissions)) {
		  return Lists.newArrayList();
	  }
    
    return rolePermissions;
  }
  
  @Override
  public List<PermissionPo> findPermissionsByIds(List<Long> permissionIds){
	  
	  PermissionPoExample example = new PermissionPoExample();
	  example.createCriteria().andIdIn(permissionIds);
	  
	  List<PermissionPo> permissions = permissionPoMapper.selectByExample(example);
	  if(CollectionUtils.isEmpty(permissions)) {
		  return Lists.newArrayList();
	  }
    
    return permissions;
  }
  
}
