package org.treegraph.baas.api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class DeleteUserRequest {

	private String id;
}
