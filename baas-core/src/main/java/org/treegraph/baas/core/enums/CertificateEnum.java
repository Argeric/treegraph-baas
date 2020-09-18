package org.treegraph.baas.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


public class CertificateEnum {

    /**
     * 延期状态
     */
    @Getter
    @AllArgsConstructor
    public enum DeferStatus {

    	INITIAL(20, "初始化"),
    	
        PROCESSING(21, "请求延期中"),

        SUCCESS(22, "延期成功"),
  
        FAILURE(23, "延期失败"),
        ;

        private Integer code;

        private String desc;

        public static DeferStatus getByCode(int code) {
            for (DeferStatus s : DeferStatus.values()) {
                if (s.code.equals(code)) {
                    return s;
                }
            }
            return null;
        }
    }
}
