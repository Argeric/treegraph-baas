package org.treegraph.baas.core.das.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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
import org.treegraph.baas.core.enums.UserEnum;
import org.treegraph.baas.core.mapper.primary.RolePermissionPoMapper;
import org.treegraph.baas.core.mapper.primary.RolePoMapper;
import org.treegraph.baas.core.mapper.primary.UserRolePoMapper;

import com.google.common.collect.Lists;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RoleDasImpl implements RoleDas {

  private UserRolePoMapper userRolePoMapper;

  private RolePoMapper rolePoMapper;

  private RolePermissionPoMapper rolePermissionPoMapper;

  public Optional<RolePo> findById(String roleId) {
    return rolePoMapper.selectByPrimaryKey(roleId);
  }

  public List<RolePo> findByIds(List<String> roleIds) {
    if (CollectionUtils.isEmpty(roleIds)) {
      return Lists.newArrayList();
    }
    RolePoExample example = new RolePoExample();
    example.createCriteria().andIdIn(roleIds);
    example.setOrderByClause("create_time desc");
    return rolePoMapper.selectByExample(example);
  }

  public List<RolePo> findAll(String id, String consId, String roleName) {
    RolePoExample example = new RolePoExample();
    Criteria criteria = example.createCriteria();
    if (StringUtils.isNotBlank(id)) {
      criteria.andIdEqualTo(id);
    }
    if (StringUtils.isNotBlank(consId)) {
      criteria.andConsortiumIdEqualTo(consId);
    }
    if (StringUtils.isNotBlank(roleName)) {
      criteria.andRoleNameEqualTo(roleName);
    }
    criteria.andDeleteFlagEqualTo(IntTypeEnum.FALSE.getValue());
    example.setOrderByClause("create_time desc");
    return rolePoMapper.selectByExample(example);
  }

  public List<UserRolePo> findUserRolesByUserId(String userId) {
    UserRolePoExample example = new UserRolePoExample();
    example.createCriteria().andUserIdEqualTo(userId)
        .andDeleteFlagEqualTo(IntTypeEnum.FALSE.getValue());
    return userRolePoMapper.selectByExample(example);
  }

  public List<UserRolePo> findUserRolesByUserIds(List<String> userIds) {
    if (CollectionUtils.isEmpty(userIds)) {
      return Lists.newArrayList();
    }
    UserRolePoExample example = new UserRolePoExample();
    example.createCriteria().andUserIdIn(userIds)
        .andDeleteFlagEqualTo(IntTypeEnum.FALSE.getValue());
    return userRolePoMapper.selectByExample(example);
  }

  public List<UserRolePo> findUserRolesForConsAdmin(List<String> userIds, String roleId) {
    if (CollectionUtils.isEmpty(userIds)) {
      return Lists.newArrayList();
    }
    UserRolePoExample example = new UserRolePoExample();
    example.createCriteria()
        .andUserIdIn(userIds)
        .andRoleIdEqualTo(roleId)
        .andDeleteFlagEqualTo(IntTypeEnum.FALSE.getValue());
    return userRolePoMapper.selectByExample(example);
  }

  public List<RolePo> findByConsIds(List<String> consIds) {
    if (CollectionUtils.isEmpty(consIds)) {
      return Lists.newArrayList();
    }
    RolePoExample example = new RolePoExample();
    example.createCriteria().andConsortiumIdIn(consIds)
        .andDeleteFlagEqualTo(IntTypeEnum.FALSE.getValue());

    return rolePoMapper.selectByExample(example);
  }

  public Optional<RolePo> findConsAdminRole(String consId) {
    RolePoExample example = new RolePoExample();
    example.createCriteria().andConsortiumIdEqualTo(consId)
        .andRoleTypeEqualTo(UserEnum.Role.CONSORTIUM_ADMIN.getCode())
        .andDeleteFlagEqualTo(IntTypeEnum.FALSE.getValue());
    List<RolePo> roles = rolePoMapper.selectByExample(example);

    return Optional.ofNullable(CollectionUtils.isEmpty(roles) ? null : roles.get(0));
  }

  public List<UserRolePo> findUserRolesByRoleId(String roleId) {
    UserRolePoExample example = new UserRolePoExample();
    example.createCriteria().andRoleIdEqualTo(roleId)
        .andDeleteFlagEqualTo(IntTypeEnum.FALSE.getValue());
    return userRolePoMapper.selectByExample(example);
  }

  public boolean createRole(RolePo role, List<RolePermissionPo> permissionList) {
    boolean succFlag = Boolean.TRUE;
    succFlag = succFlag && rolePoMapper.insert(role) > 0;
    if (CollectionUtils.isNotEmpty(permissionList)) {
      succFlag =
          succFlag && rolePermissionPoMapper.bulkInsert(permissionList) == permissionList.size();
    }
    return succFlag;
  }

  public boolean modifyRole(String roleId, RolePo role, List<Long> toDelPermIds,
      List<RolePermissionPo> toAddPerms) {
    boolean succFlag = Boolean.TRUE;
    if (role != null) {
      succFlag = succFlag && rolePoMapper.updateByPrimaryKeySelective(role) > 0;
    }

    if (CollectionUtils.isNotEmpty(toDelPermIds)) {
      RolePermissionPo record = RolePermissionPo.builder()
          .deleteFlag(IntTypeEnum.TRUE.getValue())
          .updateBy(WebHelper.getUserId())
          .updateTime(LocalDateTime.now())
          .build();
      RolePermissionPoExample example = new RolePermissionPoExample();
      example.createCriteria().andRoleIdEqualTo(roleId).andPermissionIdIn(toDelPermIds);
      succFlag = succFlag
          && rolePermissionPoMapper.updateByExampleSelective(record, example) == toDelPermIds
          .size();
    }

    if (CollectionUtils.isNotEmpty(toAddPerms)) {
      succFlag = succFlag && rolePermissionPoMapper.bulkInsert(toAddPerms) == toAddPerms.size();
    }

    return succFlag;
  }

  public boolean deleteRole(String roleId) {
    RolePo record = RolePo.builder()
        .id(roleId)
        .deleteFlag(IntTypeEnum.TRUE.getValue())
        .updateTime(LocalDateTime.now())
        .updateBy(WebHelper.getUserId())
        .build();
    return rolePoMapper.updateByPrimaryKeySelective(record) > 0;
  }
}
