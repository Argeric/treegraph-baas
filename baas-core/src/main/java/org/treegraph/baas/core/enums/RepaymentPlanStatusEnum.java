package org.treegraph.baas.core.enums;

import java.util.Arrays;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RepaymentPlanStatusEnum {
	
	SETTLED(1,"已结清"),
	REPAY_ONGOING(2,"还款中"),
	OVERDUED(3, "已逾期"),
	TO_REPAY(4, "待还款"),
	NOT_STARTED(5,"未开始"),
    ;
	
    private int code;
    private String desc;
    
    public static RepaymentPlanStatusEnum getByCode(int code) {
        return Arrays.stream(RepaymentPlanStatusEnum.values())
                .filter(s -> s.getCode() == code).findFirst().orElse(null);
    }
}
