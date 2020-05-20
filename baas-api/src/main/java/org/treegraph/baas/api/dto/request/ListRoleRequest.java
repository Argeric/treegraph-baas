package org.treegraph.baas.api.dto.request;

import org.treegraph.baas.api.dto.ListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class ListRoleRequest extends ListRequest{
	
	private String consId;
	
	private String name;
}
