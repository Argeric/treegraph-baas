package org.treegraph.baas.api.dto.response;

import java.util.List;

import org.treegraph.baas.api.dto.ListResponse;
import org.treegraph.baas.api.dto.UserInfo;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ListUserResponse extends ListResponse{
	
	
	private List<UserInfo> list;
	
	@Builder
	private ListUserResponse(long total, int current, int pageSize, List<UserInfo> list) {
		super(total, current, pageSize);
		this.list = list;
	}
}
