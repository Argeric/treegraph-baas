package org.treegraph.baas.core.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.treegraph.baas.api.RolePermissionInfo;
import org.treegraph.baas.api.dto.request.CreateRoleRequest;
import org.treegraph.baas.api.dto.request.ListRoleRequest;
import org.treegraph.baas.api.dto.request.ModifyRoleRequest;
import org.treegraph.baas.api.dto.response.ListRoleResponse;
import org.treegraph.baas.common.enums.IntTypeEnum;
import org.treegraph.baas.common.util.WebHelper;
import org.treegraph.baas.core.das.ConsortiumDas;
import org.treegraph.baas.core.das.PermissionDas;
import org.treegraph.baas.core.das.RoleDas;
import org.treegraph.baas.core.domain.ConsortiumPo;
import org.treegraph.baas.core.domain.PermissionPo;
import org.treegraph.baas.core.domain.RolePermissionPo;
import org.treegraph.baas.core.domain.RolePo;
import org.treegraph.baas.core.enums.UserEnum;
import org.treegraph.baas.core.exception.AlertException;
import org.treegraph.baas.core.service.RoleService;
import org.treegraph.baas.core.util.CollectionUtils;
import org.treegraph.baas.core.util.DateTimeUtils;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;

import lombok.AllArgsConstructor;

@Validated
@Service
@AllArgsConstructor
public class RoleServicempl implements RoleService {

    private RoleDas roleDas;
	
    private PermissionDas permissionDas;
    
    private ConsortiumDas consortiumDas;
    
    public ListRoleResponse listRole(ListRoleRequest req) {
    	
    	PageHelper.startPage(req.getCurrent(), req.getPageSize());
    	List<RolePo> roles = roleDas.findRoles(req.getConsId(), req.getName());
    	PageInfo<RolePo> pageInfo = new PageInfo<RolePo>(roles);
    	
    	List<RolePo> roleList = pageInfo.getList();
    	List<RolePermissionInfo> rolePermInfos = null;
    	if(CollectionUtils.isNotEmpty(roleList)) {
	    	List<String> consIds = roleList.stream().map(RolePo::getConsortiumId).collect(Collectors.toList());
	    	List<ConsortiumPo> cons = consortiumDas.findByIds(consIds);
			Map<String, String> consIdNameMap = cons.stream()
					.collect(Collectors.toMap(ConsortiumPo::getId, ConsortiumPo::getName));
	    	
	    	List<PermissionPo> allPerms = permissionDas.findAllPermissions();
			Map<Long, String> allPermIdCodeMap = allPerms.stream()
					.collect(Collectors.toMap(PermissionPo::getId, PermissionPo::getPermissionCode));
	    	List<String> roleIds = roleList.stream().map(RolePo::getId).collect(Collectors.toList());
	    	List<RolePermissionPo> rolePerms = permissionDas.findRolePermissionsByRoleIds(roleIds);
			Map<String, List<String>> roleIdPermCodeMap = rolePerms.stream()
					.collect(Collectors.groupingBy(RolePermissionPo::getRoleId,
							Collectors.mapping(item -> allPermIdCodeMap.get(item.getPermissionId()), Collectors.toList())));
	    	
	    	rolePermInfos = roles.stream().map(item -> RolePermissionInfo.builder()
					.id(item.getId())
					.consId(item.getConsortiumId())
					.consName(consIdNameMap.get(item.getConsortiumId()))
					.createTime(DateTimeUtils.formatTime(item.getCreateTime(),DateTimeUtils.FORMAT_yyyyMMddHHmmss))
					.name(item.getRoleName())
					.permissions(roleIdPermCodeMap.get(item.getId()))
					.build()).collect(Collectors.toList());
    	}
    	
    	ListRoleResponse resp = ListRoleResponse.builder()
    			.total(pageInfo.getTotal())
    			.list(rolePermInfos)
    			.build();
    	
    	return resp;
    }
    
