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
public class PermissionPo implements Serializable {

  /**
   * 主键
   */
  private Long id;

  /**
   * 权限类型，1:功能权限 2:菜单权限，3:uri权限
   */
  private Integer permissionType;

  /**
   * 权限名称
   */
  private String permissionName;

  /**
   * 权限码
   */
  private String permissionCode;

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