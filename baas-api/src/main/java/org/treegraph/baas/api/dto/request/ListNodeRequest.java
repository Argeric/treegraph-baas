package org.treegraph.baas.api.dto.request;

import org.treegraph.baas.api.dto.ListRequest;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class ListNodeRequest extends ListRequest{
	
	private String consId;
	
	private String ip;

	@Builder
	public ListNodeRequest(int current, int pageSize, String consId, String ip) {
		super(current, pageSize);
		this.consId = consId;
		this.ip = ip;
	}
}
