package org.treegraph.baas.api.dto.request;

import lombok.Data;

@Data
public class QueryNodeLogRequest {

  private String id;

  private String type;

  private String query;
}
