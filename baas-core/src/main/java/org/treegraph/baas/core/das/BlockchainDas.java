package org.treegraph.baas.core.das;

import java.util.List;
import java.util.Optional;

import org.treegraph.baas.core.domain.BlockChainPo;
import org.treegraph.baas.core.domain.ConsortiumPo;

public interface BlockchainDas {
	
	List<BlockChainPo> findByIds(List<String> ids);
	
	Optional<BlockChainPo> findById(String id);
	
	List<BlockChainPo> findAll(String id, String consId);
	
	List<BlockChainPo> findByConsId(String consId);
	
	List<BlockChainPo> findByConsIds(List<String> consId);
	
	long count();
	
	boolean createBlockchain(BlockChainPo blockchain, ConsortiumPo cons);
	
	boolean modify(BlockChainPo blockChainPo);
}
