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
public class RolePo implements Serializable {

  /**
   * 主键
   */
  private String id;

  /**
   * 联盟id，系统管理员角色，联盟id为0
   */
  private String consortiumId;

  /**
   * 角色类型,1-系统管理员，2-联盟管理员，3-普通用户
   */
  private Integer roleType;

  /**
   * 角色名称
   */
  private String roleName;

  /**
   * 描述
   */
  private String description;

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

  /**
   * 创建人
   */
  private String createBy;

  /**
   * 更新人
   */
  private String updateBy;

  private static final long serialVersionUID = 1L;
}