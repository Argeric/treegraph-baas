package org.treegraph.baas.core.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.treegraph.baas.core.constant.ConfluxConstant;
import org.treegraph.baas.core.das.NodeCustomizedConfigDas;
import org.treegraph.baas.core.domain.NodeCustomizedConfigPo;
import org.treegraph.baas.core.domain.NodePo;
import org.treegraph.baas.core.service.NodeCustomizedConfigService;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class NodeCustomizedConfigServicempl implements NodeCustomizedConfigService {
    
	private NodeCustomizedConfigDas nodeCustomizedConfigDas;
 
	public Map<String, Integer> getJsonRpcHttpPortMap(List<NodePo> nodes) {
		if(CollectionUtils.isEmpty(nodes)) {
			return Maps.newHashMap();
		}
		String consId = nodes.get(0).getConsortiumId();
		List<String> nodeIds = nodes.stream().map(NodePo::getId).collect(Collectors.toList());
		List<NodeCustomizedConfigPo> customizedConfigs = nodeCustomizedConfigDas.findAll(consId, nodeIds);
		return customizedConfigs.stream()
				.filter(item -> item.getPropertyName().equalsIgnoreCase(ConfluxConstant.PROPERTY_JSON_RPC_HTTP_PORT))
				.collect(Collectors.toMap(NodeCustomizedConfigPo::getNodeId, item -> Integer.parseInt(item.getPropertyValue())));
	}
	
	public List<NodeCustomizedConfigPo> getCustomizedConfig(List<NodePo> nodes) {
		if(CollectionUtils.isEmpty(nodes)) {
			return Lists.newArrayList();
		}
		String consId = nodes.get(0).getConsortiumId();
		List<String> nodeIds = nodes.stream().map(NodePo::getId).collect(Collectors.toList());
		return nodeCustomizedConfigDas.findAll(consId, nodeIds);
	}
	
	public String getPropertyValueByPropertyName(String consId, String propertyName) {
		return nodeCustomizedConfigDas.findPropertyValueByPropertyName(consId, propertyName);
	}
	
	
}
