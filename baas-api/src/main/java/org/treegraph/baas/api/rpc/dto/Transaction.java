package org.treegraph.baas.api.rpc.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Transaction {

  private String blockHash;

  private Long transactionIndex;

  private String nonce;

  private String hash;

  private String from;

  private String to;

  private String value;

  private String gasPrice;

  private String gas;

  private Long contractCreated;

  private Long status;

  private Long timestamp;

  private String syncTimestamp;
}
