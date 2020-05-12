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

        private Integer status;

        private String desc;

        public static Status getByStatus(int status) {
            for (Status s : Status.values()) {
                if (s.status.equals(status)) {
                    return s;
                }
            }
            return null;
        }

        public static boolean isAvailable(int status) {
            return AVAILABLE.getStatus() == status;
        }
    }

}
