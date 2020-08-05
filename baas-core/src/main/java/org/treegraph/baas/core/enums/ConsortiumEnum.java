package org.treegraph.baas.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


public class ConsortiumEnum {


    /**
     * 联盟状态
     */
    @Getter
    @AllArgsConstructor
    public enum Status {

        ESTABLISHING(0, "创建中"),

        RUNING(1, "运行中"),
  
        STOPED(1, "已停止"),

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
    
    /**
     * 创建步骤
     */
    @Getter
    @AllArgsConstructor
    public enum EstablishStep {
    	
    	TO_ESTAB_CONS(0, "创建联盟"),

        TO_ESTAB_CONS_ADMIN(1, "创建联盟管理员"),

        TO_ESTAB_BLOCKCHAIN(2, "创建区块链"),
        
        TO_ADD_NODE(3, "添加节点"),
        
        TO_DEPLOY(4, "等待部署"),
        
        DOWNLOADED_SCRIPTS(5, "已下载脚本"),
        
        LAUNCHED(6, "已启动"),// blockHeight > 0

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