   	public boolean createRole(CreateRoleRequest req) {
   		
   		List<RolePermissionPo> rolePermissionList = Lists.newArrayList();
   		List<String> reqPermissions = req.getPermissions();
   		LocalDateTime now = LocalDateTime.now();
		if(CollectionUtils.isNotEmpty(reqPermissions)) {
			Map<String, Long> permissionCodeIdMap = validatePermissions(reqPermissions);
   			reqPermissions.stream().forEach(item -> {
   				RolePermissionPo rolePerm = RolePermissionPo.builder()
   						.permissionId(permissionCodeIdMap.get(item))
   						.deleteFlag(IntTypeEnum.FALSE.getValue())
   						.createTime(now)
   						.createBy(WebHelper.getUserId())
   						.updateTime(now)
   						.updateBy(WebHelper.getUserId())
   						.build();
   				rolePermissionList .add(rolePerm);
   			});
   		}
   		
   		RolePo role = RolePo.builder()
   				.id(UUID.randomUUID().toString())
   				.consortiumId(req.getConsId())
   				.roleName(req.getName())
   				.roleType(UserEnum.Role.COMMON_USER.getCode())
   				.deleteFlag(IntTypeEnum.FALSE.getValue())
				.createTime(now)
				.createBy(WebHelper.getUserId())
				.updateTime(now)
				.updateBy(WebHelper.getUserId())
   				.build();
   		
   		return roleDas.createRole(role, rolePermissionList);
   	}
   	
   	public boolean modifyRole(ModifyRoleRequest req) {
   		
   		String roleId = req.getId();
   		String consId = req.getConsId();
   		RolePo role = roleDas.findRolesById(roleId).orElseThrow(() -> new AlertException("role.not.found["+roleId+"]"));
   		Assert.isTrue(role.getConsortiumId().equals(consId), "角色所属联盟不匹配["+roleId+"]["+consId+"]");
   		RolePo updateRole = null;
   		if(req.getName() != null && !req.getName().equals(role.getRoleName())) {
			updateRole = RolePo.builder().id(role.getId()).roleName(role.getRoleName()).build();
   		}
   		
   		List<String> reqPermissionCodes = req.getPermissions();
   		List<RolePermissionPo> toAddPerms = null;
   		List<Long> toDelPermIds = null;
   		LocalDateTime now = LocalDateTime.now();
   		if(CollectionUtils.isNotEmpty(reqPermissionCodes)) {
   			Map<String, Long> permissionCodeIdMap = validatePermissions(reqPermissionCodes);
	   		List<RolePermissionPo> dbRolePermissions = permissionDas.findRolePermissionsByRoleId(roleId);
	   		List<Long> dbPermissionIds = dbRolePermissions.stream().map(RolePermissionPo::getPermissionId).collect(Collectors.toList());
	   		List<PermissionPo> dbPermissions = permissionDas.findPermissionsByIds(dbPermissionIds);
	   		List<String> dbPermissionCodes = dbPermissions.stream().map(PermissionPo::getPermissionCode).collect(Collectors.toList());
	   		
	   		List<String> interset = Lists.newArrayList();
	   		interset.addAll(reqPermissionCodes);
	   		interset.retainAll(dbPermissionCodes);
	   		reqPermissionCodes.removeAll(interset);
	   		dbPermissionCodes.removeAll(interset);
	   		
	   		toAddPerms = reqPermissionCodes.stream().map(item -> RolePermissionPo.builder()
	   				.roleId(roleId)
	   				.permissionId(permissionCodeIdMap.get(item))
	   				.deleteFlag(IntTypeEnum.FALSE.getValue())
					.createTime(now)
					.createBy(WebHelper.getUserId())
					.updateTime(now)
					.updateBy(WebHelper.getUserId())
	   				.build()).collect(Collectors.toList());
	   		toDelPermIds = dbPermissionCodes.stream().map(item -> permissionCodeIdMap.get(item)).collect(Collectors.toList());;
   		}
   		
   		return roleDas.modifyRole(roleId, updateRole, toDelPermIds, toAddPerms);
   	}

	private Map<String, Long> validatePermissions(List<String> reqPermissions) {
		List<PermissionPo> permissions = permissionDas.findAllPermissions();
		Set<String> permissionCodeSet = permissions.stream().map(PermissionPo::getPermissionCode)
				.collect(Collectors.toSet());
		reqPermissions.stream().forEach(item -> {
			Assert.isTrue(permissionCodeSet.contains(item), "角色权限非法[" + item + "]");
		});

		Map<String, Long> permissionCodeIdMap = permissions.stream()
				.collect(Collectors.toMap(PermissionPo::getPermissionCode, PermissionPo::getId));
		return permissionCodeIdMap;
	}
}
