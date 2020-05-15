package org.treegraph.baas.api.dto.response;

import java.util.List;

import org.treegraph.baas.api.dto.CertInfo;
import org.treegraph.baas.api.dto.ListResponse;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ListCertResponse extends ListResponse{

	private List<CertInfo> list;
	
	@Builder
	public ListCertResponse(long total, int current, int pageSize, List<CertInfo> list) {
		super(total, current, pageSize);
		this.list = list;
	}
}
