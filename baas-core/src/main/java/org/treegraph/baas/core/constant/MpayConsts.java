package org.treegraph.baas.core.constant;

/**
 * 支付收银台有关常量
 */
public class MpayConsts {
	
	public static final String CREATE_ORDER_TRADE_CODE = "100004";
	public static final String QUERY_ORDER_TRADE_CODE = "100005";
	public static final String REPAY_ORDER_CLOSE_CODE = "100012";

    public static final String PAYMENT_RESULT_CALLBACK_SIGN_INFO_PARAM = "123321";
    
    public static final String PAYMENT_SUCCESS_RESULT = "0000";

    public static final String PAY_STATUS_CREATE_ORDER = "17"; // 预下单
    public static final String PAY_STATUS_CREATE_ORDER_SUC = "18"; // 下单成功
    public static final String PAY_STATUS_CREATE_ORDER_FAIL = "19"; // 下单失败
    public static final String PAY_STATUS_UNPAID = "20"; // 待支付
    public static final String PAY_STATUS_ONGOING  = "21";// 支付中
    public static final String PAY_STATUS_SUC = "22";// 支付成功
    public static  final String PAY_STATUS_FAIL = "23";// 支付失败

    public static final String CREATE_PAY_ORDER_URL = "/cashier/pay/order";
    public static final String QUERY_PAY_ORDER_URL = "/cashier/pay/query";
    public static final String CLOSE_PAY_ORDER_URL = "/cashier/pay/payClose";

    public static final String RESP_CODE_SUC = "200"; // 200:通讯成功
    public static final String RESP_CODE_RPC_FAIL = "400"; // 400:通讯失败
    public static final String RESP_CODE_AUTH_FAIL = "401"; // 401:认证失败
    public static final String RESP_CODE_INTERNAL_ERR = "500"; // 500:内部错误
}