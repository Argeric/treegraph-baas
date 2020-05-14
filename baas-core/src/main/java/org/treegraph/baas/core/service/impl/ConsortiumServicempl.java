package org.treegraph.baas.core.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.treegraph.baas.api.dto.ConsAdminInfo;
import org.treegraph.baas.api.dto.ConsInfo;
import org.treegraph.baas.api.dto.request.AddNodesRequest;
import org.treegraph.baas.api.dto.request.CreateBlockchainRequest;
import org.treegraph.baas.api.dto.request.CreateConsAdminRequest;
import org.treegraph.baas.api.dto.request.CreateConsRequest;
import org.treegraph.baas.api.dto.request.DeleteConsRequest;
import org.treegraph.baas.api.dto.request.GetDeployScriptRequest;
import org.treegraph.baas.api.dto.request.ListConsRequest;
import org.treegraph.baas.api.dto.request.ModifyConsRequest;
import org.treegraph.baas.api.dto.response.ListConsResponse;
import org.treegraph.baas.common.enums.IntTypeEnum;
import org.treegraph.baas.common.util.WebHelper;
import org.treegraph.baas.core.das.ConsortiumDas;
import org.treegraph.baas.core.das.RoleDas;
import org.treegraph.baas.core.das.UserInfoDas;
import org.treegraph.baas.core.domain.ConsortiumPo;
import org.treegraph.baas.core.domain.RolePo;
import org.treegraph.baas.core.domain.UserInfoPo;
import org.treegraph.baas.core.domain.UserRolePo;
import org.treegraph.baas.core.enums.ConsortiumEnum;
import org.treegraph.baas.core.enums.UserEnum;
import org.treegraph.baas.core.service.ConsortiumService;
import org.treegraph.baas.core.service.bo.RoleInfoBo;
import org.treegraph.baas.core.service.bo.UserInfoBo;
import org.treegraph.baas.core.util.CollectionUtils;
import org.treegraph.baas.core.util.DateTimeUtils;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;

import lombok.AllArgsConstructor;

@Validated
@Service
@AllArgsConstructor
public class ConsortiumServicempl implements ConsortiumService {
    
    private ConsortiumDas consortiumDas;
    
    private UserInfoDas userInfoDas;
    
    private RoleDas roleDas;
    
    private BuildService buildService;
   
	public ListConsResponse listCons(ListConsRequest req) {
		
		PageHelper.startPage(req.getCurrent(), req.getPageSize());
		List<ConsortiumPo> consList = consortiumDas.findAll();
		PageInfo<ConsortiumPo> pageInfo = new PageInfo<>(consList);
		
		List<ConsortiumPo> conses = pageInfo.getList();
		List<ConsInfo> userInfoList = conses.stream().map(item -> ConsInfo.builder()
				.id(item.getId())
				.name(item.getName())
				.remark(item.getDescription())
				.contact(item.getContactName())
				.phone(item.getContactPhone())
				.email(item.getContactEmail())
				.avatar(new String(item.getAvatar()))
				.createUser(item.getCreateBy())
				.createTime(DateTimeUtils.formatTime(item.getCreateTime(), DateTimeUtils.FORMAT_yyyyMMddHHmmss))
				.status(item.getStatus())
				.step(item.getEstablishStep())
				.build()).collect(Collectors.toList());
		
		ListConsResponse listConsResponse = ListConsResponse.builder()
				.total(pageInfo.getTotal())
				.list(userInfoList)
				.build();
		
		return listConsResponse;
	}
	
	public boolean createCons(CreateConsRequest req) {
		
		List<ConsortiumPo> consList = consortiumDas.findByName(req.getName());
		Assert.isTrue(CollectionUtils.isEmpty(consList), "新创建联盟名称已被使用["+req.getName()+"]");
		
		LocalDateTime now = LocalDateTime.now();
		String oper = WebHelper.getUserId();
		ConsortiumPo cons = ConsortiumPo.builder()
				.id(UUID.randomUUID().toString())
				.name(req.getName())
				.avatar(req.getAvatar() != null ? req.getAvatar().getBytes() : null)
				.description(req.getRemark())
				.contactName(req.getContact())
				.contactEmail(req.getEmail())
				.contactPhone(req.getPhone())
				.status(ConsortiumEnum.Status.ESTABLISHING.getCode())
				.establishStep(ConsortiumEnum.EstablishStep.TO_ESTAB_CONS_ADMIN.getCode())
				.deleteFlag(IntTypeEnum.FALSE.getValue())
				.createTime(now)
				.createBy(oper)
				.updateTime(now)
				.updateBy(oper)
				.build();
		
		return consortiumDas.createConsortium(cons) > 0;
	}
	

