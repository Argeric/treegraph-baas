package org.treegraph.baas.core.das;

import java.util.List;
import java.util.Optional;

import org.treegraph.baas.core.domain.NodePo;

public interface NodeDas {
	
	Optional<NodePo> findById(String id);
	
	List<NodePo> findAll(String consId, String ip);
	
	List<NodePo> findByConsIdAndChainId(String consId, String chainId);
	
	List<NodePo> findByChainIds(List<String> chainIds);
	
	boolean addNodes(List<NodePo> list);

	boolean modifyNode(NodePo node);
}
