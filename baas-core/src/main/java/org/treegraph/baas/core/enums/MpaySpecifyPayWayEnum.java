package org.treegraph.baas.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Mpay的指定支付方式
 */
@Getter
@AllArgsConstructor
public enum MpaySpecifyPayWayEnum {
    ALIPAY(0, "支付宝支付"),
    WECHAT(1, "微信支付"),
    QUICK_PAYMENT(2, "快捷支付"),
    BANK_CARD(3, "银行卡支付")
    ;

    private int    code;
    private String message;

    public static MpaySpecifyPayWayEnum getEnumByCode(Integer code, boolean silently) {
        if (code == null) {
            if (silently) {
                return null;
            } else {
                throw new IllegalArgumentException("code不能为空");
            }
        }

        for (MpaySpecifyPayWayEnum item : MpaySpecifyPayWayEnum.values()) {
            if (item.getCode() == code) {
                return item;
            }
        }

        if (silently) {
            return null;
        } else {
            throw new IllegalArgumentException("没有对应的枚举, code: " + code);
        }
    }
}
