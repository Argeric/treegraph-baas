package org.treegraph.baas.core.das.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.treegraph.baas.common.enums.IntTypeEnum;
import org.treegraph.baas.core.das.BlockchainDas;
import org.treegraph.baas.core.domain.BlockChainPo;
import org.treegraph.baas.core.domain.BlockChainPoExample;
import org.treegraph.baas.core.mapper.BlockChainPoMapper;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class BlockchainDasImpl implements BlockchainDas{
	
    private BlockChainPoMapper blockChainPoMapper;
    
    public Optional<BlockChainPo> findById(String id) {
    	return blockChainPoMapper.selectByPrimaryKey(id);
    }
    
    public List<BlockChainPo> findByConsId(String consId) {
    	BlockChainPoExample example = new BlockChainPoExample();
    	example.createCriteria().andConsortiumIdEqualTo(consId).andDeleteFlagEqualTo(IntTypeEnum.FALSE.getValue());
    	return blockChainPoMapper.selectByExample(example);
    }
    
    public List<BlockChainPo> findByIds(List<String> ids){
    	BlockChainPoExample example = new BlockChainPoExample();
		example.createCriteria().andIdIn(ids);
		example.setOrderByClause("create_time desc");
		return blockChainPoMapper.selectByExample(example);
    }
    
	public List<BlockChainPo> findAll() {
		BlockChainPoExample example = new BlockChainPoExample();
		example.createCriteria().andDeleteFlagEqualTo(IntTypeEnum.FALSE.getValue());
		example.setOrderByClause("create_time desc");
		return blockChainPoMapper.selectByExample(example);
	}

    public boolean createBlockchain(BlockChainPo blockchain) {
    	return blockChainPoMapper.insert(blockchain) > 0;
    }

	public boolean modify(BlockChainPo blockChainPo) {
		return blockChainPoMapper.updateByPrimaryKeySelective(blockChainPo) > 0;
	}
}
