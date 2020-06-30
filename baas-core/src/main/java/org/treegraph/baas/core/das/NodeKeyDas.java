package org.treegraph.baas.core.das;

import java.util.List;

import org.treegraph.baas.core.domain.NodeKeyPo;

public interface NodeKeyDas {
	
	boolean save(NodeKeyPo nodeKey);
	
	List<NodeKeyPo> findAll(String consId, String nodeId);
}
