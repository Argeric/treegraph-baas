package org.treegraph.baas.core.enums;

import java.util.Arrays;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AllowRepaymentEnum {

	ALLOW("00", "允许", ""),
	JUMP_TERM("01", "不允许跳跃还款", "不允许跳期还款"),
	WITHHOLDING("02", "扣款中", "扣款中,请稍后尝试"),
    AMOUNT_ERR("03", "还款金额非法", "还款金额有误"),
    ALREADY_REPAID("04", "本期已还", "本期已结清"),
    NOT_ALLOWED_WEIZHONG("05", "微众资金方不允许", "请等待扣款"),
    ;

    public final String code;
    public final String desc;
    public final String userTip;

    public static AllowRepaymentEnum getByCode(String code) {
        return Arrays.stream(AllowRepaymentEnum.values())
                .filter(s -> s.getCode().equals(code)).findFirst().orElse(null);
    }
}
