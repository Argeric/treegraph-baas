package org.treegraph.baas.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 登录入口编码
 */
@Getter
@AllArgsConstructor
public enum LoginEntryCodeEnum {
    INHOUSE(1, "自营催收"),
    VENDOR(2, "委外催收"),
    DEBTOR(3, "本人还款")
    ;

    private int    code;
    private String message;

    public static LoginEntryCodeEnum getEnumByCode(Integer code, boolean silently) {
        if (code == null) {
            if (silently) {
                return null;
            } else {
                throw new IllegalArgumentException("code不能为空");
            }
        }

        for (LoginEntryCodeEnum item: LoginEntryCodeEnum.values()) {
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
