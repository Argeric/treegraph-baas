package org.treegraph.baas.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


public class BlockchainEnum {
	
	/**
     * 区块链状态
     */
    @Getter
    @AllArgsConstructor
    public enum Status {

        TO_DEPLOY(0, "待部署"),

        RUNNING(1, "运行中"),
  
        STOPPED(2, "已停止"),

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
    }

    /**
     * 共识策略
     */
    @Getter
    @AllArgsConstructor
    public enum Consensus {

        TREE_GRAPH(1, "treegraph", "数图共识"),
        ;

        private final Integer code;
        
        private final String name;

        private final String desc;

        public static Consensus getByCode(int code) {
            for (Consensus s : Consensus.values()) {
                if (s.code.equals(code)) {
                    return s;
                }
            }
            return null;
        }
        
        public static Consensus getByName(String name) {
            for (Consensus s : Consensus.values()) {
                if (s.name.equals(name)) {
                    return s;
                }
            }
            return null;
        }
    }
    
    /**
     * 加密算法
     */
    @Getter
    @AllArgsConstructor
    public enum ALGORITHM {
    	
    	TREE_GRAPH(1, "treegraph", "数图加密"),
        ;

        private final Integer code;
        
        private final String name;

        private final String desc;

        public static ALGORITHM getByCode(int code) {
            for (ALGORITHM s : ALGORITHM.values()) {
                if (s.code.equals(code)) {
                    return s;
                }
            }
            return null;
        }
        
        public static ALGORITHM getByName(String name) {
            for (ALGORITHM s : ALGORITHM.values()) {
                if (s.name.equals(name)) {
                    return s;
                }
            }
            return null;
        }
    }
}
