package org.treegraph.baas.api.dto;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.Builder;
import lombok.Data;

/**
 * @author zt
 * @date May 11, 2020
 * @version 1.0
 */
@Data
@Builder
public class BlockchainInfo {

	private String id;//区块链id	
	private String name;//区块链名称	
	private String consId;//联盟id	
	private String consName;//联盟名称
	@JSONField(serialize = false)
	private String consAvatar;//联盟头像	
	private String createUser;//创建人	
	private String createTime;//创建时间	
	private Integer status;//区块链状态	
	private String nodeCount;//节点数	
	private String tps;//TPS	
	private String contractCount;//合约数	
	private String txCount;//日新增交易
	private String alertEmail;//报警邮箱
}
