package org.treegraph.baas.api.dto.request;

import java.util.List;

import org.treegraph.baas.api.dto.RoleInfo;

import lombok.Data;

@Data
public class InviteUserRequest {

	private String account;
	
	private List<RoleInfo> roles;
}
