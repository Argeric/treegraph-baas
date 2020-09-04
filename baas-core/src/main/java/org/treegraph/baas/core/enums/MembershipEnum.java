package org.treegraph.baas.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class MembershipEnum {

	 /**
     * 变更类型
     */
    @Getter
    @AllArgsConstructor
    public enum Type {

        REMOVE(0, "删除节点"),

        ADD(1, "添加节点"),
        ;

        private Integer code;

        private String desc;

        public static Type getByCode(int code) {
            for (Type s : Type.values()) {
                if (s.code.equals(code)) {
                    return s;
                }
            }
            return null;
        }
    }
    
	 /**
     * 节点添加状态
     */
    @Getter
    @AllArgsConstructor
    public enum Status {
    	
        INIT(0, "initialized"),

        SELECT_PROPOSER_SUCCESS(201, "选择proposer成功"),

        SELECT_PROPOSER_FAILURE(301, "选择proposer失败"),

        REQUEST_MEMBERSHIP_CHANGE_SUCCESS(202, "请求membership变更成功"),

        REQUEST_MEMBERSHIP_CHANGE_FAILURE(302, "请求membership变更失败"),
        
        SIGNATURE_MEMBERSHIP_SUCCESS(203, "签名membership变更成功"),

        SIGNATURE_MEMBERSHIP_FAILURE(303, "签名membership变更失败"),

        CONFIRM_MEMBERSHIP_CHANGE_SUCCESS(204, "确认membership变更成功"),

        CONFIRM_MEMBERSHIP_CHANGE_FAILURE(304, "确认membership变更失败"),
        
        LAUNCH_CHECK_THREAD_SUCCESS(205, "启动check线程成功"),
        
        LAUNCH_CHECK_THREAD_FAILURE(305, "启动check线程失败"),
        
        CHECK_MEMBERSHIP_CHANGE_FAILURE(206, "获取membership变更结果，变更成功"),
        
        CHECK_MEMBERSHIP_CHANGE_SUCCESS(306, "获取membership变更结果，变更失败"),

        ;

        private int code;

        private String desc;

        public static Status getByCode(int code) {
            for (Status s : Status.values()) {
                if (s.code == code) {
                    return s;
                }
            }
            return null;
        }
        
        public boolean ifFail() {
        	return this.code > SELECT_PROPOSER_FAILURE.getCode();
        }
    }
}
