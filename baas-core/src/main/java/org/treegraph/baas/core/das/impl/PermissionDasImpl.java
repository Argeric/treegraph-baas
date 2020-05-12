package org.treegraph.baas.core.das.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.treegraph.baas.core.das.PermissionDas;
import org.treegraph.baas.core.domain.PermissionPo;
import org.treegraph.baas.core.domain.PermissionPoExample;
import org.treegraph.baas.core.domain.RolePermissionPo;
import org.treegraph.baas.core.domain.RolePermissionPoExample;
import org.treegraph.baas.core.mapper.PermissionPoMapper;
import org.treegraph.baas.core.mapper.RolePermissionPoMapper;

@Service
public class PermissionDasImpl implements PermissionDas {

  @Autowired
  private RolePermissionPoMapper rolePermissionPoMapper;

  @Autowired
  private PermissionPoMapper permissionPoMapper;
  
  @Override
  public List<RolePermissionPo> findRolePermissionsByRoleId(String roldId){
	  RolePermissionPoExample example = new RolePermissionPoExample();
	  example.createCriteria().andRoleIdEqualTo(roldId);
	  return rolePermissionPoMapper.selectByExample(example);
  }

  @Override
  public List<RolePermissionPo> findRolePermissionsByRoleIds(List<String> roldIds){
	  
	  RolePermissionPoExample example = new RolePermissionPoExample();
	  example.createCriteria().andRoleIdIn(roldIds);
	  return rolePermissionPoMapper.selectByExample(example);
  }
  
  @Override
  public List<PermissionPo> findPermissionsByIds(List<Long> permissionIds){
	  
	  PermissionPoExample example = new PermissionPoExample();
	  example.createCriteria().andIdIn(permissionIds);
	  return permissionPoMapper.selectByExample(example);
  }
  
  @Override
  public List<PermissionPo> findAllPermissions(){
	  PermissionPoExample example = new PermissionPoExample();
	  return permissionPoMapper.selectByExample(example);
  }
  
}
