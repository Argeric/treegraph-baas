package org.treegraph.baas.core.das.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.treegraph.baas.common.enums.IntTypeEnum;
import org.treegraph.baas.core.das.PermissionDas;
import org.treegraph.baas.core.das.bo.PermissionInfoBo;
import org.treegraph.baas.core.domain.PermissionPo;
import org.treegraph.baas.core.domain.PermissionPoExample;
import org.treegraph.baas.core.domain.RolePermissionPo;
import org.treegraph.baas.core.domain.RolePermissionPoExample;
import org.treegraph.baas.core.mapper.primary.PermissionPoMapper;
import org.treegraph.baas.core.mapper.primary.RolePermissionPoMapper;

import com.google.common.collect.Lists;

@Service
public class PermissionDasImpl implements PermissionDas {

  @Autowired
  private RolePermissionPoMapper rolePermissionPoMapper;

  @Autowired
  private PermissionPoMapper permissionPoMapper;

  @Override
  public Optional<PermissionPo> findById(Long permissionId) {
    return permissionPoMapper.selectByPrimaryKey(permissionId);
  }

  @Override
  public List<PermissionPo> findByIds(List<Long> permissionIds) {
    if (CollectionUtils.isEmpty(permissionIds)) {
      return Lists.newArrayList();
    }
    PermissionPoExample example = new PermissionPoExample();
    example.createCriteria().andIdIn(permissionIds);
    example.setOrderByClause("create_time desc");
    return permissionPoMapper.selectByExample(example);
  }

  @Override
  public List<PermissionPo> findAll() {
    PermissionPoExample example = new PermissionPoExample();
    example.createCriteria().andDeleteFlagEqualTo(IntTypeEnum.FALSE.getValue());
    example.setOrderByClause("create_time desc");
    return permissionPoMapper.selectByExample(example);
  }

  @Override
  public List<RolePermissionPo> findRolePermissionsByRoleId(String roldId) {
    RolePermissionPoExample example = new RolePermissionPoExample();
    example.createCriteria().andRoleIdEqualTo(roldId)
        .andDeleteFlagEqualTo(IntTypeEnum.FALSE.getValue());
    return rolePermissionPoMapper.selectByExample(example);
  }

  @Override
  public List<RolePermissionPo> findRolePermissionsByRoleIds(List<String> roleIds) {
    if (CollectionUtils.isEmpty(roleIds)) {
      return Lists.newArrayList();
    }
    RolePermissionPoExample example = new RolePermissionPoExample();
    example.createCriteria().andRoleIdIn(roleIds)
        .andDeleteFlagEqualTo(IntTypeEnum.FALSE.getValue());
    return rolePermissionPoMapper.selectByExample(example);
  }

  @Override
  public Map<String, Long> validatePermissions(List<String> reqPermissions) {
    List<PermissionPo> permissions = this.findAll();
    Set<String> permissionCodeSet = permissions.stream().map(PermissionPo::getPermissionCode)
        .collect(Collectors.toSet());
    reqPermissions.stream().forEach(item -> {
      Assert.isTrue(permissionCodeSet.contains(item), "角色权限非法[" + item + "]");
    });

    Map<String, Long> permissionCodeIdMap = permissions.stream()
        .collect(Collectors.toMap(PermissionPo::getPermissionCode, PermissionPo::getId));
    return permissionCodeIdMap;
  }

  @Override
  public List<String> findPermissionCodeByUserId(String userId) {
    return permissionPoMapper.findPermissionCodeByUserId(userId);
  }

  @Override
  public List<PermissionInfoBo> findPermissionInfoByUserId(String userId) {
    return permissionPoMapper.findPermissionInfoByUserId(userId);
  }
}
