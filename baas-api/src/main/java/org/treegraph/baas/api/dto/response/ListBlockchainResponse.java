package org.treegraph.baas.api.dto.response;

import java.util.List;

import org.treegraph.baas.api.dto.BlockchainInfo;
import org.treegraph.baas.api.dto.ListResponse;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ListBlockchainResponse extends ListResponse{

	private List<BlockchainInfo> list;
	
	@Builder
	public ListBlockchainResponse(long total, int current, int pageSize, List<BlockchainInfo> list) {
		super(total, current, pageSize);
		this.list = list;
	}
}