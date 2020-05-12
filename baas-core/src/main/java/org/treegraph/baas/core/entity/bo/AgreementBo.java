package org.treegraph.baas.core.entity.bo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


import lombok.Data;


@Data
public class AgreementBo implements Serializable{
	private static final long serialVersionUID = 1L;
	String agreementNo;			//合同号
	String clientName;			//主贷人姓名
	String clientIdCardNo;			//主贷人身份证号
	BigDecimal loanAmount;			//借款金额
	Date agreementApplyDate;			//合同申请日期
	BigDecimal currentUnpaidAmount;		//当前应还
	BigDecimal totalUnpaidAmount;		//总剩余应还
	BigDecimal totalRepaidAmount;			//总已还
	BigDecimal currentTermAmount;		//当期期款
	BigDecimal maximumRepayAmount;			//最大可还款金额
	int channel;		//产品类型
	int status;			//合同状态，1表示已激活，2表示已逾期
	int agreementSource;			//合同来源，1表示新核心，2表示老核心
	List<RepaymentPlanBo> repaymentPlanList;	//还款计划
}
