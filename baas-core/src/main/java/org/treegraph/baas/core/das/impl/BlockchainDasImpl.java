package org.treegraph.baas.core.das.impl;

import java.util.List;
import java.util.Optional;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.treegraph.baas.common.enums.IntTypeEnum;
import org.treegraph.baas.core.das.BlockchainDas;
import org.treegraph.baas.core.domain.BlockChainPo;
import org.treegraph.baas.core.domain.BlockChainPoExample;
import org.treegraph.baas.core.domain.BlockChainPoExample.Criteria;
import org.treegraph.baas.core.domain.ConsortiumPo;
import org.treegraph.baas.core.mapper.primary.BlockChainPoMapper;
import org.treegraph.baas.core.mapper.primary.ConsortiumPoMapper;

import com.google.common.collect.Lists;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class BlockchainDasImpl implements BlockchainDas{
	
    private BlockChainPoMapper blockChainPoMapper;
    
    private ConsortiumPoMapper consortiumPoMapper;
    
    public Optional<BlockChainPo> findById(String id) {
    	return blockChainPoMapper.selectByPrimaryKey(id);
    }
    
    public List<BlockChainPo> findByConsId(String consId) {
    	BlockChainPoExample example = new BlockChainPoExample();
    	example.createCriteria().andConsortiumIdEqualTo(consId).andDeleteFlagEqualTo(IntTypeEnum.FALSE.getValue());
    	return blockChainPoMapper.selectByExample(example);
    }
    
    public List<BlockChainPo> findByConsIds(List<String> consIds) {
    	if(CollectionUtils.isEmpty(consIds)) {
    		return Lists.newArrayList();
    	}
    	
    	BlockChainPoExample example = new BlockChainPoExample();
    	example.createCriteria().andConsortiumIdIn(consIds).andDeleteFlagEqualTo(IntTypeEnum.FALSE.getValue());
    	return blockChainPoMapper.selectByExample(example);
    }
    
    public List<BlockChainPo> findByIds(List<String> ids){
    	if(CollectionUtils.isEmpty(ids)) {
    		return Lists.newArrayList();
    	}
    	
    	BlockChainPoExample example = new BlockChainPoExample();
		example.createCriteria().andIdIn(ids);
		example.setOrderByClause("create_time desc");
		return blockChainPoMapper.selectByExample(example);
    }
    
	public List<BlockChainPo> findAll(String id, String consId) {
		BlockChainPoExample example = new BlockChainPoExample();
		Criteria criteria = example.createCriteria();
		if(id != null) {
			criteria.andIdEqualTo(id);
		}
		if(consId != null) {
			criteria.andConsortiumIdEqualTo(consId);
		}
		criteria.andDeleteFlagEqualTo(IntTypeEnum.FALSE.getValue());
		
		example.setOrderByClause("create_time desc");
		return blockChainPoMapper.selectByExample(example);
	}
	
	public long count() {
		BlockChainPoExample example = new BlockChainPoExample();
		example.createCriteria().andDeleteFlagEqualTo(IntTypeEnum.FALSE.getValue());
		return blockChainPoMapper.countByExample(example);
	}

    public void createBlockchain(BlockChainPo blockchain, ConsortiumPo cons) {
    	boolean succFlag =  Boolean.TRUE;
    	succFlag = succFlag && blockChainPoMapper.insert(blockchain) > 0;
    	succFlag = succFlag && consortiumPoMapper.updateByPrimaryKeySelective(cons) > 0;
    }

	public boolean modify(BlockChainPo blockChainPo) {
		return blockChainPoMapper.updateByPrimaryKeySelective(blockChainPo) > 0;
	}
}
