package org.treegraph.baas.core.entity.bo;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class RepayOrderBo {
    private Long       id;
    private String     agreementNo;
    private Integer    channelCode;
    private Long       clientId;
    private String     idCardNo;
    private Integer    agreementSource;
    private String     repayOrderNo;
    private BigDecimal amount;
    private String     payOrderNo;
    private Integer    payResult;
    private Long       repaymentConnectionId;
    private Integer    oldCoreActiveRepayResult;
    private Date       oldCoreActiveRepayTime;
    private Integer source;// 订单来源: 1, 自营催收; 2, 委外催收; 3, 本人还款
    private Integer payType;// 支付方式: 0,支付宝; 1, 微信; 2, 快捷; 3, 代扣封装快捷
    private String bankAccountNo;
    private String bankName;
    private Date       createdTime;
    private Date       modifiedTime;
}
