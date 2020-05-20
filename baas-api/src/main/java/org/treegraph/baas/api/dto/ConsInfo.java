package org.treegraph.baas.api.dto;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConsInfo {
	
	private String id;

	private String name;
	
	private String remark;
	
	private String contact;
	
	private String phone;
	
	private String email;
	
	@JSONField(serialize = false)
	private String avatar;
	
	private String createUser;
	
	private String createTime;
	
	private Integer status;
	
	private Integer step;
}
