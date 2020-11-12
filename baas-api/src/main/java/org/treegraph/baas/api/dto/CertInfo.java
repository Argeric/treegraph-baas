package org.treegraph.baas.api.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CertInfo {

  private String id;//区块链id

  private String name;//生产用户

  private String account;//生产用户账号

  private List<String> ipList;

  private String createTime;//创建时间
}
