package org.treegraph.baas.core.das;

import java.util.List;

import org.treegraph.baas.core.domain.NodePo;

public interface NodeDas {
	
	List<NodePo> findByConsIdAndChainId(String consId, String chainId);
	
	List<NodePo> findByChainIds(List<String> chainIds);
	
	boolean addNodes(List<NodePo> list);
	
	NodePo findById(String id);
	
	List<NodePo> findNodes(String consId, String ip);
	
	boolean modifyNode(NodePo node);
}
