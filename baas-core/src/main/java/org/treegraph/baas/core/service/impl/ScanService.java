package org.treegraph.baas.core.service.impl;

import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.treegraph.baas.core.enums.ErrorCodeEnum;
import org.treegraph.baas.core.exception.BusinessException;

import com.alibaba.fastjson.JSON;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class ScanService {

	private RestTemplate restTemplate;

	public Object invoke(String url, Map<String, String> uriVariables) {
		log.info("uriVariables--->" + JSON.toJSONString(uriVariables));
		HttpHeaders header = new HttpHeaders();
		
		ResponseEntity<Object> response = null;
		if(url.contains("create") || url.contains("update") || url.contains("delete")) {
			header.setContentType(MediaType.parseMediaType("application/x-www-form-urlencoded; charset=UTF-8"));
			MultiValueMap<String, String> request = new LinkedMultiValueMap<>();
			if (uriVariables != null) {
				uriVariables.entrySet().stream().forEach(o -> request.add(o.getKey(), o.getValue()));
			}
			HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(request, header);
			log.info("request--->" + JSON.toJSONString(requestEntity));
			response = restTemplate.postForEntity(url, requestEntity, Object.class);
			log.info("response--->" + JSON.toJSONString(response));
		} else {
			header.setContentType(MediaType.parseMediaType("application/json; charset=UTF-8"));
			if (uriVariables != null) {
				UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
				uriVariables.entrySet().stream().forEach(o -> builder.queryParam(o.getKey(), o.getValue()));
				url = builder.build().encode().toString();
			}
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(null, header);
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, Object.class);
		}
		
		if (response.getStatusCode() != HttpStatus.OK) {
			throw new BusinessException(ErrorCodeEnum.SCAN_EXCEPTION);
		} else {
			return response.getBody();
		}
	}
	
	public <T> T invoke(String url, Map<String, ?> uriVariables, ParameterizedTypeReference<T> responseType) {
		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.parseMediaType("application/json; charset=UTF-8"));
		HttpEntity<String> requestEntity = new HttpEntity<String>(null, header);
		ResponseEntity<T> resp = restTemplate.exchange(url, HttpMethod.GET, requestEntity, responseType, uriVariables);
		if (resp.getStatusCode() != HttpStatus.OK) {
			throw new BusinessException(ErrorCodeEnum.SCAN_EXCEPTION);
		} else {
			return resp.getBody();
		}
	}
}
