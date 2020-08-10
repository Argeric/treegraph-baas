package org.treegraph.baas.api.dto.request;

import java.util.List;

import lombok.Data;

@Data
public class DownloadDeployScriptRequest {
	
	private String consId;
	
	private List<String> nodeId;
}




