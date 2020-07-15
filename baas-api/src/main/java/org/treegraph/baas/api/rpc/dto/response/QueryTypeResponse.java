package org.treegraph.baas.api.rpc.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QueryTypeResponse {

	private Long typeCode;
}
