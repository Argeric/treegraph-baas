package org.treegraph.baas.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class PaymentResultVo {
	
	@ApiModelProperty(value = "支付状态，0-订单支付中，1-支付成功，2-支付失败， 3-待支付， 4-预下单，5-下单成功，6-下单失败")
    private Integer paymentResultCode;
    
	@ApiModelProperty(value = "支付状态描述")
    private String paymentResultDesc;
}