	public boolean modifyCons(ModifyConsRequest req) {
		
		List<ConsortiumPo> consList = consortiumDas.findByName(req.getName());
		Assert.isTrue(CollectionUtils.isEmpty(consList), "新创建联盟名称已被使用["+req.getName()+"]");
		
		ConsortiumPo cons = ConsortiumPo.builder()
				.id(req.getId())
				.name(req.getName())
				.description(req.getRemark())
				.contactName(req.getContact())
				.contactPhone(req.getPhone())
				.contactEmail(req.getEmail())
				.avatar(req.getAvatar() != null ? req.getAvatar().getBytes() : null)
				.updateTime(LocalDateTime.now())
				.updateBy( WebHelper.getUserId())
				.build();
		
		return consortiumDas.modifyCons(cons) > 0;
	}

	public boolean deleteCons(DeleteConsRequest req) {
		
		ConsortiumPo cons = ConsortiumPo.builder()
				.id(req.getId())
				.deleteFlag(IntTypeEnum.TRUE.getValue())
				.updateTime(LocalDateTime.now())
				.updateBy( WebHelper.getUserId())
				.build();
		return consortiumDas.modifyCons(cons) > 0;
	}
	

	public boolean createConsAdmin(CreateConsAdminRequest req) {
		
		RoleInfoBo roleBo = null;
		Optional<RolePo> roleOp = roleDas.findConsAdminRole(req.getConsId());
		if (!roleOp.isPresent()) {
			List<String> reqPermissions = UserEnum.PermissionPoint.getForConsAdmin().stream()
					.map(item -> item.getCode()).collect(Collectors.toList());
			roleBo = buildService.buildRole(UserEnum.Role.CONSORTIUM_ADMIN.getCode(),
					UserEnum.Role.CONSORTIUM_ADMIN.getDesc(), req.getConsId(), reqPermissions);
		}
		
		String roleId = roleBo != null ? roleBo.getRole().getId() : roleOp.get().getId();
		List<UserInfoBo> userBos = req.getUsers().stream().filter(item -> item.getUserId() == null).map(
					item -> buildService.buildUser(item.getAccount(),
						item.getName(), item.getPhone(), item.getEmail(), 
						item.getPwd(), Lists.newArrayList(roleId))
				).collect(Collectors.toList());
		
		List<UserInfoPo> users = Lists.newArrayList();
		List<UserRolePo> userRoles = Lists.newArrayList();
		if(CollectionUtils.isNotEmpty(userBos)) {
			userBos.stream().forEach(item -> {
				users.add(item.getUser());
				userRoles.addAll(item.getUserRoles());
			});
		}
		
		List<String> existUserIds = req.getUsers().stream().filter(item -> item.getUserId() != null)
				.map(ConsAdminInfo::getUserId).collect(Collectors.toList());
		List<UserRolePo> dbUserRoles = roleDas.findUserRolesByUserIds(existUserIds);
		Set<String> dbUserIdSet = dbUserRoles.stream().map(UserRolePo::getUserId).collect(Collectors.toSet());
		existUserIds.stream().filter(item -> !dbUserIdSet.contains(item))
				.forEach(item -> userRoles.add(buildService.buildUserRole(item, roleId)));
		
		return userInfoDas.createConsAdmin(roleBo != null ? roleBo.getRole() : null,
				roleBo != null ? roleBo.getRolePermissionList() : null, users, userRoles);
	}
	

	public boolean createBlockchain( CreateBlockchainRequest createBlockchainRequest) {
		return false;
	}
	

	public boolean addNodes( AddNodesRequest addNodesRequest) {
		return false;
	}
	

	public void getDeployScript(GetDeployScriptRequest getDeployScriptRequest) {
		
	}
}
