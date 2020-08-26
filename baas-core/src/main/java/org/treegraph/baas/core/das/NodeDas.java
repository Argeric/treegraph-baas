package org.treegraph.baas.core.das;

import java.util.List;
import java.util.Optional;

import org.treegraph.baas.core.das.bo.NodeKeypairBo;
import org.treegraph.baas.core.domain.ConsortiumPo;
import org.treegraph.baas.core.domain.NodeCustomizedConfigPo;
import org.treegraph.baas.core.domain.NodePo;
import org.treegraph.baas.core.enums.NodeEnum;

public interface NodeDas {
	
	Optional<NodePo> findById(String id);
	
	List<NodePo> findAll(String consId, String ip);
	
	List<NodePo> findByConsIdAndChainId(String consId, String chainId, boolean isAlive);
	
	List<NodePo> findByChainIds(List<String> chainIds);
	
	NodePo findByIpPort(String ip, Integer port);
	
	long count();
	
	boolean addNodes(List<NodePo> list, ConsortiumPo cons, NodeCustomizedConfigPo stepFactorConfig,
			List<NodeCustomizedConfigPo> customizedConfigList);
	
	boolean addNodeKeypairs(List<NodeKeypairBo> nodeKeypairBoList, NodeCustomizedConfigPo stepFactorConfig,
			List<NodeCustomizedConfigPo> customizedConfigList);

	boolean modifyNode(NodePo node);
	
	boolean modifyNodesStatus(List<String> nodeIds, NodeEnum.Status status);
	
	boolean deleteNodes(NodePo node, ConsortiumPo cons);
	
	Integer deleteByIpPort(String ip, Integer port);
}
