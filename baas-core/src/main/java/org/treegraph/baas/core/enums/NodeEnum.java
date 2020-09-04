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
        TO_LAUNCH(0, "未启动"),

        /**
         * 运行中
         */
        RUNING(1, "运行中"),
        
        /**
         * 异常
         */
        EXCEPTION(2, "异常"),
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
        
        public static boolean isRuning(int status) {
            return RUNING.getCode() == status;
        }
    }
    
	 /**
     * 节点添加状态
     */
    @Getter
    @AllArgsConstructor
    public enum MembershipStatus {
    	
        TO_LAUNCH_FIRST_TIME(20, "待首次启动"),

        ADDING(21, "添加节点中"),
        
        ADD_SUCCESS(22, "添加节点成功"),
        
        ADD_FAILURE(23, "添加节点失败"),
        
        REMOVING(31, "移除节点中"),
        
        REMOVE_SUCCESS(32, "移除节点成功"),
        
        REMOVE_FAILURE(33, "移除节点失败"),

        ;

        private Integer code;

        private String desc;

        public static MembershipStatus getByCode(int code) {
            for (MembershipStatus s : MembershipStatus.values()) {
                if (s.code.equals(code)) {
                    return s;
                }
            }
            return null;
        }
    }
}
