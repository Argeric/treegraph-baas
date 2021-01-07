package org.treegraph.baas.api.dto.response;

import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.treegraph.baas.api.dto.FavoriteInfo;
import org.treegraph.baas.api.dto.ListResponse;
import org.treegraph.baas.api.dto.NodeInfo;

@Data
@EqualsAndHashCode(callSuper = true)
public class ListFavoriteResponse extends ListResponse {

  private List<FavoriteInfo> list;

  @Builder
  public ListFavoriteResponse(long total, int current, int pageSize, List<FavoriteInfo> list) {
    super(total, current, pageSize);
    this.list = list;
  }
}
