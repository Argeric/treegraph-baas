package org.treegraph.baas.core.das.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.treegraph.baas.common.enums.IntTypeEnum;
import org.treegraph.baas.core.das.NodeCustomizedConfigDas;
import org.treegraph.baas.core.domain.NodeCustomizedConfigPo;
import org.treegraph.baas.core.domain.NodeCustomizedConfigPoExample;
import org.treegraph.baas.core.domain.NodeCustomizedConfigPoExample.Criteria;
import org.treegraph.baas.core.mapper.primary.NodeCustomizedConfigPoMapper;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class NodeCustomizedConfigDasImpl implements NodeCustomizedConfigDas {

	private NodeCustomizedConfigPoMapper nodeCustomizedConfigPoMapper;
	
	public List<NodeCustomizedConfigPo> findAll(String consId, List<String> nodeIds) {
		NodeCustomizedConfigPoExample example = new NodeCustomizedConfigPoExample();
		Criteria criteria = example.createCriteria();
		if (StringUtils.isNotBlank(consId)) {
			criteria.andConsortiumIdEqualTo(consId);
		}
		if (CollectionUtils.isNotEmpty(nodeIds)) {
			criteria.andNodeIdIn(nodeIds);
		}
		criteria.andDeleteFlagEqualTo(IntTypeEnum.FALSE.getValue());
		example.setOrderByClause("create_time desc");
		return nodeCustomizedConfigPoMapper.selectByExample(example);
	}
}
