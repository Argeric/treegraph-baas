package org.treegraph.baas.api.dto.request;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DownloadRpcCertRequest {
	
	private String consId;
	
	private List<String> nodeId;
}




