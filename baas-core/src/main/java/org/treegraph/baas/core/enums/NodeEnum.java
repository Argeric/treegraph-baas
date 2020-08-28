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
         * 未启动
         */
        TO_DEPLOY(0, "未启动"),

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
        
        /**
         * 添加节点中
         */
        ADDING_BY_MEMBERSHIP_CHANGE(21, "添加节点中"),
        
        /**
         * 添加节点完成
         */
        ADDED_BY_MEMBERSHIP_CHANGE(22, "添加节点完成"),
        
        /**
         * 移除节点中
         */
        REMOVING_BY_MEMBERSHIP_CHANGE(23, "移除节点中"),
        
        /**
         * 移除节点完成
         */
        REMOVED_BY_MEMBERSHIP_CHANGE(24, "移除节点完成"),

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
