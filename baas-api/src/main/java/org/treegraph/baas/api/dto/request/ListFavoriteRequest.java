package org.treegraph.baas.api.dto.request;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.treegraph.baas.api.dto.ListRequest;

@Data
@EqualsAndHashCode(callSuper = true)
public class ListFavoriteRequest extends ListRequest {

  private String blockchainId;

  @Builder
  public ListFavoriteRequest(int current, int pageSize, String blockchainId) {
    super(current, pageSize);
    this.blockchainId = blockchainId;
  }
}
