package org.treegraph.baas.api.dto.response;

import java.util.List;

import lombok.*;
import org.treegraph.baas.api.dto.ListResponse;
import org.treegraph.baas.api.dto.UserInfo;

@Data
@EqualsAndHashCode(callSuper = true)
public class ListUserResponse extends ListResponse {

  private List<UserInfo> list;

  @Builder
  public ListUserResponse(long total, int current, int pageSize, List<UserInfo> list) {
    super(total, current, pageSize);
    this.list = list;
  }
}
