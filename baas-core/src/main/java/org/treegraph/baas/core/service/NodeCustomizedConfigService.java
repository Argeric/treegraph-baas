package org.treegraph.baas.core.service;

import java.util.List;

import org.treegraph.baas.core.domain.NodeCustomizedConfigPo;
import org.treegraph.baas.core.domain.NodePo;

public interface NodeCustomizedConfigService {
	
	List<NodeCustomizedConfigPo> getAllProperties(List<NodePo> nodes);
	
	String getPropertyValueByName(String consId, String nodeId, String propertyName);
}
