package org.treegraph.baas.core.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("topic.kafka")
public class TopicConfiguration {
	
	private String lyqcFinAccountNotify;
	
	private String lyqcFinAccountCallBack;

	public String getLyqcFinAccountNotify() {
		return lyqcFinAccountNotify;
	}

	public void setLyqcFinAccountNotify(String lyqcFinAccountNotify) {
		this.lyqcFinAccountNotify = lyqcFinAccountNotify;
	}

	public String getLyqcFinAccountCallBack() {
		return lyqcFinAccountCallBack;
	}

	public void setLyqcFinAccountCallBack(String lyqcFinAccountCallBack) {
		this.lyqcFinAccountCallBack = lyqcFinAccountCallBack;
	}
}
