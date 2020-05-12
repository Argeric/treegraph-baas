package org.treegraph.baas.web.vo;

import java.io.Serializable;
import java.math.BigDecimal;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class RepaymentPlanVo implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value = "当前期数")
	private Integer termNo; 			//当前期数
	
	@ApiModelProperty(value = "总期数")
	private Integer termTotal;          //总期数
	
	@ApiModelProperty(value = "状态描述")
	private String statusDesc;				//状态描述
	
	@ApiModelProperty(value = "状态")
	private Integer status;				//状态
	
	@ApiModelProperty(value = "还款日")
	private String repayDate;			//还款日
	
	@ApiModelProperty(value = "总应还")
	private BigDecimal totalAmount;				//总应还
	
	@ApiModelProperty(value = "剩余应还")
	private BigDecimal unpaidAmount;			//剩余应还
	
	@ApiModelProperty(value = "逾期费用")
	private BigDecimal overdueAmount;			//逾期费用
	
	@ApiModelProperty(value = "标色，1-标蓝，2-标红")
	private Integer tipColor;			//标色，1-标蓝，2-标红
}
