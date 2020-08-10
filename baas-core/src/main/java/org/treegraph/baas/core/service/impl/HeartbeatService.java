package org.treegraph.baas.core.service.impl;

import java.io.IOException;
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

//	private final static String MEDIA_TYPE_JSON = "application/json; charset=UTF-8";
	
//	private RestTemplate restTemplate;
	
	private final static ExecutorService executor = Executors.newFixedThreadPool(5);
	
	public Map<String, Boolean> batchNodeHeartbeat(List<NodePo> nodes) {
		if(CollectionUtils.isEmpty(nodes)) {
			return Maps.newHashMap();
		}
		
		List<CompletableFuture<NodeBo>> futureList = nodes.stream().map(node -> {
			String ip = node.getNodeIp();
			int port = node.getNodePort();
			return CompletableFuture.supplyAsync(() -> {
				boolean status = nodeHeartbeat(ip, port);
				return NodeBo.builder().nodeIp(ip).nodePort(port).status(status).build();
			}, executor).exceptionally(e -> {
				log.error("节点心跳检测异常[" + node.getNodeIp() + ":" + node.getNodePort() + "]", e);
				return NodeBo.builder().nodeIp(ip).nodePort(port).status(false).build();
			});
		}).collect(Collectors.toList());
		List<NodeBo> nodeBoList = futureList.stream().map(CompletableFuture::join)
				.collect(Collectors.toList());
		
		Map<String, Boolean> heartbeatMap = Maps.newHashMap();
		nodeBoList.stream().forEach(item -> heartbeatMap.put(item.getNodeIp() + item.getNodePort(), item.isStatus()));
		return heartbeatMap;
	}

//	public boolean nodeHeartbeat(String nodeIp, int nodePort) {
//		HttpHeaders requestHeader = buildHttpHeader(MEDIA_TYPE_JSON);
//		HttpEntity<String> request = new HttpEntity<String>(null, requestHeader);
//		
//		HttpMethod defaultMethod = HttpMethod.GET;
//		String uri = MessageFormat.format(HeartbeatConstant.URI_HEARTBEAT_NODE,
//				new Object[] { nodeIp, Integer.toString(nodePort) });
//		ResponseEntity<String> response = null;
//		if (defaultMethod == HttpMethod.POST) {
//			response = restTemplate.postForEntity(uri, request, String.class);
//		} else if (defaultMethod == HttpMethod.GET) {
//			response = restTemplate.exchange(uri, HttpMethod.GET, request, String.class);
//		} else {
//			Assert.isTrue(defaultMethod == HttpMethod.POST || defaultMethod == HttpMethod.GET, "不支持的http method");
//		}
//		
//		return response != null && response.getStatusCode() == HttpStatus.OK;
//		return false;
//	}

//	private HttpHeaders buildHttpHeader(String mediaType) {
//		HttpHeaders headers = new HttpHeaders();
//		MediaType type = MediaType.parseMediaType(mediaType);
//		headers.setContentType(type);
//		return headers;
//	}
	
	public boolean nodeHeartbeat(String nodeIp, int nodePort) {
//		boolean result = false;
//		try {
//			Cfx cfx = Cfx.create("http://"+nodeIp+":"+nodePort);
//			Request<Status, Status.Response> request = cfx.getCustomizedRequest(Status.Response.class,
//					ConfluxConstant.INTERFACE_BEATHEART, new Object[0]);
//			Status.Response response = request.send();
//			result = response != null && response.getResult() != null && response.getResult().getBlockNumber() != null;
//		} catch (IOException e) {
//		}
		
		return false;
	}
	
	@SuppressWarnings("unused")
	public static void main(String[] args) throws IOException {
		CompletableFuture<String> thenApplyAsync = CompletableFuture.supplyAsync(() -> {
			return "Hello ";
		}).thenApplyAsync(v -> v + "world");
		String result = CompletableFuture.supplyAsync(() -> {
			return "Hello ";
		}).thenApplyAsync(v -> v + "world").join();
		System.out.println(result);
	}
}
