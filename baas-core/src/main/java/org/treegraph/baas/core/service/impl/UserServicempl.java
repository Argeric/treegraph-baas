package org.treegraph.baas.core.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
import org.treegraph.baas.api.dto.response.ListUserResponse;
import org.treegraph.baas.common.enums.IntTypeEnum;
import org.treegraph.baas.common.util.WebHelper;
import org.treegraph.baas.core.constant.InviteConsts;
import org.treegraph.baas.core.constant.SplitterConsts;
import org.treegraph.baas.core.das.ConsortiumDas;
import org.treegraph.baas.core.das.InviteUserDas;
import org.treegraph.baas.core.das.RoleDas;
import org.treegraph.baas.core.das.UserInfoDas;
import org.treegraph.baas.core.domain.ConsortiumPo;
import org.treegraph.baas.core.domain.InviteUserPo;
import org.treegraph.baas.core.domain.InviteUserRolePo;
import org.treegraph.baas.core.domain.RolePo;
import org.treegraph.baas.core.domain.UserInfoPo;
import org.treegraph.baas.core.domain.UserOrganizationPo;
import org.treegraph.baas.core.domain.UserRolePo;
import org.treegraph.baas.core.enums.UserEnum;
import org.treegraph.baas.core.exception.AlertException;
import org.treegraph.baas.core.service.TokenService;
import org.treegraph.baas.core.service.UserService;
import org.treegraph.baas.core.service.bo.UserInfoBo;
import org.treegraph.baas.core.service.bo.UserOrgBo;
import org.treegraph.baas.core.util.CollectionUtils;
import org.treegraph.baas.core.util.DateTimeUtils;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Functions;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import lombok.AllArgsConstructor;

@Validated
@Service
@AllArgsConstructor
public class UserServicempl implements UserService {

    private UserInfoDas userInfoDas;
    
    private RoleDas roleDas;
    
    private InviteUserDas inviteUserDas;
    
    private ConsortiumDas consortiumDas;
	
    private PasswordService passwordService;
	
    private BuildService buildService;
    
    private EmailService emailService;
    
    private TokenService tokenService;
    
	public boolean createUser(CreateUserRequest req) {
		Optional<UserInfoPo> userInfoOp = userInfoDas.findByAccount(req.getAccount());
		Assert.isTrue(!userInfoOp.isPresent(), "用户账号不可重复[" + req.getAccount() + "]");
		List<String> roleIds = validateRoleIds(req.getRoles());
		UserInfoBo user = buildService.buildUser(req.getAccount(), req.getName(), req.getPhone(), req.getEmail(),
				req.getPwd(), roleIds);
		
		LocalDateTime now = LocalDateTime.now();
		List<UserOrganizationPo> userOrgs = req.getRoles().stream().map(item -> UserOrganizationPo.builder()
				.userId(user.getUser().getId())
				.consortiumId(item.getConsId())
				.organizationName(item.getOrgName())
				.deleteFlag(IntTypeEnum.FALSE.getValue())
				.createTime(now)
				.updateTime(now)
				.build()).collect(Collectors.toList());

		return userInfoDas.createUser(user.getUser(), user.getUserRoles(), userOrgs);
	}
	
	public String inviteUser(InviteUserRequest req) {
		UserInfoPo userInfo = userInfoDas.findByAccount(req.getAccount())
				.orElseThrow(() -> new AlertException("user.not.found"));
		validateRoleIds(req.getRoles());
		List<InviteUserRolePo> inviteUserRoles = Lists.newArrayList();
		req.getRoles().stream().forEach(role -> {
			List<InviteUserRolePo> inviteUserRoleList = role.getRoleIds().stream()
					.map(roleId -> InviteUserRolePo.builder()
							.inviteId(null)
							.userId(userInfo.getId())
							.roleId(roleId)
							.consId(role.getConsId())
							.orgName(role.getOrgName())
							.status(UserEnum.InviteUserRoleStatus.NOT_ACCEPT.getCode())
							.build())
					.collect(Collectors.toList());
			inviteUserRoles.addAll(inviteUserRoleList);
		});
		
		String oper = WebHelper.getUserId();
		LocalDateTime now = LocalDateTime.now();
		InviteUserPo inviteUser = InviteUserPo.builder()
				.inviter(oper)
				.accepter(userInfo.getId())
				.createTime(now)
				.build();
		userInfoDas.inviteUser(inviteUser, inviteUserRoles);
		
		emailService.sendEmail(Lists.newArrayList(userInfo.getEmail()), InviteConsts.TITLE, inviteUser.getToken());
		
		InviteUserPo updateInviteUser = InviteUserPo.builder()
				.id(inviteUser.getId())
				.inviteTime(LocalDateTime.now())
				.build();
		userInfoDas.updateInviteUserById(updateInviteUser);
		
		return inviteUser.getToken();
	}
	
