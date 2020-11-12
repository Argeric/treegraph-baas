package org.treegraph.baas.common.enums;

/**
 * 0/1操作枚举类
 */
public enum IntTypeEnum {
  /**
   * 真
   */
  TRUE(1, "true"),

  /**
   * 假
   */
  FALSE(0, "false"),
  ;

  private Integer value;
  private String name;

  private IntTypeEnum(Integer value, String name) {
    this.value = value;
    this.name = name;
  }

  /**
   * 是否为真
   *
   * @param value
   * @return
   */
  public static boolean isTrue(Integer value) {
    return isSomeType(value, IntTypeEnum.TRUE);
  }

  /**
   * 是否为假
   *
   * @param value
   * @return
   */
  public static boolean isFalse(Integer value) {
    return isSomeType(value, IntTypeEnum.FALSE);
  }

  private static boolean isSomeType(Integer value, IntTypeEnum type) {
    if (null == value) {
      return false;
    }
    return type.value.equals(value);
  }

  public Integer getValue() {
    return value;
  }

  public String getName() {
    return name;
  }
}
