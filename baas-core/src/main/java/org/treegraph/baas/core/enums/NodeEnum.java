package org.treegraph.baas.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


public class NodeEnum {

	 /**
     * 节点状态
     */
    @Getter
    @AllArgsConstructor
    public enum Status {

        /**
         * 部署中
         */
        TO_DEPLOY(0, "待部署"),

        /**
         * 运行中
         */
        RUNING(1, "运行中"),
        
        /**
         * 已停止
         */
        STOP(2, "已停止"),
        
        /**
         * 异常
         */
        EXCEPTION(3, "异常"),

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

        public static boolean isRuningOrDeploying(int status) {
            return RUNING.getCode() == status;
        }
    }

}
