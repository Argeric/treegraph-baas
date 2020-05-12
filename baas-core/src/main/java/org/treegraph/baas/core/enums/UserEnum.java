package org.treegraph.baas.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


public class UserEnum {


    /**
     * 员工状态
     */
    @Getter
    @AllArgsConstructor
    public enum Status {

        /**
         * 禁用
         */
        DISABLE(0, "禁用"),

        /**
         * 启用
         */
        AVAILABLE(1, "启用"),

        ;

        private Integer code;

        private String desc;

        public static Status getByCode(int code) {
            for (Status s : Status.values()) {
                if (s.code.equals(code)) {
                    return s;
                }
            }
            return null;
        }

        public static boolean isAvailable(int status) {
            return AVAILABLE.getCode() == status;
        }
    }
    
    /**
     * 角色
     */
    @Getter
    @AllArgsConstructor
    public enum Role {

        SYS_ADMIN(1, "系统角色"),

        CONSORTIUM_ADMIN(2, "联盟角色"),
        
        COMMON_USER(2, "普通角色"),

        ;

        private Integer code;

        private String desc;

        public static Status getByCode(int code) {
            for (Status s : Status.values()) {
                if (s.code.equals(code)) {
                    return s;
                }
            }
            return null;
        }

    }

}
