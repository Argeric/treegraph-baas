package org.treegraph.baas.web.param;

import java.math.BigDecimal;

import net.sf.oval.constraint.NotNull;
import lombok.Data;

@Data
public class RepayInfoParam {

	@NotNull(message = "合同号不能为空")
	private String agreementNo;
	
	@NotNull(message = "合同来源不能为空")
	private Integer agreementSource;
	
	@NotNull(message = "还款期号")
	private Integer termNo;
	
	@NotNull(message = "还款金额不能为空")
	private BigDecimal payAmount;
	
	@NotNull(message = "产品渠道不能为空")
	private Integer channel;
	
    @NotNull(message = "是否本人支付不能为空")
    private Integer isSelf;
}
