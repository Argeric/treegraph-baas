package org.treegraph.baas.core.das.impl;

import java.util.List;

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
    
	public List<BlockChainPo> findByConsId(String consId) {
		BlockChainPoExample example = new BlockChainPoExample();
		example.createCriteria().andConsortiumIdEqualTo(consId).andDeleteFlagEqualTo(IntTypeEnum.FALSE.getValue());
		return blockChainPoMapper.selectByExample(example);
	}
  
    public boolean createBlockchain(BlockChainPo blockchain) {
    	
    	return blockChainPoMapper.insert(blockchain) > 0;
    }
    
}
