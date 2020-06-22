package org.treegraph.baas.core.service.impl;

import java.text.MessageFormat;

import org.springframework.beans.factory.annotation.Autowired;
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

@Service
public class HeartbeatService {

	public final static String MEDIA_TYPE_FORM = "application/x-www-form-urlencoded; charset=UTF-8";

	public final static String MEDIA_TYPE_JSON = "application/json; charset=UTF-8";

	@Autowired
	private RestTemplate restTemplate;

	public boolean sendNodeHeartbeat(String nodeIp, int nodePort, HttpMethod method) {
		HttpHeaders reqHeader = buildHttpHeader(MEDIA_TYPE_JSON);
		HttpEntity<String> request = new HttpEntity<String>(null, reqHeader);

		ResponseEntity<String> responseEntity = null;
		String uri = MessageFormat.format(HeartbeatConstant.URI_HEARTBEAT_NODE,
				new Object[] { nodeIp, Integer.toString(nodePort) });
		if (method == HttpMethod.POST) {
			responseEntity = restTemplate.postForEntity(uri, request, String.class);
		} else if (method == HttpMethod.GET) {
			responseEntity = restTemplate.exchange(uri, HttpMethod.GET, request, String.class);
		} else {
			Assert.isTrue(method == HttpMethod.POST || method == HttpMethod.GET, "不支持的http method");
		}

		return responseEntity.getStatusCode() == HttpStatus.OK;
	}

	public HttpHeaders buildHttpHeader(String mediaType) {
		HttpHeaders headers = new HttpHeaders();
		MediaType type = MediaType.parseMediaType(mediaType);
		headers.setContentType(type);
		return headers;
	}
}
