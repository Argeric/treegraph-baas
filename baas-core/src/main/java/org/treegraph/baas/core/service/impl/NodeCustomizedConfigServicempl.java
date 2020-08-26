package org.treegraph.baas.core.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.treegraph.baas.core.das.NodeCustomizedConfigDas;
import org.treegraph.baas.core.domain.NodeCustomizedConfigPo;
import org.treegraph.baas.core.domain.NodePo;
import org.treegraph.baas.core.service.NodeCustomizedConfigService;

import com.google.common.collect.Lists;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class NodeCustomizedConfigServicempl implements NodeCustomizedConfigService {
    
	private NodeCustomizedConfigDas nodeCustomizedConfigDas;
 
	public List<NodeCustomizedConfigPo> getAllProperties(List<NodePo> nodes) {
		if(CollectionUtils.isEmpty(nodes)) {
			return Lists.newArrayList();
		}
		List<String> nodeIds = nodes.stream().map(NodePo::getId).collect(Collectors.toList());
		return nodeCustomizedConfigDas.findAll(null, nodeIds);
	}
	
	public String getPropertyValueByName(String consId, String nodeId, String propertyName) {
		return nodeCustomizedConfigDas.findPropertyValueByName(consId, nodeId, propertyName);
	}
	
	
}
