package org.treegraph.baas.api.dto.request;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.Data;

@Data
public class CreateConsRequest {

	private String name;
	
	private String remark;
	
	private String contact;
	
	private String phone;
	
	private String email;
	
	@JSONField(serialize = false)
	private String avatar;
}
