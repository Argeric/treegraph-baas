package org.treegraph.baas.api.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeleteFavoriteRequest {

  private Long id;
}