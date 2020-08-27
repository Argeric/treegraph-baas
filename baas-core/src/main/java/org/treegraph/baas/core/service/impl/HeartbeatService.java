package org.treegraph.baas.core.service.impl;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.treegraph.baas.core.component.BizThreadPools;
import org.treegraph.baas.core.constant.ConfluxConstant;
import org.treegraph.baas.core.das.NodeCustomizedConfigDas;
import org.treegraph.baas.core.das.NodeDas;
import org.treegraph.baas.core.domain.NodeCustomizedConfigPo;
import org.treegraph.baas.core.domain.NodePo;
import org.treegraph.baas.core.enums.NodeEnum;
import org.treegraph.baas.core.service.bo.NodeBo;
import org.treegraph.baas.core.util.ConfluxUtils;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import conflux.web3j.Cfx;
import conflux.web3j.Request;
import conflux.web3j.response.Status;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class HeartbeatService {
	
    private NodeDas nodeDas;
    
    private NodeCustomizedConfigDas nodeCustomizedConfigDas;
	
	public Map<String, NodeBo> batchNodeHeartbeatPlus(List<NodePo> nodes) {
		if(CollectionUtils.isEmpty(nodes)) {
			return Maps.newHashMap();
		}

		List<NodeCustomizedConfigPo> customizedConfigs = nodeCustomizedConfigDas.findAll(nodes);	
		Map<String, Integer> nodeIdRpcHttpPortMap = customizedConfigs.stream()
				.filter(item -> item.getPropertyName().equalsIgnoreCase(ConfluxConstant.PROPERTY_JSON_RPC_HTTP_PORT))
				.collect(Collectors.toMap(NodeCustomizedConfigPo::getNodeId, item -> Integer.parseInt(item.getPropertyValue())));
		
		List<CompletableFuture<NodeBo>> futureList = nodes.stream().map(node -> {
			String nodeIp = node.getNodeIp();
			Integer jsonRpcHttpPort = nodeIdRpcHttpPortMap.get(node.getId());
			return CompletableFuture.supplyAsync(() -> {
				boolean status = nodeHeartbeat(nodeIp, jsonRpcHttpPort);
				return NodeBo.builder().nodeId(node.getId()).nodeIp(nodeIp).nodePort(node.getNodePort())
						.jsonRpcHttpPort(jsonRpcHttpPort).status(status).build();
			}, BizThreadPools.HEARTBEAT).exceptionally(e -> {
				return NodeBo.builder().nodeId(node.getId()).nodeIp(nodeIp).nodePort(node.getNodePort())
						.jsonRpcHttpPort(jsonRpcHttpPort).status(false).build();
			});
		}).collect(Collectors.toList());
		List<NodeBo> nodeBoList = futureList.stream().map(CompletableFuture::join)
				.collect(Collectors.toList());
		updateNodeStatus(nodeBoList);
		
		return nodeBoList.stream().collect(Collectors.toMap(NodeBo::getNodeId, Function.identity()));
	}
	
	private boolean nodeHeartbeat(String nodeIp, Integer jsonRpcHttpPort) {
		boolean result = false;
		Request<Status, Status.Response> statusRequest = null;
		Status statusResponse = null;
		String uri = ConfluxUtils.toJsonRpcUri(nodeIp, jsonRpcHttpPort);
		
		try {
			Cfx cfx = Cfx.create(uri);
			statusRequest = cfx.getStatus();
			statusResponse = statusRequest.sendAndGet();
			result = statusResponse != null;
		} catch (Exception e) {
			log.error("nodeHeartbeat uri--->{}, request--->{}, response--->{}, error!", uri,
					JSON.toJSONString(statusRequest), JSON.toJSONString(statusResponse), e);
			return false;
		}
		log.info("nodeHeartbeat uri--->{}, request--->{}, response--->{}", uri, JSON.toJSONString(statusRequest),
				JSON.toJSONString(statusResponse));
		
		return result;
	}
	
	private void updateNodeStatus(List<NodeBo> nodeBoList) {
		List<String> upList = Lists.newArrayList();
		List<String> downList = Lists.newArrayList();
		try {
			nodeBoList.stream().forEach(item -> {
				if(item.isStatus()) {
					upList.add(item.getNodeId());
				} else {
					downList.add(item.getNodeId());
				}
			});
			if(CollectionUtils.isNotEmpty(upList)) {
				nodeDas.modifyNodesStatus(upList, NodeEnum.Status.RUNING);
			}
			if(CollectionUtils.isNotEmpty(downList)) {
				nodeDas.modifyNodesStatus(downList, NodeEnum.Status.STOP);
			}
		} catch(Exception e) {
			log.error("updateNodeStatus nodeBoList--->{}, error!", JSON.toJSONString(nodeBoList), e);
		}
	}
}
