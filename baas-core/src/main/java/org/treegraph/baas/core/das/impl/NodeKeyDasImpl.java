package org.treegraph.baas.core.das.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.treegraph.baas.common.enums.IntTypeEnum;
import org.treegraph.baas.core.das.NodeKeyDas;
import org.treegraph.baas.core.domain.NodeKeyPoExample.Criteria;
import org.treegraph.baas.core.domain.NodeKeyPo;
import org.treegraph.baas.core.domain.NodeKeyPoExample;
import org.treegraph.baas.core.mapper.primary.NodeKeyPoMapper;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class NodeKeyDasImpl implements NodeKeyDas {

	private NodeKeyPoMapper nodeKeyPoMapper;
	
	public boolean save(NodeKeyPo nodeKey){
		return nodeKeyPoMapper.insert(nodeKey) > 0;
	}
	
	public List<NodeKeyPo> findAll(String consId, String nodeId){
		NodeKeyPoExample example = new NodeKeyPoExample();
		Criteria criteria = example.createCriteria();
		if (StringUtils.isNotBlank(consId)) {
			criteria.andConsortiumIdEqualTo(consId);
		}
		if (StringUtils.isNotBlank(nodeId)) {
			criteria.andNodeIdEqualTo(nodeId);
		}
		criteria.andDeleteFlagEqualTo(IntTypeEnum.FALSE.getValue());
		example.setOrderByClause("create_time desc");
		
		return nodeKeyPoMapper.selectByExample(example);
	}
}
