package org.treegraph.baas.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class OperLogInfo {

  private String id;

  private String name;

  private String account;

  private String type;

  private String createTime;

  private String log;

  private String ip;
}
