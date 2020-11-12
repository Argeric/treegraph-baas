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
public class NodeCustomizedConfigPo implements Serializable {

  /**
   *
   */
  private Long id;

  /**
   * 联盟id
   */
  private String consortiumId;

  /**
   * 节点id
   */
  private String nodeId;

  /**
   * 属性名
   */
  private String propertyName;

  /**
   * 0-未删除1-已删除
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
   * 更新时间
   */
  private LocalDateTime updateTime;

  /**
   * 更新者
   */
  private String updateBy;

  /**
   * 属性值
   */
  private String propertyValue;

  private static final long serialVersionUID = 1L;
}