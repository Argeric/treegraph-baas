package org.treegraph.baas.api.dto.request;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateConsRequest {

	private String name;
	
	private String remark;
	
	private String contact;
	
	private String phone;
	
	private String email;
	
	@JSONField(serialize = false)
	private String avatar;
}
