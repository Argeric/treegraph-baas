package org.treegraph.baas.core.das;

import java.util.List;

import org.treegraph.baas.core.domain.NodeCustomizedConfigPo;

public interface NodeCustomizedConfigDas {
	
	List<NodeCustomizedConfigPo> findAll(String consId, List<String> nodeIds);
	
	NodeCustomizedConfigPo findConfigByPropertyName(String consId, String propertyName);
	
	String findPropertyValueByPropertyName(String consId, String propertyName);
	
	boolean add(NodeCustomizedConfigPo nodeCustomizedConfig);
	
	boolean batchAdd(List<NodeCustomizedConfigPo> customizedConfigList);
	
	boolean modify(NodeCustomizedConfigPo nodeCustomizedConfig);
}
