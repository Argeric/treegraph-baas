package org.treegraph.baas.core.das.impl;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.treegraph.baas.common.enums.IntTypeEnum;
import org.treegraph.baas.core.das.NodeDas;
import org.treegraph.baas.core.domain.NodePo;
import org.treegraph.baas.core.domain.NodePoExample;
import org.treegraph.baas.core.domain.NodePoExample.Criteria;
import org.treegraph.baas.core.mapper.NodePoMapper;
import org.treegraph.baas.core.util.CollectionUtils;

import com.google.common.collect.Lists;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class NodeDasImpl implements NodeDas {

	private NodePoMapper nodePoMapper;

	public Optional<NodePo> findById(String id) {
		return nodePoMapper.selectByPrimaryKey(id);
	}
	
	public List<NodePo> findAll(String consId, String ip) {

		NodePoExample example = new NodePoExample();
		Criteria criteria = example.createCriteria();
		if (StringUtils.isNotBlank(consId)) {
			criteria.andConsortiumIdEqualTo(consId);
		}
		if (StringUtils.isNotBlank(ip)) {
			criteria.andNodeIpEqualTo(ip);
		}
		criteria.andDeleteFlagEqualTo(IntTypeEnum.FALSE.getValue());
		example.setOrderByClause("create_time desc");
		return nodePoMapper.selectByExample(example);
	}

	public List<NodePo> findByConsIdAndChainId(String consId, String chainId) {
		NodePoExample example = new NodePoExample();
		example.createCriteria().andConsortiumIdEqualTo(consId).andBlockchainIdEqualTo(chainId)
				.andDeleteFlagEqualTo(IntTypeEnum.FALSE.getValue());
		example.setOrderByClause("create_time desc");
		return nodePoMapper.selectByExample(example);
	}

	public List<NodePo> findByChainIds(List<String> chainIds) {
		if(CollectionUtils.isEmpty(chainIds)) {
			return Lists.newArrayList();
		}
		NodePoExample example = new NodePoExample();
		example.createCriteria().andBlockchainIdIn(chainIds).andDeleteFlagEqualTo(IntTypeEnum.FALSE.getValue());
		example.setOrderByClause("create_time desc");
		return nodePoMapper.selectByExample(example);
	}

	public boolean addNodes(List<NodePo> list) {
		return nodePoMapper.bulkInsert(list) == list.size();
	}

	public boolean modifyNode(NodePo node) {
		return nodePoMapper.updateByPrimaryKeySelective(node) > 0;
	}
}
