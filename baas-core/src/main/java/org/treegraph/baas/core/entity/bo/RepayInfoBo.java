package org.treegraph.baas.core.entity.bo;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class RepayInfoBo {
    /**
     * 合同号
     */
    private String agreementNo;
    /**
     * 合同来源，1表示新核心，2表示老核心
     */
    private Integer agreementSource;
    /**
     * 还款订单金额
     */
    private BigDecimal payAmount;
    /**
     * 产品类型
     */
    private Integer channel;
    /**
     * 是否本人支付: 0,否; 1, 是;
     */
    private Integer isSelf;
    /**
     * 客户id（client表id字段）
     */
    private Long clientId;
    /**
     * 借款人身份证号
     */
    private String idCardNo;
    /**
     * 借款人姓名
     */
    private String idCardName;
    /**
     * 产品合同查询码
     */
    private String productCode;
    /**
     * 还款期号
     */
	private Integer	termNo;
}
