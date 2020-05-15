package org.treegraph.baas.api.dto.request;

import org.treegraph.baas.api.dto.ListRequest;

import lombok.Data;

@Data
public class ListNodeRequest extends ListRequest{
	
	private String consId;
	
	private String ip;
}
