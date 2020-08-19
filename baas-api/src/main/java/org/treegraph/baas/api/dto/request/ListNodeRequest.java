package org.treegraph.baas.api.dto.request;

import org.treegraph.baas.api.dto.ListRequest;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
@EqualsAndHashCode(callSuper=true)
public class ListNodeRequest extends ListRequest{
	
	private String consId;
	
	private String ip;
}
