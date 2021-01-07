package org.treegraph.baas.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class ScanEnum {

  /*  0 - 纪元
  1 - 区块
  2 - 交易
  3 - 账户
  4 - 合约*/
  @Getter
  @AllArgsConstructor
  public enum FavoriteType {

    EPOCH(0, "纪元"),

    BLOCK(1, "区块"),

    TRANSACTION(2, "交易"),

    ACCOUNT(3, "账户"),

    SMART_CONTRACT(4, "合约"),

    ;

    private final int code;

    private final String desc;

    public static FavoriteType getByCode(int code) {
      for (FavoriteType s : FavoriteType.values()) {
        if (s.code == code) {
          return s;
        }
      }
      return null;
    }
  }
}
