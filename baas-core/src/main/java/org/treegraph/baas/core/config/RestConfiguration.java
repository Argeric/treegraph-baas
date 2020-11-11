package org.treegraph.baas.core.config;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestTemplate;
import org.treegraph.baas.core.util.IdWorker;

import lombok.extern.slf4j.Slf4j;


@Configuration
@Slf4j
public class RestConfiguration {
	
	@Autowired
	RestTemplateBuilder restTemplateBuilder;
	
	@Bean
	public RestTemplate restTemplate(){
		final RestTemplate restTemplate = restTemplateBuilder.setConnectTimeout(Duration.ofMillis(5000))
				.setReadTimeout(Duration.ofMillis(5000)).build();
		
		 // 添加拦截器
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        TraceLogInterceptor traceLogInterceptor = new TraceLogInterceptor();
        interceptors.add(traceLogInterceptor);
        restTemplate.setInterceptors(interceptors);
        
        return restTemplate;
	}
	
	final class TraceLogInterceptor implements ClientHttpRequestInterceptor {

		@Override
		public ClientHttpResponse intercept(@NotNull HttpRequest request, @NotNull byte[] bytes, ClientHttpRequestExecution execution)
				throws IOException {
			Long seq = IdWorker.generateId();
			traceRequest(seq, request, bytes);
			ClientHttpResponse response = execution.execute(request, bytes);
			ClientHttpResponse responseCopy = new BufferingClientHttpResponseWrapper(response);
			traceResponse(seq, responseCopy);

			return responseCopy;
		}

		private void traceRequest(Long seq, HttpRequest request, byte[] body) {
			log.debug("rest[request][seq:{}] uri:{},header:{},requestBody:{},method:{}", seq, request.getURI(),
					request.getHeaders().toString(), new String(body, StandardCharsets.UTF_8),
					request.getMethodValue());
		}

		private void traceResponse(Long seq, ClientHttpResponse response) throws IOException {
			StringBuilder inputStringBuilder = new StringBuilder();
			try (BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(response.getBody(), StandardCharsets.UTF_8))) {
				String line = bufferedReader.readLine();
				while (line != null) {
					inputStringBuilder.append(line);
					line = bufferedReader.readLine();
				}
			}
			log.debug("rest[response][seq:{}] statusCode:{},statusText:{},headers:{},responseBody:{}", seq,
					response.getStatusCode(), response.getStatusText(), response.getHeaders().toString(),
					inputStringBuilder.toString());
		}
	}

	static final class BufferingClientHttpResponseWrapper implements ClientHttpResponse {

		private final ClientHttpResponse response;
		private byte[] body;

		BufferingClientHttpResponseWrapper(ClientHttpResponse response) {
			this.response = response;
		}

		@Override
		public HttpStatus getStatusCode() throws IOException {
			return this.response.getStatusCode();
		}

		@Override
		public int getRawStatusCode() throws IOException {
			return this.response.getRawStatusCode();
		}

		@Override
		public String getStatusText() throws IOException {
			return this.response.getStatusText();
		}

		@Override
		public HttpHeaders getHeaders() {
			return this.response.getHeaders();
		}

		@Override
		public InputStream getBody() throws IOException {
			if (this.body == null) {
				this.body = StreamUtils.copyToByteArray(this.response.getBody());
			}
			return new ByteArrayInputStream(this.body);
		}

		@Override
		public void close() {
			this.response.close();
		}
	}
}
