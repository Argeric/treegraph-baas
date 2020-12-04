package org.treegraph.baas.api.dto.response;

import java.util.List;

import lombok.*;
import org.treegraph.baas.api.dto.ListResponse;
import org.treegraph.baas.api.dto.OperLogInfo;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ListOperLogResponse extends ListResponse {

  private List<OperLogInfo> list;

  @Builder
  private ListOperLogResponse(long total, int current, int pageSize, List<OperLogInfo> list) {
    super(total, current, pageSize);
    this.list = list;
  }
}
