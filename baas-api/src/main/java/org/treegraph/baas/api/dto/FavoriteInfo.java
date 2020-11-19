package org.treegraph.baas.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteInfo {

  private Long id;

  private Integer type;

  private String hash;

  private String remark;
}
