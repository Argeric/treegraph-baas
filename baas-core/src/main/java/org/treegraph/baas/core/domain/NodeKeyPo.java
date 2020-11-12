package org.treegraph.baas.core.domain;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NodeKeyPo implements Serializable {

  /**
   * 主键
   */
  private Long id;

  /**
   * 节点id
   */
  private String nodeId;

  /**
   * 公钥
   */
  private Long keypairId;

  /**
   * 联盟id，根证书该字段为0
   */
  private String consortiumId;

  /**
   * 0-未删除，1-已删除
   */
  private Integer deleteFlag;

  /**
   * 创建时间
   */
  private LocalDateTime createTime;

  /**
   * 创建者
   */
  private String createBy;

  /**
   * 修改时间
   */
  private LocalDateTime updateTime;

  /**
   * 修改者
   */
  private String updateBy;

  private static final long serialVersionUID = 1L;
}