package org.treegraph.baas.core.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@AllArgsConstructor
@Slf4j
public enum ErrorCodeEnum {
	SCAN_EXCEPTION(4001, "调用scan接口异常"),
	INVALID_PARAMS(9000, "输入参数错误"),
	BUSINESS_EXCEPTION(9999, "业务异常"),
	AUTH_TOKEN_ERROR(40001, "token异常"),
	AUTH_PERMISSION_ERROR(40002, "permission异常"),
    ;

    private int    code;
    private String message;
    
    public static ErrorCodeEnum getEnumByCode(int code) {

        ErrorCodeEnum result = null;
        for (ErrorCodeEnum curDim : ErrorCodeEnum.values()) {
            if (curDim.getCode() == code) {
                result = curDim;
                break;
            }
        }
        if (result == null) {
            log.warn("code: " + code + " 没有对应的枚举");
            return BUSINESS_EXCEPTION;
        } else {
            return result;
        }
    }
}