	public boolean acceptInvite(AcceptInviteRequest req) {
		InviteUserPo invitUser = tokenService.checkInvitToken(req.getToken());
		List<InviteUserRolePo> inviteUserRoles = inviteUserDas.findByInviteId(invitUser.getId());

		Map<String, List<String>> roleInfoMap = inviteUserRoles.stream()
				.collect(Collectors.groupingBy(item -> item.getConsId() + "," + item.getOrgName(),
						Collectors.mapping(InviteUserRolePo::getRoleId, Collectors.toList())));
		List<RoleInfo> roleInfos = roleInfoMap.entrySet().stream().map(item -> {
			List<String> paras = SplitterConsts.COMMA_SPLITTER.splitToList(item.getKey());
			return RoleInfo.builder()
					.consId(paras.get(0))
					.orgName(paras.get(1))
					.roleIds(item.getValue())
					.build();
		}).collect(Collectors.toList());
		List<String> roleIds = validateRoleIds(roleInfos);
		
		List<UserRolePo> userRoles = buildService.buildUserRoles(invitUser.getAccepter(), roleIds);
		return userInfoDas.createUserRoles(userRoles);
	}
	
	public String acceptInvite(InviteUserRequest req) {
		UserInfoPo userInfo = userInfoDas.findByAccount(req.getAccount())
				.orElseThrow(() -> new AlertException("user.not.found"));
		List<String> roleIds = validateRoleIds(req.getRoles());
		List<InviteUserRolePo> inviteUserRoles = roleIds.stream()
				.map(item -> InviteUserRolePo.builder().inviteId(null).userId(userInfo.getId()).roleId(item).build())
				.collect(Collectors.toList());
		
		String oper = WebHelper.getUserId();
		LocalDateTime now = LocalDateTime.now();
		InviteUserPo inviteUser = InviteUserPo.builder()
				.inviter(oper)
				.accepter(userInfo.getId())
				.createTime(now)
				.build();
		userInfoDas.inviteUser(inviteUser, inviteUserRoles);
		
		emailService.sendEmail(Lists.newArrayList(userInfo.getEmail()), InviteConsts.TITLE, inviteUser.getToken());
		
		InviteUserPo updateInviteUser = InviteUserPo.builder()
				.id(inviteUser.getId())
				.inviteTime(LocalDateTime.now())
				.build();
		userInfoDas.updateInviteUserById(updateInviteUser);
		
		return inviteUser.getToken();
	}
	
	public boolean deleteUser(DeleteUserRequest req){
		String userId = req.getId();
		UserInfoPo userInfo = userInfoDas.findById(userId).orElseThrow(() -> new AlertException("user.not.found"));
		UserInfoPo user = UserInfoPo.builder()
				.id(userId)
				.account("_0_"+userInfo.getAccount()+"_0_")
				.deleteFlag(IntTypeEnum.TRUE.getValue())
				.updateTime(LocalDateTime.now())
				.updateBy(WebHelper.getUserId())
				.build();
		return userInfoDas.delUser(user);
	}
	
	public List<UserInfo> queryUser(QueryUserRequest req) {
		String account = req.getAccount();
		List<UserInfoPo> users = userInfoDas.findByAccountLike(account);
		
		List<UserInfo> userList = users.stream().map(item -> UserInfo.builder().id(item.getId())
				.account(item.getAccount())
				.name(item.getName()).build()).collect(Collectors.toList());

		return userList;
	}
	
