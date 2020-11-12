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
public class UserLoginTokenPo implements Serializable {

  /**
   *
   */
  private Long id;

  /**
   * id
   */
  private String userId;

  /**
   * token
   */
  private String token;

  /**
   *
   */
  private Integer expire;

  /**
   *
   */
  private LocalDateTime createTime;

  /**
   *
   */
  private LocalDateTime updateTime;

  private static final long serialVersionUID = 1L;
}