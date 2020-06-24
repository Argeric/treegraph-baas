package org.treegraph.baas.core.service.impl;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;
import org.treegraph.baas.core.constant.HeartbeatConstant;
import org.treegraph.baas.core.domain.NodePo;
import org.treegraph.baas.core.entity.bo.NodeBo;
import org.treegraph.baas.core.util.CollectionUtils;

import com.google.common.collect.Maps;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class HeartbeatService {

	public final static String MEDIA_TYPE_FORM = "application/x-www-form-urlencoded; charset=UTF-8";

	public final static String MEDIA_TYPE_JSON = "application/json; charset=UTF-8";

	private RestTemplate restTemplate;
	
	public Map<String, Boolean> batchNodeHeartbeat(List<NodePo> nodes) {
		if(CollectionUtils.isEmpty(nodes)) {
			return Maps.newHashMap();
		}
		
		List<CompletableFuture<NodeBo>> completableFutureList = nodes.stream()
				.map(node -> CompletableFuture.supplyAsync(() -> {
					String ip = node.getNodeIp();
					int port = node.getNodePort();
					boolean status = nodeHeartbeat(ip, port);
					return NodeBo.builder().nodeIp(ip).nodePort(port).runningStatus(status).build();
				})).collect(Collectors.toList());
		List<NodeBo> nodeBoList = completableFutureList.stream().map(CompletableFuture::join)
				.collect(Collectors.toList());
		
		return nodeBoList.stream().collect(
				Collectors.toMap(item -> item.getNodeIp() + item.getNodePort(), item -> item.isRunningStatus()));
	}

	public boolean nodeHeartbeat(String nodeIp, int nodePort) {
		HttpHeaders requestHeader = buildHttpHeader(MEDIA_TYPE_JSON);
		HttpEntity<String> request = new HttpEntity<String>(null, requestHeader);
		
		ResponseEntity<String> response = null;
		HttpMethod defaultMethod = HttpMethod.GET;
		try {
			String uri = MessageFormat.format(HeartbeatConstant.URI_HEARTBEAT_NODE,
					new Object[] { nodeIp, Integer.toString(nodePort) });
			if (defaultMethod == HttpMethod.POST) {
				response = restTemplate.postForEntity(uri, request, String.class);
			} else if (defaultMethod == HttpMethod.GET) {
				response = restTemplate.exchange(uri, HttpMethod.GET, request, String.class);
			} else {
				Assert.isTrue(defaultMethod == HttpMethod.POST || defaultMethod == HttpMethod.GET, "不支持的http method");
			}
		} catch(Exception e) {
			log.error("节点心跳检测异常["+nodeIp+":"+nodePort+"]", e);
		}
		
		return response != null && response.getStatusCode() == HttpStatus.OK;
	}

	private HttpHeaders buildHttpHeader(String mediaType) {
		HttpHeaders headers = new HttpHeaders();
		MediaType type = MediaType.parseMediaType(mediaType);
		headers.setContentType(type);
		return headers;
	}
}
