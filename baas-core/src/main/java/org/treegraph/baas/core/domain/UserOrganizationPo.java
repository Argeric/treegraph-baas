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
public class UserOrganizationPo implements Serializable {

  /**
   * 主键
   */
  private Long id;

  /**
   * 用户id
   */
  private String userId;

  /**
   * 联盟id
   */
  private String consortiumId;

  /**
   * 机构名称
   */
  private String organizationName;

  /**
   * 0-未删除，1-已删除
   */
  private Integer deleteFlag;

  /**
   * 创建时间
   */
  private LocalDateTime createTime;

  /**
   * 更新时间
   */
  private LocalDateTime updateTime;

  private static final long serialVersionUID = 1L;
}