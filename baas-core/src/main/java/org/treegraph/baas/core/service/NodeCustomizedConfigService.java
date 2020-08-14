package org.treegraph.baas.core.service;

import java.util.List;
import java.util.Map;

import org.treegraph.baas.core.domain.NodePo;

public interface NodeCustomizedConfigService {
	Map<String, Integer> getJsonRpcHttpPortMap(List<NodePo> nodes);
}
