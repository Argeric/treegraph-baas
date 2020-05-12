package org.treegraph.baas.web.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class LoginResultVo implements Serializable {
	
	private static final long serialVersionUID = -6958283184983406484L;

	@ApiModelProperty(value = "客户唯一标示")
	private String userId;

	@ApiModelProperty(value = "会话token")
    private String token;

	@ApiModelProperty(value = "登录类型，1-自营，2-委外，3-本人")
    private Integer entryCode;

	@ApiModelProperty(value = "是否已绑定主贷人", hidden = true)
    private Boolean boundToDebtor;

	@ApiModelProperty(value = "产品code")
    private String version; //productCode;
}
