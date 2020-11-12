package org.treegraph.baas.core.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.treegraph.baas.api.dto.Role;
import org.treegraph.baas.api.dto.RoleDetailInfo;
import org.treegraph.baas.api.dto.RoleInfo;
import org.treegraph.baas.api.dto.UserInfo;
import org.treegraph.baas.api.dto.request.AcceptInviteRequest;
import org.treegraph.baas.api.dto.request.CreateUserRequest;
import org.treegraph.baas.api.dto.request.DeleteUserRequest;
import org.treegraph.baas.api.dto.request.InviteUserRequest;
import org.treegraph.baas.api.dto.request.ListUserRequest;
import org.treegraph.baas.api.dto.request.ModifyPwdRequest;
import org.treegraph.baas.api.dto.request.ModifyUserRequest;
import org.treegraph.baas.api.dto.request.QueryUserRequest;
import org.treegraph.baas.api.dto.request.UnfrozenUserRequest;
import org.treegraph.baas.api.dto.response.ListUserResponse;
import org.treegraph.baas.common.enums.IntTypeEnum;
import org.treegraph.baas.common.util.WebHelper;
import org.treegraph.baas.core.constant.InviteConstant;
import org.treegraph.baas.core.constant.SplitterConstant;
import org.treegraph.baas.core.das.ConsortiumDas;
import org.treegraph.baas.core.das.InviteUserDas;
import org.treegraph.baas.core.das.RoleDas;
import org.treegraph.baas.core.das.UserInfoDas;
import org.treegraph.baas.core.domain.ConsortiumPo;
import org.treegraph.baas.core.domain.RolePo;
import org.treegraph.baas.core.domain.UserInfoPo;
import org.treegraph.baas.core.domain.UserInvitePo;
import org.treegraph.baas.core.domain.UserOrganizationPo;
import org.treegraph.baas.core.domain.UserRoleInvitePo;
import org.treegraph.baas.core.domain.UserRolePo;
import org.treegraph.baas.core.enums.UserEnum;
import org.treegraph.baas.core.exception.AlertException;
import org.treegraph.baas.core.service.TokenService;
import org.treegraph.baas.core.service.UserService;
import org.treegraph.baas.core.service.bo.OrgBo;
import org.treegraph.baas.core.service.bo.UserInfoBo;
import org.treegraph.baas.core.service.bo.UserOrgBo;
import org.treegraph.baas.core.util.DateTimeUtils;

import static org.treegraph.baas.core.util.DateTimeUtils.FORMAT_yyyyMMddHHmmss;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Functions;
import com.google.common.collect.Lists;

import lombok.AllArgsConstructor;

