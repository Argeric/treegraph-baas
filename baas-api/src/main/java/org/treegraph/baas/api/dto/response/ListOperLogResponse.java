package org.treegraph.baas.api.dto.response;

import java.util.List;

import org.treegraph.baas.api.dto.ListResponse;
import org.treegraph.baas.api.dto.OperLogInfo;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ListOperLogResponse extends ListResponse {


  private List<OperLogInfo> list;

  @Builder
  private ListOperLogResponse(long total, int current, int pageSize, List<OperLogInfo> list) {
    super(total, current, pageSize);
    this.list = list;
  }
}
