package org.treegraph.baas.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zt
 * @version 1.0
 * @date May 11, 2020
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NodeInfo {

  private String id;

  private String ip;

  private String port;

  private String org;

  private String consId;

  private String consName;

  private String blockchainId;

  private String blockchainName;

  private String createTime;

  private Integer status;

  private String logMonitorUri;

  private String sysMonitorUri;

  private String deployMsg;
}