@Validated
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserInfoDas userInfoDas;

  private final RoleDas roleDas;

  private final InviteUserDas inviteUserDas;

  private final ConsortiumDas consortiumDas;

  private final PasswordService passwordService;

  private final BuildService buildService;

  private final EmailService emailService;

  private final TokenService tokenService;

  public void createUser(CreateUserRequest req) {
    Optional<UserInfoPo> userInfoOp = userInfoDas.findByAccount(req.getAccount());
    Assert.isTrue(!userInfoOp.isPresent(), "用户账号不可重复[" + req.getAccount() + "]");
    List<Role> roles = validateRoleInfo(req.getRoles());
    List<OrgBo> orgs = req.getRoles().stream().map(item -> OrgBo.builder()
        .consortiumId(item.getConsId())
        .organizationName(item.getOrgName())
        .build()).collect(Collectors.toList());
    UserInfoBo user = buildService
        .buildUser(req.getAccount(), req.getName(), req.getPhone(), req.getEmail(),
            req.getPwd(), roles, orgs);

    userInfoDas.createUser(user.getUser(), user.getUserRoles(), user.getUserOrgs());
  }

  public String inviteUser(InviteUserRequest req) {
    UserInfoPo userInfo = userInfoDas.findByAccount(req.getAccount())
        .orElseThrow(() -> new AlertException("user.not.found"));
    validateRoleInfo(req.getRoles());
    List<UserRoleInvitePo> userRoleInvites = Lists.newArrayList();
    req.getRoles().forEach(role -> {
      List<UserRoleInvitePo> userRoleInviteList = role.getRoleIds().stream()
          .map(roleId -> UserRoleInvitePo.builder()
              .inviteId(null)
              .userId(userInfo.getId())
              .roleId(roleId)
              .consortiumId(role.getConsId())
              .organizationName(role.getOrgName())
              .status(UserEnum.InviteUserRoleStatus.NOT_ACCEPT.getCode())
              .build())
          .collect(Collectors.toList());
      userRoleInvites.addAll(userRoleInviteList);
    });

    String oper = WebHelper.getUserId();
    LocalDateTime now = LocalDateTime.now();
    UserInvitePo userInvite = UserInvitePo.builder()
        .inviter(oper)
        .accepter(userInfo.getId())
        .createTime(now)
        .build();
    userInfoDas.inviteUser(userInvite, userRoleInvites);

    emailService.sendEmail(Lists.newArrayList(userInfo.getEmail()), InviteConstant.TITLE,
        userInvite.getToken());

    UserInvitePo updateInviteUser = UserInvitePo.builder()
        .id(userInvite.getId())
        .inviteTime(LocalDateTime.now())
        .build();
    userInfoDas.updateInviteUserById(updateInviteUser);

    return userInvite.getToken();
  }

  public boolean acceptInvite(AcceptInviteRequest req) {
    UserInvitePo userInvite = tokenService.checkInviteToken(req.getToken());
    List<UserRoleInvitePo> userRoleInvites = inviteUserDas.findByInviteId(userInvite.getId());

    Map<String, List<String>> roleInfoMap = userRoleInvites.stream()
        .collect(
            Collectors.groupingBy(item -> item.getConsortiumId() + "," + item.getOrganizationName(),
                Collectors.mapping(UserRoleInvitePo::getRoleId, Collectors.toList())));
    List<RoleInfo> roleInfoList = roleInfoMap.entrySet().stream().map(item -> {
      List<String> paras = SplitterConstant.COMMA_SPLITTER.splitToList(item.getKey());
      return RoleInfo.builder()
          .consId(paras.get(0))
          .orgName(paras.get(1))
          .roleIds(item.getValue())
          .build();
    }).collect(Collectors.toList());
    List<Role> roles = validateRoleInfo(roleInfoList);

    List<UserRolePo> userRoles = buildService.buildUserRoles(userInvite.getAccepter(), roles);
    return userInfoDas.createUserRoles(userRoles);
  }

  public String acceptInvite(InviteUserRequest req) {
    UserInfoPo userInfo = userInfoDas.findByAccount(req.getAccount())
        .orElseThrow(() -> new AlertException("user.not.found"));
    List<Role> roles = validateRoleInfo(req.getRoles());
    List<UserRoleInvitePo> userRoleInvites = roles.stream().map(item -> UserRoleInvitePo.builder()
        .inviteId(null)
        .userId(userInfo.getId())
        .roleId(item.getRoleId())
        .build())
        .collect(Collectors.toList());

    String oper = WebHelper.getUserId();
    LocalDateTime now = LocalDateTime.now();
    UserInvitePo userInvite = UserInvitePo.builder()
        .inviter(oper)
        .accepter(userInfo.getId())
        .createTime(now)
        .build();
    userInfoDas.inviteUser(userInvite, userRoleInvites);

    emailService.sendEmail(Lists.newArrayList(userInfo.getEmail()), InviteConstant.TITLE,
        userInvite.getToken());

    UserInvitePo updateInviteUser = UserInvitePo.builder()
        .id(userInvite.getId())
        .inviteTime(LocalDateTime.now())
        .build();
    userInfoDas.updateInviteUserById(updateInviteUser);

    return userInvite.getToken();
  }

  public boolean deleteUser(DeleteUserRequest req) {
    String userId = req.getId();
    UserInfoPo userInfo = userInfoDas.findById(userId)
        .orElseThrow(() -> new AlertException("user.not.found"));
    UserInfoPo user = UserInfoPo.builder()
        .id(userId)
        .account("_0_" + userInfo.getAccount() + "_0_")
        .deleteFlag(IntTypeEnum.TRUE.getValue())
        .updateTime(LocalDateTime.now())
        .updateBy(WebHelper.getUserId())
        .build();

    List<UserRolePo> userRoles = roleDas.findUserRolesByUserId(userId);
    List<Long> userRoleIds = userRoles.stream().map(UserRolePo::getId).collect(Collectors.toList());

    return userInfoDas.delUser(user, userRoleIds);
  }

  public List<UserInfo> queryUser(QueryUserRequest req) {
    String account = req.getAccount();
    List<UserInfoPo> users = userInfoDas.findByAccountLike(account);

    return users.stream().map(item -> UserInfo.builder().id(item.getId())
        .account(item.getAccount())
        .name(item.getName()).build()).collect(Collectors.toList());
  }

  public ListUserResponse listUser(ListUserRequest req) {
    PageHelper.startPage(req.getCurrent(), req.getPageSize());
    List<UserInfoPo> userList = userInfoDas.findAll(req.getAccount(), req.getName(), req.getEmail(),
        req.getPhone(), req.getStatus());
    PageInfo<UserInfoPo> pageInfo = new PageInfo<>(userList);

    List<UserInfoPo> list = pageInfo.getList();
    List<String> userIds = list.stream().map(UserInfoPo::getId).collect(Collectors.toList());
    Map<String, List<RoleDetailInfo>> roleMap = getRolesByUserIds(userIds);

    List<UserInfo> users = list.stream().map(item -> UserInfo.builder()
        .id(item.getId())
        .account(item.getAccount())
        .name(item.getName())
        .phone(item.getPhone())
        .email(item.getEmail())
        .status(item.getStatus())
        .createTime(DateTimeUtils.formatTime(item.getCreateTime(), FORMAT_yyyyMMddHHmmss))
        .roles(roleMap.get(item.getId()))
        .build())
        .collect(Collectors.toList());

    return ListUserResponse.builder()
        .current(req.getCurrent())
        .pageSize(req.getPageSize())
        .total(pageInfo.getTotal())
        .list(users)
        .build();
  }

  public boolean modifyPwd(ModifyPwdRequest req) {
    String userId = req.getId();
    String oldPwd = req.getOldPwd();
    String newPwd = req.getNewPwd();

    UserInfoPo user = userInfoDas.findById(userId)
        .orElseThrow(() -> new AlertException("user.not.found"));
    boolean matches = passwordService.matches(oldPwd, user.getPassword());
    Assert.isTrue(matches, "旧密码不正确");

    UserInfoPo updateUser = UserInfoPo.builder()
        .id(userId)
        .password(passwordService.bcryptEncoder(newPwd))
        .updateTime(LocalDateTime.now())
        .updateBy(WebHelper.getUserId())
        .build();
    return userInfoDas.updateUser(updateUser);
  }

  public void modifyUser(ModifyUserRequest req) {
    UserInfoPo updateUser = UserInfoPo.builder()
        .id(req.getId())
        .password(req.getPwd() != null ? passwordService.bcryptEncoder(req.getPwd()) : null)
        .account(req.getAccount())
        .name(req.getName())
        .phone(req.getPhone())
        .updateTime(LocalDateTime.now())
        .updateBy(WebHelper.getUserId())
        .build();

    List<Role> reqRoles = validateRoleInfo(req.getRoles());
    List<String> reqRoleIds = reqRoles.stream().map(Role::getRoleId).collect(Collectors.toList());
    List<UserRolePo> dbUserRoles = roleDas.findUserRolesByUserId(req.getId());
    List<String> dbRoleIds = dbUserRoles.stream().map(UserRolePo::getRoleId)
        .collect(Collectors.toList());
    List<String> intersect = Lists.newArrayList();
    intersect.addAll(reqRoleIds);
    intersect.retainAll(dbRoleIds);
    reqRoleIds.removeAll(intersect);
    dbRoleIds.removeAll(intersect);

    LocalDateTime now = LocalDateTime.now();
    String operUser = WebHelper.getUserId();
    Map<String, String> roleMap = reqRoles.stream()
        .collect(Collectors.toMap(Role::getRoleId, Role::getConsId));
    List<UserRolePo> toAddUserRoles = reqRoleIds.stream().map(item -> UserRolePo.builder()
        .userId(req.getId())
        .roleId(item)
        .consortiumId(roleMap.get(item))
        .deleteFlag(IntTypeEnum.FALSE.getValue())
        .createTime(now)
        .createBy(operUser)
        .updateTime(now)
        .updateBy(operUser)
        .build()).collect(Collectors.toList());
    Map<String, UserRolePo> dbUserRoleMap = dbUserRoles.stream()
        .collect(Collectors.toMap(UserRolePo::getRoleId, Functions.identity()));
    List<Long> toDelUserRoleIds = dbRoleIds.stream().map(item -> dbUserRoleMap.get(item).getId())
        .collect(Collectors.toList());

    List<UserOrganizationPo> reqUserOrgs = req.getRoles().stream()
        .map(item -> UserOrganizationPo.builder()
            .userId(req.getId())
            .consortiumId(item.getConsId())
            .organizationName(item.getOrgName())
            .build()).collect(Collectors.toList());
    List<UserOrganizationPo> dbUserOrgs = userInfoDas
        .findUserOrgsByUserIds(Lists.newArrayList(req.getId()));
    UserOrgBo userOrgBo = buildService.buildModifyUserOrg(dbUserOrgs, reqUserOrgs);

    userInfoDas.modifyUser(req.getId(), updateUser, toDelUserRoleIds, toAddUserRoles,
        userOrgBo.getToDelUserOrgIds(), userOrgBo.getToAddUserOrgs());
  }

  public void unfrozenUser(UnfrozenUserRequest unfrozenUserRequest) {
    String accountNo = unfrozenUserRequest.getAccountNo();
    UserInfoPo user = userInfoDas.findByAccount(accountNo)
        .orElseThrow(() -> new AlertException("用户不存在"));
    Assert.isTrue(UserEnum.Status.isFrozen(user.getStatus()), "该用户未冻结，无需解冻");

    UserInfoPo userUpdate = UserInfoPo.builder().id(user.getId()).tryTimes(0)
        .status(UserEnum.Status.AVAILABLE.getCode()).build();
    userInfoDas.updateUser(userUpdate);
  }

  private List<Role> validateRoleInfo(List<RoleInfo> roles) {
    if (CollectionUtils.isEmpty(roles)) {
      return Lists.newArrayList();
    }

    List<String> consIds = roles.stream().map(RoleInfo::getConsId).collect(Collectors.toList());
    List<RolePo> dbRoles = roleDas.findByConsIds(consIds);
    Map<String, List<String>> dbConsIdRoleIdMap = dbRoles.stream().collect(
        Collectors.groupingBy(RolePo::getConsortiumId,
            Collectors.mapping(RolePo::getId, Collectors.toList())));

    roles.forEach(item -> {
      List<String> dbRoleIdList = dbConsIdRoleIdMap.get(item.getConsId());
      Assert.notEmpty(dbRoleIdList, "给用户所授角色,联盟无此角色[" + item.getConsId() + "]");
      List<String> reqRoleIdList = item.getRoleIds();
      reqRoleIdList
          .forEach(
              roleId -> Assert.isTrue(dbRoleIdList.contains(roleId), "给用户所授角色无效[" + roleId + "]"));
    });

    List<Role> userRoleList = Lists.newArrayList();
    roles.forEach(item -> {
      List<Role> userRoles = item.getRoleIds().stream()
          .map(roleId -> Role.builder().consId(item.getConsId()).roleId(roleId).build())
          .collect(Collectors.toList());
      userRoleList.addAll(userRoles);
    });

    return userRoleList;
  }

  private Map<String, List<RoleDetailInfo>> getRolesByUserIds(List<String> userIds) {
    List<UserRolePo> userRoles = roleDas.findUserRolesByUserIds(userIds);
    List<String> allRoleIds = userRoles.stream().map(UserRolePo::getRoleId)
        .collect(Collectors.toList());
    List<RolePo> allRolePos = roleDas.findByIds(allRoleIds);
    Map<String, RolePo> allRoleMap = allRolePos.stream()
        .collect(Collectors.toMap(RolePo::getId, Functions.identity()));

    List<String> allConsIds = allRolePos.stream().map(RolePo::getConsortiumId)
        .collect(Collectors.toList());
    List<ConsortiumPo> allCons = consortiumDas.findByIds(allConsIds);
    Map<String, ConsortiumPo> allConsMap = allCons.stream()
        .collect(Collectors.toMap(ConsortiumPo::getConsortiumId, Functions.identity()));

    List<UserOrganizationPo> allUserOrgs = userInfoDas.findUserOrgsByUserIds(userIds);
    Map<String, List<UserOrganizationPo>> allOrgsMap = allUserOrgs.stream()
        .collect(Collectors.groupingBy(item -> item.getUserId() + "," + item.getConsortiumId()));

    return userRoles.stream().collect(Collectors.groupingBy(UserRolePo::getUserId)).entrySet()
        .stream()
        .collect(Collectors.toMap(Map.Entry::getKey, item -> {
          List<String> roleIds = item.getValue().stream().map(UserRolePo::getRoleId)
              .collect(Collectors.toList());
          List<RolePo> rolePos = roleIds.stream().map(allRoleMap::get)
              .collect(Collectors.toList());
          Map<String, List<RolePo>> rolePoMap = rolePos.stream()
              .collect(Collectors.groupingBy(RolePo::getConsortiumId));

          return rolePoMap.entrySet().stream().map(entry -> {
            ConsortiumPo cons = allConsMap.get(entry.getKey());
            String consName = cons != null ? cons.getName() : null;
            String key = item.getKey() + "," + entry.getKey();
            List<UserOrganizationPo> orgs = allOrgsMap.get(key);
            String orgName =
                CollectionUtils.isNotEmpty(orgs) ? orgs.get(0).getOrganizationName() : null;
            List<Role> roles = entry.getValue().stream()
                .map(role -> Role.builder()
                    .roleId(role.getId())
                    .roleName(role.getRoleName())
                    .build())
                .collect(Collectors.toList());

            return RoleDetailInfo.builder()
                .consId(entry.getKey())
                .consName(consName)
                .orgName(orgName)
                .roles(roles)
                .build();
          }).collect(Collectors.toList());
        }));
  }
}
