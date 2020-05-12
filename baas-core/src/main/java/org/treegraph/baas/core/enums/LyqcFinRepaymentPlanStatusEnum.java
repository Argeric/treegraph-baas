package org.treegraph.baas.core.enums;

import java.util.Arrays;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LyqcFinRepaymentPlanStatusEnum {
	
	NOT_YET_CHARGE(1,"未扣款"),
	CHARGED_SUCCESS(2, "扣款成功"),
	EARLY_SETTLED(3,"已提前结清"),
	CONTRACT_CANCELED(4, "已取消合同"),
	REPAY_ONGOING(5,"还款中"),
    ;
	
    private int code;
    private String desc;
    
    public static LyqcFinRepaymentPlanStatusEnum getByCode(int code) {
        return Arrays.stream(LyqcFinRepaymentPlanStatusEnum.values())
                .filter(s -> s.getCode() == code).findFirst().orElse(null);
    }
}
