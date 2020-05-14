package org.treegraph.baas.core.das.impl;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
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
	public List<RolePermissionPo> findRolePermissionsByRoleId(String roldId) {
		RolePermissionPoExample example = new RolePermissionPoExample();
		example.createCriteria().andRoleIdEqualTo(roldId);
		return rolePermissionPoMapper.selectByExample(example);
	}

	@Override
	public List<RolePermissionPo> findRolePermissionsByRoleIds(List<String> roldIds) {

		RolePermissionPoExample example = new RolePermissionPoExample();
		example.createCriteria().andRoleIdIn(roldIds);
		return rolePermissionPoMapper.selectByExample(example);
	}

	@Override
	public List<PermissionPo> findPermissionsByIds(List<Long> permissionIds) {

		PermissionPoExample example = new PermissionPoExample();
		example.createCriteria().andIdIn(permissionIds);
		return permissionPoMapper.selectByExample(example);
	}

	@Override
	public List<PermissionPo> findAllPermissions() {
		PermissionPoExample example = new PermissionPoExample();
		return permissionPoMapper.selectByExample(example);
	}

	@Override
	public Map<String, Long> validatePermissions(List<String> reqPermissions) {
		List<PermissionPo> permissions = this.findAllPermissions();
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
