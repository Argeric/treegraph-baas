package org.treegraph.baas.api.dto.response;

import java.util.List;

import org.treegraph.baas.api.dto.ListResponse;
import org.treegraph.baas.api.dto.NodeInfo;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ListNodeResponse extends ListResponse {

  private List<NodeInfo> list;

  @Builder
  public ListNodeResponse(long total, int current, int pageSize, List<NodeInfo> list) {
    super(total, current, pageSize);
    this.list = list;
  }
}
