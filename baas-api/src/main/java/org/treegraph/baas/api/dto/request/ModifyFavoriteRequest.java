package org.treegraph.baas.api.dto.request;

import lombok.Data;

@Data
public class ModifyFavoriteRequest {

  private Long id;
  private String remark;
}