package org.treegraph.baas.core.enums;

import java.util.HashMap;
import java.util.Map;

/**
* @Title: RepaymentStatusEnum.java 
* @Package com.mljr.lyqc.beans.constant.em 
* @ClassName: RepaymentStatusEnum 
* @Description: (还款计划状态) 
* @author ma wei long 
* @date 2018年2月24日 上午9:14:14 
* @version V1.0
 */
public enum RepaymentStatusEnum {
	
	NOT_REPAY(1, "未扣款"),
	SUC_REPAY(2, "扣款成功"),
	ADVANCE_REPAY(3, "已提前结清"),
	CANCELCONTRACT(4, "已取消合同"),
	REPAYING(5, "还款中"),
	OVERDUE_REPAY(6, "逾期"),
    ;
    
	public final int code;
    public final String desc;
    
    RepaymentStatusEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static RepaymentStatusEnum getByCode(String code) {
        return codeMap.get(code);
    }

    private static Map<Integer, RepaymentStatusEnum> codeMap = new HashMap<>();

    static {
        for (RepaymentStatusEnum item : RepaymentStatusEnum.values()) {
            codeMap.put(item.code, item);
        }
    }
}
