package org.treegraph.baas.core.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@AllArgsConstructor
@Slf4j
public enum ErrorCodeEnum {
	NEW_CORE_EXCEPTION(1000, "新核心查询合同信息调用异常"),
	NEW_CORE_CREATE_ORDER_EXCEPTION(1001,"新核心创建还款订单调用异常"),
	YYCS_EXCEPTION(2001, "老核心查询合同信息调用异常"),
	REPAYORDER_EXIST(2002, "有未完成支付的订单，请15分钟后再尝试"),
	OVERPAYMENT_EXCEPTION(2003, "金额已超过剩余应还"),
	YYCS_CONTRACT_NOT_EXIST(2004, "老核心合同不存在"),
	CONTRACT_NAME_ERROR_EXCEPTION(3000,"合同数据异常，请联系客服"),
	CHANNEL_NOT_EXIST_EXCEPTION(3001,"无产品信息"),
	ALLOW_REPAY_AMT_CHANGED(3002,"最大可还金额发生变化，请返回合同列表"),
	PRODUCT_NOT_EXIST_EXCEPTION(3003,"无合同产品信息"),
	MPAY_EXCEPTION(4000, "调用mpay接口异常"),
	FOOTSTONE_EXCEPTION(4001, "调用footStone接口异常"),
	INVALID_PARAMS(9000, "输入参数错误"),
	SMS_SEND_FAST(9001, "短信发送频繁"),
	SMS_SEND_ERROR(9002, "短信发送异常"),
	SMS_VERIFY_CODE_ERROR(9003, "短信验证码错误"),
	BUSINESS_EXCEPTION(9999, "业务异常"),
	AUTH_ERROR(9999, "auth异常"),
	
	//新核心异常
	AGREEMENT_BUSINESS_EXCEPTION(10000, "业务异常"),
    AGREEMENT_BUSINESS_EXCEPTION_INVALID_PARAMETERS(10001, "参数不合法系统异常，请稍后再试"),    
    AGREEMENT_BUSINESS_EXCEPTION_CAPITAL_DEDUCT(10013,"当前合同不允许还款"),
    AGREEMENT_BUSINESS_EXCEPTION_WAIT_CUST_REPAY_COMPLETE(10014,"您有一笔未完成订单，请15分钟后再试"),
    AGREEMENT_BUSINESS_EXCEPTION_WAIT_DAILY_CUT(10015,"系统处理中，请稍后再试"),
    AGREEMENT_BUSINESS_EXCEPTION_WAIT_WITHHOLD_COMPLETE(10016,"您有一笔在途订单，请稍后再试"),
    AGREEMENT_BUSINESS_EXCEPTION_JD_NOT_ALLOW_REPAY(10017,"当前合同不允许还款"),
    AGREEMENT_BUSINESS_EXCEPTION_CAPITAL_DEDUCT_SUCCESS(10018,"当前合同已还款"),
    AGREEMENT_BUSINESS_EXCEPTION_NOT_ALLOW_REPAY_BEF_REPAY_DAY(10019,"业务异常"),
    AGREEMENT_BUSINESS_EXCEPTION_NOT_ALLOW_REPAY_AT_REPAY_DAY(10020,"当前合同不允许还款"),
    AGREEMENT_BUSINESS_EXCEPTION_CAPITAL_DEDUCTING(10021,"您有一笔在途订单，请稍后再试"),
    AGREEMENT_BUSINESS_EXCEPTION_NO_PLAN_ALLOW_REPAY(10022,"当前合同不允许还款"),
    AGREEMENT_BUSINESS_EXCEPTION_WAIT_BUILD_REPAYPLAN(10023,"系统处理中，请稍后再试"),
    AGREEMENT_BUSINESS_EXCEPTION_GREAT_THAN_MAX_AMT(10024,"金额超出最大可还金额，请重新输入"),
    AGREEMENT_BUSINESS_EXCEPTION_QUERY_PAY_ORDER_FAIL(10025,"系统处理中，请稍后再试"),
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
