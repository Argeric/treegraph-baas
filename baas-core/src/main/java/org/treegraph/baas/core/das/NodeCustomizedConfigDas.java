package org.treegraph.baas.core.das;

import java.util.List;

import org.treegraph.baas.core.domain.NodeCustomizedConfigPo;

public interface NodeCustomizedConfigDas {
	List<NodeCustomizedConfigPo> findAll(String consId, List<String> nodeIds);
	
	NodeCustomizedConfigPo findConfigByPropertyName(String consId, String nodeId, String propertyName);
	
	//List<NodeCustomizedConfigPo> findConfigByPropertyName(List<String> nodeIds, String propertyName);
	
	String findPropertyValueByName(String consId, String nodeId, String propertyName);
	
	boolean add(NodeCustomizedConfigPo nodeCustomizedConfig);
	
	boolean batchAdd(List<NodeCustomizedConfigPo> customizedConfigList);
	
	boolean modify(NodeCustomizedConfigPo nodeCustomizedConfig);
}
