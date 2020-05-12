package org.treegraph.baas.core.constant;

import java.util.HashMap;
import java.util.Map;

public class RepaymentPlanConsts {
	
	public static final Map<Integer, String> NEWCORE_REPAYMENTPLAN_STATUS = new HashMap<Integer, String>();
	static{
		NEWCORE_REPAYMENTPLAN_STATUS.put(0, "还款中");
		NEWCORE_REPAYMENTPLAN_STATUS.put(2, "未开始");
		NEWCORE_REPAYMENTPLAN_STATUS.put(1, "已结清");
		NEWCORE_REPAYMENTPLAN_STATUS.put(3, "已结清");
		NEWCORE_REPAYMENTPLAN_STATUS.put(5, "已结清");		
		NEWCORE_REPAYMENTPLAN_STATUS.put(8, "已结清");		
		NEWCORE_REPAYMENTPLAN_STATUS.put(4, "已逾期");		
		NEWCORE_REPAYMENTPLAN_STATUS.put(6, "已逾期");		
	}
	
	
}
