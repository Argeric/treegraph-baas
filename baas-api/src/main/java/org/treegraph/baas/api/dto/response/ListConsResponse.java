package org.treegraph.baas.api.dto.response;

import java.util.List;

import org.treegraph.baas.api.dto.ConsInfo;
import org.treegraph.baas.api.dto.ListResponse;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ListConsResponse extends ListResponse {

  private List<ConsInfo> list;

  @Builder
  public ListConsResponse(long total, int current, int pageSize, List<ConsInfo> list) {
    super(total, current, pageSize);
    this.list = list;
  }
}
