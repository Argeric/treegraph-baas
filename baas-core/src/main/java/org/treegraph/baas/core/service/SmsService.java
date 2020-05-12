package org.treegraph.baas.core.service;

public interface SmsService {
	
	public boolean sendSingleSms(String phone,String content, String templateId);
	
	public boolean sendSingleSms1(String phone,String content, String templateId);
}
