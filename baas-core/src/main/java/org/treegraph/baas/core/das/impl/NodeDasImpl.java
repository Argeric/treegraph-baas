package org.treegraph.baas.core.das.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.treegraph.baas.common.enums.IntTypeEnum;
import org.treegraph.baas.core.das.NodeDas;
import org.treegraph.baas.core.domain.NodePo;
import org.treegraph.baas.core.domain.NodePoExample;
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
    
    public boolean addNodes(List<NodePo> list) {
    	return nodePoMapper.bulkInsert(list) == list.size();
    }
}
