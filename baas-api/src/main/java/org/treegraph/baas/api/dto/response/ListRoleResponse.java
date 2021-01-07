package org.treegraph.baas.api.dto.response;

import java.util.List;

import org.treegraph.baas.api.dto.ListResponse;
import org.treegraph.baas.api.dto.RolePermissionInfo;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ListRoleResponse extends ListResponse {

  private List<RolePermissionInfo> list;

  @Builder
  public ListRoleResponse(long total, int current, int pageSize, List<RolePermissionInfo> list) {
    super(total, current, pageSize);
    this.list = list;
  }


}
