package org.treegraph.baas.core.service.impl;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.treegraph.baas.core.constant.ConfluxConstant;
import org.treegraph.baas.core.domain.NodePo;
import org.treegraph.baas.core.service.bo.NodeBo;
import org.treegraph.baas.core.util.ConfluxUtils;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;

import conflux.web3j.Cfx;
import conflux.web3j.Request;
import conflux.web3j.response.Status;
import conflux.web3j.response.Status.Response;
import conflux.web3j.response.StringResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class HeartbeatService {

	private final static ExecutorService executor = Executors.newFixedThreadPool(5);
	
	public Map<String, Boolean> batchNodeHeartbeat(List<NodePo> nodes, Map<String, Integer> nodeIdRpcPortMap) {
		if(CollectionUtils.isEmpty(nodes)) {
			return Maps.newHashMap();
		}
		
		List<CompletableFuture<NodeBo>> futureList = nodes.stream().map(node -> {
			String nodeIp = node.getNodeIp();
			Integer jsonRpcHttpPort = nodeIdRpcPortMap.get(node.getId());
			return CompletableFuture.supplyAsync(() -> {
				boolean status = nodeHeartbeat(nodeIp, jsonRpcHttpPort);
				return NodeBo.builder().nodeIp(nodeIp).jsonRpcHttpPort(jsonRpcHttpPort).status(status).build();
			}, executor).exceptionally(e -> {
				log.error("节点心跳检测异常[" + ConfluxUtils.toIpAtPort(nodeIp, jsonRpcHttpPort) + "]", e);
				return NodeBo.builder().nodeIp(nodeIp).jsonRpcHttpPort(jsonRpcHttpPort).status(false).build();
			});
		}).collect(Collectors.toList());
		List<NodeBo> nodeBoList = futureList.stream().map(CompletableFuture::join)
				.collect(Collectors.toList());
		
		return nodeBoList.stream().collect(Collectors
				.toMap(item -> ConfluxUtils.toIpAtPort(item.getNodeIp(), item.getJsonRpcHttpPort()), NodeBo::isStatus));
	}
	
	public static boolean nodeHeartbeat(String nodeIp, Integer jsonRpcHttpPort) {
		boolean result = false;
		try {
			String uri = ConfluxUtils.toJsonRpcUri(nodeIp, jsonRpcHttpPort);
			Cfx cfx = Cfx.create(uri);
			//Request<Status, Response> statusRequest = cfx.getStatus();
			Request<String, StringResponse> statusRequest = cfx.getCustomizedRequest(StringResponse.class,
					ConfluxConstant.RPC_BEATHEART, new Object[0]);
			log.info("HeartbeatService uri--->{}, txRequest--->{}", uri, JSON.toJSONString(statusRequest));
			String statusResponse = statusRequest.sendAndGet();
			log.info("HeartbeatService uri--->{}, txRequest--->{}, txResponse--->{}", uri, JSON.toJSONString(statusRequest), JSON.toJSONString(statusResponse));
			result = statusResponse != null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return result;
	}
	
	public static void main(String[] args) {
		nodeHeartbeat("101.200.183.108", 12537);
	}
}
