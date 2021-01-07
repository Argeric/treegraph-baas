package org.treegraph.baas.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


public class HeartbeatEnum {

  /**
   * 状态
   */
  @Getter
  @AllArgsConstructor
  public enum Status {
    /**
     * 正常
     */
    GREEN,

    /**
     * 异常
     */
    YELLOW,

    /**
     * 挂了
     */
    RED,
    ;
  }
}
