package org.treegraph.baas.core.enums;

import lombok.Getter;

public class LogEnum {



  @Getter
  public enum Login {

    LOGIN(1, "login"),

    LOGOUT(2, "logout"),

    NO_PWD_LOGIN(3, "noPwdLogin"),

    ;

    private int code;
    
    private String desc;

    Login(int code, String desc) {
      this.code = code;
      this.desc = desc;
    }
  }

  /**
   * 模块具体操作项
   * 
   * @date Sep 19, 2016 4:27:24 PM
   */
  @Getter
  public enum Operate {

    USER_LOGIN("用户管理", "用户登录"),

    SKILL_GROUP_DEL("技能组", "删除"),

    SKILL_GROUP_UPDATE("技能组", "更新"),

    SKILL_GROUP_EMPLOYEE_DEL("技能组成员", "删除"),

    ;

    /**
     * 模块
     */
    String module;
    /**
     * 操作项
     */
    String operate;

    Operate(String module, String operate) {
      this.module = module;
      this.operate = operate;
    }

  }

}
