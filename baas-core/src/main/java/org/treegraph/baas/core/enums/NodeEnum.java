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
        RUNNING(1, "运行中"),
        
        /**
         * 异常
         */
        EXCEPTION(2, "异常"),
        ;

        private final Integer code;

        private final String desc;

        public static Status getByCode(int code) {
            for (Status s : Status.values()) {
                if (s.code.equals(code)) {
                    return s;
                }
            }
            return null;
        }
        
        public static boolean isRunning(int status) {
            return RUNNING.getCode() == status;
        }
    }
    
	 /**
     * 节点添加状态
     * 
     */
    @Getter
    @AllArgsConstructor
    public enum MembershipStatus {
    	
    	// first add nodes
        TO_LAUNCH_FIRST_TIME(20, "待首次启动"),
        
        // add nodes
        ADD_START(21, "添加节点开始"),
        
        ADD_CHECK_THREAD_LAUNCH(22, "添加节点-校验线程启动"),
        
        ADD_SUCCESS(23, "添加节点成功"),
        
        ADD_FAILURE(24, "添加节点失败"),
        
        // remove nodes
        REMOVE_START(31, "移除节点开始"),
        
        REMOVE_CHECK_THREAD_LAUNCH(32, "移除节点-校验线程启动"),
        
        REMOVE_SUCCESS(33, "移除节点成功"),
        
        REMOVE_FAILURE(34, "移除节点失败"),
        ;

        private final int code;

        private final String desc;

        public static MembershipStatus getByCode(int code) {
            for (MembershipStatus s : MembershipStatus.values()) {
                if (s.code == code) {
                    return s;
                }
            }
            return null;
        }
    }
}
