package org.treegraph.baas.core.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.treegraph.baas.api.dto.RolePermissionInfo;
import org.treegraph.baas.api.dto.request.CreateRoleRequest;
import org.treegraph.baas.api.dto.request.DeleteRoleRequest;
import org.treegraph.baas.api.dto.request.ListRoleRequest;
import org.treegraph.baas.api.dto.request.ModifyRoleRequest;
import org.treegraph.baas.api.dto.request.QueryRoleRequest;
import org.treegraph.baas.api.dto.response.ListRoleResponse;
import org.treegraph.baas.common.enums.IntTypeEnum;
import org.treegraph.baas.common.util.WebHelper;
import org.treegraph.baas.core.constant.RoleConstant;
import org.treegraph.baas.core.das.ConsortiumDas;
import org.treegraph.baas.core.das.PermissionDas;
import org.treegraph.baas.core.das.RoleDas;
import org.treegraph.baas.core.domain.ConsortiumPo;
import org.treegraph.baas.core.domain.PermissionPo;
import org.treegraph.baas.core.domain.RolePermissionPo;
import org.treegraph.baas.core.domain.RolePo;
import org.treegraph.baas.core.domain.UserRolePo;
import org.treegraph.baas.core.enums.UserEnum;
import org.treegraph.baas.core.exception.AlertException;
import org.treegraph.baas.core.service.RoleService;
import org.treegraph.baas.core.service.bo.RoleInfoBo;
import org.treegraph.baas.core.util.DateTimeUtils;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;

import lombok.AllArgsConstructor;

@Validated
@Service
@AllArgsConstructor
public class RoleServiceImpl implements RoleService {

  private final RoleDas roleDas;

  private final PermissionDas permissionDas;

  private final ConsortiumDas consortiumDas;

  private final BuildService buildService;

  public ListRoleResponse listRole(ListRoleRequest req) {
    PageHelper.startPage(req.getCurrent(), req.getPageSize());
    List<RolePo> roles = roleDas.findAll(null, req.getConsId(), req.getName());
    PageInfo<RolePo> pageInfo = new PageInfo<>(roles);

    List<RolePo> roleList = pageInfo.getList();
    List<RolePermissionInfo> rolePermInfoList = getRolePermissionInfo(roleList);

    return ListRoleResponse.builder()
        .current(req.getCurrent())
        .pageSize(req.getPageSize())
        .total(pageInfo.getTotal())
        .list(rolePermInfoList)
        .build();
  }

  public List<RolePermissionInfo> queryRole(QueryRoleRequest req) {
    List<RolePo> roleList = roleDas.findAll(req.getId(), req.getConsId(), null);
    return getRolePermissionInfo(roleList);
  }

  public boolean createRole(CreateRoleRequest req) {
    Assert.isTrue(!RoleConstant.PREDEFINE_ROLE_NAME.contains(req.getName()),
        "预定义角色不可使用[" + req.getName() + "]");
    List<RolePo> roles = roleDas.findAll(null, req.getConsId(), req.getName());
    Assert.isTrue(CollectionUtils.isEmpty(roles), "同一联盟，角色名不可重复");
    RoleInfoBo roleInfoBo = buildService
        .buildRole(UserEnum.Role.COMMON_USER.getCode(), req.getName(),
            req.getConsId(), req.getPermissions());
    return roleDas.createRole(roleInfoBo.getRole(), roleInfoBo.getRolePermissionList());
  }

