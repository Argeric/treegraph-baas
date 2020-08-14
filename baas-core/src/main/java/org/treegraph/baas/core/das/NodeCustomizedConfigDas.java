package org.treegraph.baas.core.das;

import java.util.List;

import org.treegraph.baas.core.domain.NodeCustomizedConfigPo;

public interface NodeCustomizedConfigDas {
	List<NodeCustomizedConfigPo> findAll(String consId, List<String> nodeIds);
}
