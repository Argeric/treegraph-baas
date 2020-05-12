package org.treegraph.baas.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AccountStatusEnum {
	NOTIFYING(0,"入账中"),
    SUCCESS(1, "入账成功"),
    FAILED(2, "入账失败"),
    UNNOTIFIED(3,"待入账"),
    ;

    private int    code;
    private String message;
}
