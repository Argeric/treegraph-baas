package org.treegraph.baas.core.das.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.treegraph.baas.common.enums.IntTypeEnum;
import org.treegraph.baas.core.das.NodeCustomizedConfigDas;
import org.treegraph.baas.core.domain.NodeCustomizedConfigPo;
import org.treegraph.baas.core.domain.NodeCustomizedConfigPoExample;
import org.treegraph.baas.core.domain.NodeCustomizedConfigPoExample.Criteria;
import org.treegraph.baas.core.domain.NodePo;
import org.treegraph.baas.core.mapper.primary.NodeCustomizedConfigPoMapper;

import com.google.common.collect.Lists;

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
	
	public List<NodeCustomizedConfigPo> findAll(List<NodePo> nodes) {
		if(CollectionUtils.isEmpty(nodes)) {
			return Lists.newArrayList();
		}
		List<String> nodeIds = nodes.stream().map(NodePo::getId).collect(Collectors.toList());
		return findAll(null, nodeIds);
	}
	
	public NodeCustomizedConfigPo findConfigByPropertyName(String consId, String nodeId, String propertyName) {
		NodeCustomizedConfigPoExample example = new NodeCustomizedConfigPoExample();
		Criteria criteria = example.createCriteria();
		if (StringUtils.isNotBlank(consId)) {
			criteria.andConsortiumIdEqualTo(consId);
		}
		if(StringUtils.isNoneBlank(nodeId)){
			criteria.andNodeIdEqualTo(nodeId);
		} else {
			criteria.andNodeIdEqualTo(IntTypeEnum.FALSE.getValue()+"");
		}
		criteria.andPropertyNameEqualTo(propertyName);
		criteria.andDeleteFlagEqualTo(IntTypeEnum.FALSE.getValue());
		example.setOrderByClause("create_time desc");
		List<NodeCustomizedConfigPo> nodeCustomizedConfigList = nodeCustomizedConfigPoMapper.selectByExample(example);
		return CollectionUtils.isNotEmpty(nodeCustomizedConfigList) 
				? nodeCustomizedConfigList.get(0) : null;
	}
	
	public String findPropertyValueByName(String consId, String nodeId, String propertyName) {
		NodeCustomizedConfigPoExample example = new NodeCustomizedConfigPoExample();
		Criteria criteria = example.createCriteria();
		if (StringUtils.isNotBlank(consId)) {
			criteria.andConsortiumIdEqualTo(consId);
		}
		if(StringUtils.isNoneBlank(nodeId)){
			criteria.andNodeIdEqualTo(nodeId);
		} else {
			criteria.andNodeIdEqualTo(IntTypeEnum.FALSE.getValue()+"");
		}
		criteria.andPropertyNameEqualTo(propertyName);
		criteria.andDeleteFlagEqualTo(IntTypeEnum.FALSE.getValue());
		example.setOrderByClause("create_time desc");
		List<NodeCustomizedConfigPo> nodeCustomizedConfigList = nodeCustomizedConfigPoMapper.selectByExample(example);
		return CollectionUtils.isNotEmpty(nodeCustomizedConfigList) 
				? nodeCustomizedConfigList.get(0).getPropertyValue()
						: null;
	}
	
	public boolean add(NodeCustomizedConfigPo nodeCustomizedConfig) {
		return nodeCustomizedConfigPoMapper.insertSelective(nodeCustomizedConfig) > 0;
	}
	
	public boolean batchAdd(List<NodeCustomizedConfigPo> customizedConfigList) {
		return nodeCustomizedConfigPoMapper.bulkInsert(customizedConfigList) == customizedConfigList.size();
	}
	
	public boolean modify(NodeCustomizedConfigPo nodeCustomizedConfig) {
		return nodeCustomizedConfigPoMapper.updateByPrimaryKeySelective(nodeCustomizedConfig) > 0;
	}
}
