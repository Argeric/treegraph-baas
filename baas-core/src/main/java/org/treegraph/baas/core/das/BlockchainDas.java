package org.treegraph.baas.core.das;

import java.util.List;
import java.util.Optional;

import org.treegraph.baas.core.domain.BlockChainPo;

public interface BlockchainDas {
	
	List<BlockChainPo> findByIds(List<String> ids);
	
	Optional<BlockChainPo> findById(String id);
	
	List<BlockChainPo> findAll();
	
	List<BlockChainPo> findByConsId(String consId);
	
	boolean createBlockchain(BlockChainPo blockchain);
	
	boolean modify(BlockChainPo blockChainPo);
}
