package org.treegraph.baas.api.dto.request;

import org.treegraph.baas.api.dto.ListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class ListOperLogRequest extends ListRequest{
	
	private String account;
	
	private String name;
	
	private String type;
	
	private String ip;
}
