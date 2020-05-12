package org.treegraph.baas.core.enums;

import java.util.Arrays;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AgreementStatusEnum {
	
	ACTIVITY(1,"已激活"),
	OVERDUE(2, "已逾期"),
    ;
	
    private int code;
    private String desc;
    
    public static AgreementStatusEnum getByCode(int code) {
        return Arrays.stream(AgreementStatusEnum.values())
                .filter(s -> s.getCode() == code).findFirst().orElse(null);
    }
}
