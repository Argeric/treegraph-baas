package org.treegraph.baas.core.das;

import java.util.List;
import java.util.Optional;

import org.treegraph.baas.core.das.bo.NodeKeypairBo;
import org.treegraph.baas.core.domain.ConsortiumPo;
import org.treegraph.baas.core.domain.NodePo;
import org.treegraph.baas.core.enums.NodeEnum;

public interface NodeDas {
	
	Optional<NodePo> findById(String id);
	
	List<NodePo> findAll(String consId, String ip);
	
	List<NodePo> findByConsIdAndChainId(String consId, String chainId);
	
	List<NodePo> findByChainIds(List<String> chainIds);
	
	NodePo findByIpPort(String ip, Integer port);
	
	long count();
	
	boolean addNodes(List<NodePo> list, ConsortiumPo cons);
	
	boolean addNodeKeypairs(List<NodeKeypairBo> nodeKeypairBoList);

	boolean modifyNode(NodePo node);
	
	boolean modifyNodesStatus(List<String> nodeIds, NodeEnum.Status status);
	
	boolean deleteNodes(NodePo node, ConsortiumPo cons);
	
	Integer deleteByIpPort(String ip, Integer port);
}
