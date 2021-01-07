package org.treegraph.baas.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListResponse {

  public long total;

  public int current;

  public int pageSize;
}
