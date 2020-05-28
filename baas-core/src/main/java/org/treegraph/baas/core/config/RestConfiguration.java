package org.treegraph.baas.core.config;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;


@Configuration
public class RestConfiguration {
	
	@Autowired
	RestTemplateBuilder restTemplateBuilder;
	
	@Bean
	public RestTemplate restTemplate(){
		return restTemplateBuilder.setConnectTimeout(Duration.ofMillis(5000)).setReadTimeout(Duration.ofMillis(5000))
				.build();
	}
}
