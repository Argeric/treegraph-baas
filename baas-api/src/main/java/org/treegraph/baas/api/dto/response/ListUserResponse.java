package org.treegraph.baas.api.dto.response;

import java.util.List;

import org.treegraph.baas.api.UserInfo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ListUserResponse {

	private List<UserInfo> list;
	
	private Long total;
}
