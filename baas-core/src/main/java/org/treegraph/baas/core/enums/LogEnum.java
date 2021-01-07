package org.treegraph.baas.core.enums;

import lombok.Getter;

public class LogEnum {


  @Getter
  public enum Login {

    LOGIN(1, "login"),

    LOGOUT(2, "logout"),

    NO_PWD_LOGIN(3, "noPwdLogin"),

    ;

    private final int code;

    private final String desc;

    Login(int code, String desc) {
      this.code = code;
      this.desc = desc;
    }
  }
}
