package org.treegraph.baas.api.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QueryRoleRequest {

  private String id;

  private String consId;
}