	public ListUserResponse listUser(ListUserRequest req){
		PageHelper.startPage(req.getCurrent(), req.getPageSize());
		List<UserInfoPo> userList = userInfoDas.findAll(req.getAccount(), req.getName(), req.getEmail(),
				req.getPhone());
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
						.createTime(DateTimeUtils.formatTime(item.getCreateTime(),DateTimeUtils.FORMAT_yyyyMMddHHmmss))
						.roles(roleMap.get(item.getId()))
						.build())
				.collect(Collectors.toList());
		
		ListUserResponse listUserResponse = ListUserResponse.builder()
				.current(req.getCurrent())
				.pageSize(req.getPageSize())
				.total(pageInfo.getTotal())
				.list(users)
				.build();
		
		return listUserResponse;
	}
	
	public boolean modifyPwd(ModifyPwdRequest req){
		String userId = req.getId();
		String oldPwd = req.getOldPwd();
		String newPwd = req.getNewPwd();
		
		UserInfoPo user = userInfoDas.findById(userId).orElseThrow(() -> new AlertException("user.not.found"));
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
	
	public boolean modifyUser(ModifyUserRequest req){
		UserInfoPo updateUser = UserInfoPo.builder()
				.id(req.getId())
				.password(req.getPwd() != null ? passwordService.bcryptEncoder(req.getPwd()) : null)
				.account(req.getAccount())
				.name(req.getName())
				.phone(req.getPhone())
				.updateTime(LocalDateTime.now())
				.updateBy(WebHelper.getUserId())
				.build();
		
		List<String> reqRoleIds = validateRoleIds(req.getRoles());
		List<UserRolePo> dbUserRoles = roleDas.findUserRolesByUserId(req.getId());
		List<String> dbRoleIds = dbUserRoles.stream().map(UserRolePo::getRoleId).collect(Collectors.toList());
		List<String> interset = Lists.newArrayList();
		interset.addAll(reqRoleIds);
		interset.retainAll(dbRoleIds);
		reqRoleIds.removeAll(interset);
		dbRoleIds.removeAll(interset);
		
		LocalDateTime now = LocalDateTime.now();
		String operUser = WebHelper.getUserId();
		List<UserRolePo> toAddUserRoles = reqRoleIds.stream().map(item -> UserRolePo.builder()
				.userId(req.getId())
				.roleId(item)
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
		
		List<UserOrganizationPo> reqUserOrgs = req.getRoles().stream().map(item -> UserOrganizationPo.builder()
				.userId(req.getId())
				.consortiumId(item.getConsId())
				.organizationName(item.getOrgName())
				.build()).collect(Collectors.toList());
		UserOrgBo userOrgBo = modifyUserOrg(req.getId(), reqUserOrgs);
		
		return userInfoDas.modifyUser(req.getId(), updateUser, toDelUserRoleIds, toAddUserRoles,
				userOrgBo.getToDelUserOrgIds(), userOrgBo.getToAddUserOrgs());
	}

	private List<String> validateRoleIds(List<RoleInfo> roles) {
		if(CollectionUtils.isEmpty(roles)) {
			return Lists.newArrayList();
		}
		
		List<String> consIds = roles.stream().map(RoleInfo::getConsId).collect(Collectors.toList());
		List<RolePo> dbRoles = roleDas.findByConsIds(consIds);
		Map<String, List<String>> dbConsIdRoldIdMap = dbRoles.stream().collect(
				Collectors.groupingBy(RolePo::getConsortiumId, Collectors.mapping(RolePo::getId, Collectors.toList())));
		
		roles.stream().forEach(item -> {
			List<String> dbRoleIdList = dbConsIdRoldIdMap.get(item.getConsId());
			Assert.notEmpty(dbRoleIdList, "给用户所授角色,联盟无此角色[" + item.getConsId() + "]");
			List<String> reqRoleIdList = item.getRoleIds();
			reqRoleIdList.stream()
					.forEach(roleId -> Assert.isTrue(dbRoleIdList.contains(roleId), "给用户所授角色无效[" + roleId + "]"));
		});
		
		List<String> roleIds = Lists.newArrayList();
		roles.stream().forEach(item -> roleIds.addAll(item.getRoleIds()));
		
		return roleIds;
	}
	
	private Map<String, List<RoleDetailInfo>> getRolesByUserIds(List<String> userIds) {
		List<UserRolePo> userRoles = roleDas.findUserRolesByUserIds(userIds);
		List<String> allRoleIds = userRoles.stream().map(UserRolePo::getRoleId).collect(Collectors.toList());
		List<RolePo> allRolePos = roleDas.findByIds(allRoleIds);
		Map<String, RolePo> allRoleMap = allRolePos.stream().collect(Collectors.toMap(RolePo::getId, Functions.identity()));
		
		List<String> allConsIds = allRolePos.stream().map(RolePo::getConsortiumId).collect(Collectors.toList());
		List<ConsortiumPo> allCons = consortiumDas.findByIds(allConsIds);
		Map<String, ConsortiumPo> allConsMap = allCons.stream().collect(Collectors.toMap(ConsortiumPo::getId, Functions.identity()));
		
		List<UserOrganizationPo> allUserOrgs = userInfoDas.findUserOrgsByUserIds(userIds);
		Map<String, List<UserOrganizationPo>> allOrgsMap = allUserOrgs.stream()
				.collect(Collectors.groupingBy(item -> item.getUserId() + "," + item.getConsortiumId()));
		
		return userRoles.stream().collect(Collectors.groupingBy(UserRolePo::getUserId)).entrySet().stream()
				.collect(Collectors.toMap(item -> item.getKey(), item -> {
					List<String> roleIds = item.getValue().stream().map(UserRolePo::getRoleId)
							.collect(Collectors.toList());
					List<RolePo> rolePos = roleIds.stream().map(roleId -> allRoleMap.get(roleId)).collect(Collectors.toList());
					Map<String, List<RolePo>> rolePoMap = rolePos.stream()
							.collect(Collectors.groupingBy(RolePo::getConsortiumId));

					return rolePoMap.entrySet().stream().map(entry -> {
						ConsortiumPo cons = allConsMap.get(entry.getKey());
						String consName = cons != null ? cons.getName() : null;
						String key = item.getKey()+","+entry.getKey();
						List<UserOrganizationPo> orgs = allOrgsMap.get(key);
						String orgName = CollectionUtils.isNotEmpty(orgs) ? orgs.get(0).getOrganizationName() : null;
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
	
	private UserOrgBo modifyUserOrg(String userId, List<UserOrganizationPo> reqUserOrgs) {
		List<UserOrganizationPo> dbUserOrgs = userInfoDas.findUserOrgsByUserIds(Lists.newArrayList(userId));
		Set<String> dbOrgs = dbUserOrgs.stream().map(item -> buildService.buildUserOrgIdentifier(item))
				.collect(Collectors.toSet());
		Set<String> reqOrgs = reqUserOrgs.stream().map(item -> buildService.buildUserOrgIdentifier(item))
				.collect(Collectors.toSet());
		
		Set<String> intersetOrgs = Sets.newHashSet();
		intersetOrgs.addAll(dbOrgs);
		intersetOrgs.retainAll(reqOrgs);
		reqOrgs.removeAll(intersetOrgs);
		dbOrgs.removeAll(intersetOrgs);
		
		List<UserOrganizationPo> toAddUserOrgs = reqOrgs.stream().map(item -> buildService.buildUserOrg(item))
				.collect(Collectors.toList());
		Map<String, UserOrganizationPo> dbOrgMap = dbUserOrgs.stream()
				.collect(Collectors.toMap(item -> buildService.buildUserOrgIdentifier(item), Functions.identity()));
		List<Long> toDelUserOrgIds = dbOrgs.stream().map(item -> dbOrgMap.get(item).getId())
				.collect(Collectors.toList());

		return UserOrgBo.builder()
				.toAddUserOrgs(toAddUserOrgs)
				.toDelUserOrgIds(toDelUserOrgIds)
				.build();
	}
}
