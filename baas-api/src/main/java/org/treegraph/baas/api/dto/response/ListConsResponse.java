package org.treegraph.baas.api.dto.response;

import java.util.List;

import org.treegraph.baas.api.dto.ConsInfo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ListConsResponse {

	private List<ConsInfo> list;
	
	private long total;
}
