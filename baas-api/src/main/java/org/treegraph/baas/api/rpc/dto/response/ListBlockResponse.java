package org.treegraph.baas.api.rpc.dto.response;

import java.util.List;

import org.treegraph.baas.api.rpc.dto.Block;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ListBlockResponse {

	private Long total;
	
	private List<Block> list;
	
	private Long serverTimestamp;
	
	private Long minTime;
	
	private Long maxTime;
}
