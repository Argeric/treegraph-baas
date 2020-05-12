package org.treegraph.baas.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AccountPayStatusEnum {
	
	SUCCESS("0","成功"),
    FAIL("1", "失败"),
    ;

    private String    code;
    private String message;
}
