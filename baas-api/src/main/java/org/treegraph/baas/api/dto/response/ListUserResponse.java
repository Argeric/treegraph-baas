package org.treegraph.baas.api.dto.response;

import java.util.List;

import org.treegraph.baas.api.UserInfo;

import lombok.Data;

@Data
public class ListUserResponse {

	private List<UserInfo> list;
	
	private int total;
}
