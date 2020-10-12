package org.treegraph.baas.api.dto;

import lombok.Builder;
import lombok.Data;

/**
 * @author zt
 * @date May 11, 2020
 * @version 1.0
 */
@Data
@Builder
public class ConsAdminInfo {
	
	private String userId;
	
	private String account;

	private String name;
	
	private String email;
	
	private String phone;
	
	private String pwd;
	
	private String org;
}
