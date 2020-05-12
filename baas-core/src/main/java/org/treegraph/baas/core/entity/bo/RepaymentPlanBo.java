package org.treegraph.baas.core.entity.bo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

/**
 * 还款计划
 * @author GUANDI
 *
 */
@Data
public class RepaymentPlanBo implements Serializable, Comparable<RepaymentPlanBo>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private Integer termNum; 			//期号
	private String status;				//状态
	private Date	payDate;			//还款日
	BigDecimal totalAmount;				//总剩余应还
	BigDecimal unpaidAmount;			//剩余应还
	BigDecimal overdueAmount;			//逾期费用
	@Override
	public int compareTo(RepaymentPlanBo o) {
		return this.getTermNum() - o.getTermNum();
	}
}
