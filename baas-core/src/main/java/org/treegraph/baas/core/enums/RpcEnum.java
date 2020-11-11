package org.treegraph.baas.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class RpcEnum {

    @Getter
    @AllArgsConstructor
    public enum Type {

        JSONRPC("jsonrpc"),

        GRPC("grpc"),

        ;

        private final String code;

        public static Type getByCode(String code) {
            for (Type s : Type.values()) {
                if (s.code.equals(code)) {
                    return s;
                }
            }
            return null;
        }
    }
}
