package org.treegraph.baas.core.das;

import java.util.List;

import org.treegraph.baas.core.domain.NodePo;

public interface NodeDas {
	
	List<NodePo> findByConsIdAndChainId(String consId, String chainId);
	
	boolean addNodes(List<NodePo> list);
}
