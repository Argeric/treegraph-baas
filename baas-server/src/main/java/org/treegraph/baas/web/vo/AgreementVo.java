package org.treegraph.baas.web.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
@ApiModel
public class AgreementVo implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value = "合同号")
	String agreementNo;			//合同号
	
	@ApiModelProperty(value = "合同申请日期")
	String agreementApplyDate;			//合同申请日期
	
	@ApiModelProperty(value = "合同状态描述，已激活/已逾期")
	String statusDesc;			//合同状态描述，已激活/已逾期
	
	@ApiModelProperty(value = "当前期数")
	Integer termNo; 			//当前期数
	
	@ApiModelProperty(value = "总期数")
	Integer termTotal;          //总期数
	
	@ApiModelProperty(value = "当前应还")
	BigDecimal currentUnpaidAmount;		//当前应还
	
	@ApiModelProperty(value = "已还总金额")
	BigDecimal totalRepaidAmount;			//已还总金额
	
	@ApiModelProperty(value = "产品类型")
	Integer channel;		//产品类型
	
	@ApiModelProperty(value = "合同来源，3表示车金融")
	Integer agreementSource;			//合同来源，1表示新核心，2表示老核心，3表示车金融
	
	@ApiModelProperty(value = "还款计划")
	List<RepaymentPlanVo> repaymentPlanList;	//还款计划

}
