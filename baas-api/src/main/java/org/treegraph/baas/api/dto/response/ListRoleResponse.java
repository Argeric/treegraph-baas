package org.treegraph.baas.api.dto.response;

import java.util.List;

import org.treegraph.baas.api.dto.RolePermissionInfo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ListRoleResponse {

	private List<RolePermissionInfo> list;
	
	private long total;
}
