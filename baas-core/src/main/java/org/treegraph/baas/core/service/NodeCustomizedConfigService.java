package org.treegraph.baas.core.service;

import java.util.List;
import java.util.Map;

import org.treegraph.baas.core.domain.NodeCustomizedConfigPo;
import org.treegraph.baas.core.domain.NodePo;

public interface NodeCustomizedConfigService {
	
	Map<String, Integer> getJsonRpcHttpPortMap(List<NodePo> nodes);
	
	List<NodeCustomizedConfigPo> getCustomizedConfig(List<NodePo> nodes);
	
	String getPropertyValueByName(String consId, String propertyName);
}
