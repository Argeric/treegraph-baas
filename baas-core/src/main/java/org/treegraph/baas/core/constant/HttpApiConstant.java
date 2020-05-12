package org.treegraph.baas.core.constant;

/**
 * @description: http 接口
 * @author: Guo Lixiao
 * @date: 2018-2-7 11:09
 * @see:
 * @since:
 **/
public interface HttpApiConstant {

    /**
     * 账务接口
     */
    interface Fin {

        /**
         * 根据身份证查询主贷人手机号
         **/
        String GETMOBILESBYIDCARD = "/api/getMobileByIdNo";

        /**
         * 查询还款计划
         **/
        String GETREPAYMENT = "/api/getRepayment";

        /**
         * 查询还款记录
         **/
        String GETREPAYMENTDETAIL = "/api/getRepaymentDetail";

        /**
         * 查询客户本期是否可还
         **/
        String CHECKCANREPAY = "/api/checkCanRepay";

        /**
         * 查询还款卡
         **/
        String GETREPAYMENTCARDNUMBYAPPNO = "/api/getRepaymentCardNumByAppNo";

        /**
         * 请求当期还款
         **/
        String LOCKREPAYMENT = "/api/lockRepayment";

        /**
         * 根据合同号查询还款计划
         **/
        String GETREPAYMENTBYAPPNO = "/api/getRepaymentByAppNo";
        
        /**
         * 同步支付结果
         **/
        String dealRepayment4cResutSync = "/api/dealRepayment4cResult4Sync";

    }

    /**
     * 力蕴接口
     */
    interface Liyun {

        /**
         * 查询秒距信息
         */
        String GETSECONDREFUSAL = "/car/outside/getSecondRefusalInfos";

        /**
         * 查询订单
         */
        String GETORDER = "/car/outside/getOrderInfos";
    }
}
