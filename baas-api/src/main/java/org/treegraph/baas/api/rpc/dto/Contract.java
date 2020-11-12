package org.treegraph.baas.api.rpc.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Contract {

  private String transactionHash;

  private String address;

  private String from;

  private Long timestamp;
}
