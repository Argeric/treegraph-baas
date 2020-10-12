package org.treegraph.baas.api.dto.request;

import java.util.List;

import org.treegraph.baas.api.dto.ConsAdminInfo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateConsAdminRequest {

	private String consId;
	
	private List<ConsAdminInfo> users;
}
