package org.treegraph.baas.core.enums;

import java.util.Arrays;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PayResultEnum {
	
	//INITIALIZATION(-1, "初始化"),
	PAYING(0,"订单支付中"),
    SUCCESS(1, "支付成功"),
    FAILED(2, "支付失败"),
    UNPAID(3, "待支付"),
    ORDER_CREATED(4, "预下单"),
    ORDER_CREATED_SUC(5, "下单成功"),
    ORDER_CREATED_FAIL(6, "下单失败"),
    ;

    private int    code;
    private String message;
    
    public static PayResultEnum getByCode(int code) {
        return Arrays.stream(PayResultEnum.values())
                .filter(s -> s.getCode() == code).findFirst().orElse(null);
    }
}
