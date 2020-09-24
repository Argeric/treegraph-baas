package org.treegraph.baas.core.heartbeat.impl;

import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;
import org.treegraph.baas.common.util.MD5;
import org.treegraph.baas.core.constant.ConfluxConstant;
import org.treegraph.baas.core.enums.HeartbeatEnum;
import org.treegraph.baas.core.heartbeat.HeartbeatClient;
import org.treegraph.baas.core.heartbeat.bo.Heartbeat;
import org.treegraph.baas.core.util.ConfluxUtils;

import com.alibaba.fastjson.JSON;

import conflux.web3j.Cfx;
import conflux.web3j.Request;
import conflux.web3j.response.Status;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class JsonRpcHeartbeatClientImpl implements HeartbeatClient{
	
	public Heartbeat send(Heartbeat heartbeat) {
		long pingTime = System.currentTimeMillis();
		String uri = null;
		Request<Status, Status.Response> statusRequest = null;
		Status statusResponse = null;
		String error = null;
		
		try {
			uri = ConfluxUtils.toUri(heartbeat.getIp(), heartbeat.getPort());
			Cfx cfx = Cfx.create(uri, ConfluxConstant.TIMEOUT_CONNECT, ConfluxConstant.TIMEOUT_READ, TimeUnit.SECONDS);
			statusRequest = cfx.getStatus();
			statusResponse = statusRequest.sendAndGet();
		} catch (Exception e) {
			error = e.getMessage();
		}
		
		String lastPongMd5 = heartbeat.getPong() != null ? MD5.encode(heartbeat.getPong()) : null;
		String pong = JSON.toJSONString(statusResponse);
		String pongMd5 = pong != null ? MD5.encode(pong) : null;
		HeartbeatEnum.Status status = null;
		if(error != null){
			status = HeartbeatEnum.Status.RED;
		} else{
			if(pongMd5 != null && lastPongMd5 != null && !pongMd5.equalsIgnoreCase(lastPongMd5)){
				status = HeartbeatEnum.Status.GREEN;
			} else{
				status = HeartbeatEnum.Status.YELLOW;
			}
		}
		//log.info("\n\theartbeat--->{}\n\tping--->{}\n\tpong--->{}\\n\\terror--->{}", uri,
		//		JSON.toJSONString(statusRequest), JSON.toJSONString(statusResponse), error);
		log.info("{} heartbeat:{}", ConfluxUtils.toIpAtPortForLog(heartbeat.getIp(), heartbeat.getPort()), status);
		
		return Heartbeat.builder().id(heartbeat.getId())
				.ip(heartbeat.getIp())
				.port(heartbeat.getPort())
				.lastPong(heartbeat.getPong())
				.lastPongMd5(lastPongMd5)
				
				.ping(JSON.toJSONString(statusRequest))
				.pong(pong)
				.pingTime(pingTime)
				.pongTime(System.currentTimeMillis())
				.pongMd5(pongMd5)
				.error(error)
				
				.status(status)
				.build();
	}
}
