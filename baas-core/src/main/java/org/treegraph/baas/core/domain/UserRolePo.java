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
public class UserRolePo implements Serializable {

  /**
   * 主键
   */
  private Long id;

  /**
   * 用户id
   */
  private String userId;

  /**
   * 角色id
   */
  private String roleId;

  /**
   * 联盟id
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
   * 创建用户
   */
  private String createBy;

  /**
   * 更新时间
   */
  private LocalDateTime updateTime;

  /**
   * 更新用户
   */
  private String updateBy;

  private static final long serialVersionUID = 1L;
}