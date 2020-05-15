package org.treegraph.baas.core.das.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.treegraph.baas.common.enums.IntTypeEnum;
import org.treegraph.baas.core.das.NodeDas;
import org.treegraph.baas.core.domain.NodePo;
import org.treegraph.baas.core.domain.NodePoExample;
import org.treegraph.baas.core.domain.NodePoExample.Criteria;
import org.treegraph.baas.core.mapper.NodePoMapper;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class NodeDasImpl implements NodeDas{
	
    private NodePoMapper nodePoMapper;
    
    public List<NodePo> findByConsIdAndChainId(String consId, String chainId) {
		NodePoExample example = new NodePoExample();
		example.createCriteria()
		.andConsortiumIdEqualTo(consId)
		.andBlockchainIdEqualTo(chainId)
		.andDeleteFlagEqualTo(IntTypeEnum.FALSE.getValue());
		return nodePoMapper.selectByExample(example);
	}
    
    public List<NodePo> findByChainIds(List<String> chainIds) {
		NodePoExample example = new NodePoExample();
		example.createCriteria().andBlockchainIdIn(chainIds);
		return nodePoMapper.selectByExample(example);
	}
    
    public boolean addNodes(List<NodePo> list) {
    	return nodePoMapper.bulkInsert(list) == list.size();
    }
    
    public NodePo findById(String id) {
    	return nodePoMapper.selectByPrimaryKey(id);
    }
    
    public List<NodePo> findNodes(String consId, String ip){
    	
    	NodePoExample example = new NodePoExample();
		Criteria criteria = example.createCriteria();
		if(consId != null) {
			criteria.andConsortiumIdEqualTo(consId);
		}
		if(ip != null) {
			criteria.andNodeIpEqualTo(ip);
		}
		return nodePoMapper.selectByExample(example);
    }
    
    public boolean modifyNode(NodePo node) {
    	return nodePoMapper.updateByPrimaryKeySelective(node) > 0;
    }
}
