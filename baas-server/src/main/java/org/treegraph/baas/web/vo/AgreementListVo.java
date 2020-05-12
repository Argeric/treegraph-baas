package org.treegraph.baas.web.vo;

import lombok.Data;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@Data
@ApiModel
public class AgreementListVo {
	
	@ApiModelProperty(value = "主贷人姓名")
	String clientName;			//主贷人姓名
	
	@ApiModelProperty(value = "主贷人身份证号")
	String clientIdCardNo;			//主贷人身份证号
	
	@ApiModelProperty(value = "合同列表")
    private List<AgreementVo> agreements;
}
