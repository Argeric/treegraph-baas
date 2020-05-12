package org.treegraph.baas.core.util.mpay;

import static com.google.common.base.Preconditions.checkArgument;

import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.treegraph.baas.core.constant.MpayConsts;
import org.treegraph.baas.core.enums.PayResultEnum;
import org.treegraph.baas.core.exception.BusinessException;

import com.google.common.base.Strings;

/**
 * 支付收银台工具类
 */
public class MpayUtil {
    private MpayUtil() {
        // 工具类不允许实例化
    }

    /**
     * 计算支付结果回调中的signInfo合法的取值
     *
     * @param mpayMerchantId 平台商户号
     * @param flowId 流水号
     * @return signInfo合法的取值
     */
    public static String calcPaymentResultCallbackSignInfo(String mpayMerchantId, String flowId) {
        checkArgument(!Strings.isNullOrEmpty(mpayMerchantId));
        checkArgument(!Strings.isNullOrEmpty(flowId));

        String info = mpayMerchantId + flowId;
        return new Md5PasswordEncoder().encodePassword(info, MpayConsts.PAYMENT_RESULT_CALLBACK_SIGN_INFO_PARAM);
    }

    public static PayResultEnum convertMpayPayStatusToPayResultEnum(String mpayPayStatus) {
    	
        checkArgument(!Strings.isNullOrEmpty(mpayPayStatus));
        PayResultEnum result = null;
        
        switch (mpayPayStatus) {
            case MpayConsts.PAY_STATUS_SUC:
                result = PayResultEnum.SUCCESS;
                break;
            case MpayConsts.PAY_STATUS_FAIL:
                result = PayResultEnum.FAILED;
                break;
            case MpayConsts.PAY_STATUS_UNPAID:
                result = PayResultEnum.UNPAID;
                break;
            case MpayConsts.PAY_STATUS_ONGOING:
                result = PayResultEnum.PAYING;
                break;
            case MpayConsts.PAY_STATUS_CREATE_ORDER:
                result = PayResultEnum.ORDER_CREATED;
                break;
            case MpayConsts.PAY_STATUS_CREATE_ORDER_SUC:
                result = PayResultEnum.ORDER_CREATED_SUC;
                break;
            case MpayConsts.PAY_STATUS_CREATE_ORDER_FAIL:
                result = PayResultEnum.ORDER_CREATED_FAIL;
                break;
            default:
                throw new BusinessException(-1, "支付结果取值超出处理范围");
        }
        
        return result;
    }
}