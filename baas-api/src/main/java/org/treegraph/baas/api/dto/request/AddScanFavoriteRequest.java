package org.treegraph.baas.api.dto.request;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddScanFavoriteRequest {

  private String consId;

  private String blockchainId;

  private Integer type;

  private String hash;

  private String remark;
}




