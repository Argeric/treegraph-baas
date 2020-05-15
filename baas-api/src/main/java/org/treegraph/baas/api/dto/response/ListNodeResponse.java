package org.treegraph.baas.api.dto.response;

import java.util.List;

import org.treegraph.baas.api.dto.NodeInfo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ListNodeResponse {

	private List<NodeInfo> list;
	
	private long total;
}
