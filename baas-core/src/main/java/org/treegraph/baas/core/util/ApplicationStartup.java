package org.treegraph.baas.core.util;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.treegraph.baas.core.constant.ConfluxConstant;
import org.treegraph.baas.core.das.NodeCustomizedConfigDas;
import org.treegraph.baas.core.das.NodeDas;
import org.treegraph.baas.core.domain.NodeCustomizedConfigPo;
import org.treegraph.baas.core.domain.NodePo;
import org.treegraph.baas.core.heartbeat.bo.Heartbeat;
import org.treegraph.baas.core.heartbeat.impl.HeartbeatLinstener;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ApplicationStartup implements ApplicationListener<ContextRefreshedEvent> {
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		
		// get nodes
		ApplicationContext ac = event.getApplicationContext();
		NodeDas nodeDas = ac.getBean(NodeDas.class);
		List<NodePo> nodes = nodeDas.findAll(null, null);
		if(CollectionUtils.isEmpty(nodes)) return;
		
		// get customized config
		List<String> nodeIds = nodes.stream().map(NodePo::getId).collect(Collectors.toList());
		if(CollectionUtils.isEmpty(nodeIds)) return;
		NodeCustomizedConfigDas customizedConfigDas = ac.getBean(NodeCustomizedConfigDas.class);
		List<NodeCustomizedConfigPo> customizedConfigList = customizedConfigDas.findAll(null, nodeIds);	
		Map<String, Integer> rpcHttpPortMap = customizedConfigList.stream()
				.filter(item -> item.getPropertyName().equalsIgnoreCase(ConfluxConstant.PROPERTY_JSON_RPC_HTTP_PORT))
				.collect(Collectors.toMap(NodeCustomizedConfigPo::getNodeId, item -> Integer.parseInt(item.getPropertyValue())));
		
		// register nodes
		HeartbeatLinstener heartbeatListener = HeartbeatLinstener.getInstance();
		nodes.stream().forEach(node -> {
			if(heartbeatListener.isNodeValid(node.getConsortiumId(), node.getId())){
				Heartbeat nodeInfo = Heartbeat.builder()
						.id(node.getId())
						.ip(node.getNodeIp())
						.port(rpcHttpPortMap.get(node.getId())).build();
				heartbeatListener.registerNode(node.getConsortiumId(), node.getId(), nodeInfo);
			}
		});
		log.info("ApplicationStartup.onApplicationEvent completed!");
	}
}
