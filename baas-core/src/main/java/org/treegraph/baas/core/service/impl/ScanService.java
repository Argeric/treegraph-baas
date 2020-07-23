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
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.treegraph.baas.core.enums.ErrorCodeEnum;
import org.treegraph.baas.core.exception.BusinessException;

import com.google.common.collect.Maps;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ScanService {

	private RestTemplate restTemplate;

	public Object invoke(String url, Map<String, ?> uriVariables) {
		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.parseMediaType("application/json; charset=UTF-8"));
		HttpEntity<Object> requestEntity = new HttpEntity<Object>(null, header);
		
		if (uriVariables != null) {
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
			uriVariables.entrySet().stream().forEach(o -> builder.queryParam(o.getKey(), o.getValue()));
			url = builder.build().encode().toString();
		}
		
		ResponseEntity<Object> resp = restTemplate.exchange(url, HttpMethod.GET, requestEntity, Object.class);
		if (resp.getStatusCode() != HttpStatus.OK) {
			throw new BusinessException(ErrorCodeEnum.SCAN_EXCEPTION);
		} else {
			return resp.getBody();
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
	
	public static void main(String[] args) {
	    String url = "http://www.baidu.com";
	    UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
//	    String url2 = builder.queryParam("name", "zgd").build().encode().toString();
//	    System.out.println("url2 = " + url2);//http://www.baidu.com?name=zgd

	    Map<String,String> param = Maps.newHashMap();
	    param.put("name","zzz");
	    param.put("age","123");
	    param.entrySet().stream().forEach(o -> builder.queryParam(o.getKey(),o.getValue()));
	    String url3 = builder.build().encode().toString(); //http://www.baidu.com?name=zgd&name=zzz&age=123
	    System.out.println("url3 = " + url3);

		//    ResponseEntity<String> rsp = restTemplate.getForEntity(url3, String.class);
	  }
}
