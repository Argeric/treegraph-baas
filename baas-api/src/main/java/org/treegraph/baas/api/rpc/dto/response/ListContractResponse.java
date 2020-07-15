package org.treegraph.baas.api.rpc.dto.response;

import java.util.List;

import org.treegraph.baas.api.rpc.dto.Contract;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ListContractResponse {

	private Long total;
	
	private List<Contract> list;
}