  public boolean modifyRole(ModifyRoleRequest req) {
    String roleId = req.getId();
    String consId = req.getConsId();
    RolePo role = roleDas.findById(roleId)
        .orElseThrow(() -> new AlertException("role.not.found[" + roleId + "]"));
    Assert
        .isTrue(role.getConsortiumId().equals(consId), "角色所属联盟不匹配[" + roleId + "][" + consId + "]");
    RolePo updateRole = null;
    if (req.getName() != null && !req.getName().equals(role.getRoleName())) {
      updateRole = RolePo.builder().id(role.getId()).roleName(role.getRoleName()).build();
    }

    List<String> reqPermissionCodes = req.getPermissions();
    List<RolePermissionPo> toAddPerms = null;
    List<Long> toDelPermIds = null;
    LocalDateTime now = LocalDateTime.now();
    if (CollectionUtils.isNotEmpty(reqPermissionCodes)) {
      Map<String, Long> permissionCodeIdMap = validatePermissions(reqPermissionCodes);
      List<RolePermissionPo> dbRolePermissions = permissionDas.findRolePermissionsByRoleId(roleId);
      List<Long> dbPermissionIds = dbRolePermissions.stream().map(RolePermissionPo::getPermissionId)
          .collect(Collectors.toList());
      List<PermissionPo> dbPermissions = permissionDas.findByIds(dbPermissionIds);
      List<String> dbPermissionCodes = dbPermissions.stream().map(PermissionPo::getPermissionCode)
          .collect(Collectors.toList());

      List<String> intersect = Lists.newArrayList();
      intersect.addAll(reqPermissionCodes);
      intersect.retainAll(dbPermissionCodes);
      reqPermissionCodes.removeAll(intersect);
      dbPermissionCodes.removeAll(intersect);

      toAddPerms = reqPermissionCodes.stream().map(item -> RolePermissionPo.builder()
          .roleId(roleId)
          .permissionId(permissionCodeIdMap.get(item))
          .consortiumId(req.getConsId())
          .deleteFlag(IntTypeEnum.FALSE.getValue())
          .createTime(now)
          .createBy(WebHelper.getUserId())
          .updateTime(now)
          .updateBy(WebHelper.getUserId())
          .build()).collect(Collectors.toList());
      toDelPermIds = dbPermissionCodes.stream().map(permissionCodeIdMap::get)
          .collect(Collectors.toList());
    }

    return roleDas.modifyRole(roleId, updateRole, toDelPermIds, toAddPerms);
  }

  public boolean deleteRole(DeleteRoleRequest req) {
    RolePo role = roleDas.findById(req.getId())
        .orElseThrow(() -> new AlertException("role.not.found"));
    List<UserRolePo> userRoles = roleDas.findUserRolesByRoleId(role.getId());
    Assert.isTrue(CollectionUtils.isEmpty(userRoles), "此角色正在使用，不可删除");
    return roleDas.deleteRole(role.getId());
  }

  private Map<String, Long> validatePermissions(List<String> reqPermissions) {
    List<PermissionPo> permissions = permissionDas.findAll();
    Set<String> permissionCodeSet = permissions.stream().map(PermissionPo::getPermissionCode)
        .collect(Collectors.toSet());
    reqPermissions
        .forEach(item -> Assert.isTrue(permissionCodeSet.contains(item), "角色权限非法[" + item + "]"));

    return permissions.stream()
        .collect(Collectors.toMap(PermissionPo::getPermissionCode, PermissionPo::getId));
  }

  private List<RolePermissionInfo> getRolePermissionInfo(List<RolePo> roleList) {
    if (CollectionUtils.isEmpty(roleList)) {
      return Lists.newArrayList();
    }

    List<String> consIds = roleList.stream().map(RolePo::getConsortiumId)
        .collect(Collectors.toList());
    List<ConsortiumPo> cons = consortiumDas.findByIds(consIds);
    Map<String, String> consIdNameMap = cons.stream()
        .collect(Collectors.toMap(ConsortiumPo::getConsortiumId, ConsortiumPo::getName));

    List<PermissionPo> allPerms = permissionDas.findAll();
    Map<Long, String> allPermIdCodeMap = allPerms.stream()
        .collect(Collectors.toMap(PermissionPo::getId, PermissionPo::getPermissionCode));
    List<String> roleIds = roleList.stream().map(RolePo::getId).collect(Collectors.toList());
    List<RolePermissionPo> rolePerms = permissionDas.findRolePermissionsByRoleIds(roleIds);
    Map<String, List<String>> roleIdPermCodeMap = rolePerms.stream()
        .collect(Collectors.groupingBy(RolePermissionPo::getRoleId,
            Collectors.mapping(item -> allPermIdCodeMap.get(item.getPermissionId()),
                Collectors.toList())));

    return roleList.stream().map(item -> RolePermissionInfo.builder()
        .id(item.getId())
        .consId(item.getConsortiumId())
        .consName(consIdNameMap.get(item.getConsortiumId()))
        .createTime(
            DateTimeUtils.formatTime(item.getCreateTime(), DateTimeUtils.FORMAT_yyyyMMddHHmmss))
        .name(item.getRoleName())
        .permissions(roleIdPermCodeMap.get(item.getId()))
        .build()).collect(Collectors.toList());
  }
}
