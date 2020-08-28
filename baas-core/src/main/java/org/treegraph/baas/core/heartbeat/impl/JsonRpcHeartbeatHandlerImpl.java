package org.treegraph.baas.core.heartbeat.impl;

import java.util.concurrent.TimeUnit;

import org.treegraph.baas.common.util.MD5;
import org.treegraph.baas.core.constant.ConfluxConstant;
import org.treegraph.baas.core.heartbeat.HeartbeatHandler;
import org.treegraph.baas.core.heartbeat.bo.Heartbeat;
import org.treegraph.baas.core.util.ConfluxUtils;

import com.alibaba.fastjson.JSON;

import conflux.web3j.Cfx;
import conflux.web3j.Request;
import conflux.web3j.response.Status;

public class JsonRpcHeartbeatHandlerImpl implements HeartbeatHandler{
	
	public Heartbeat send(Heartbeat heartbeat) {
		long pingTime = System.currentTimeMillis();
		Request<Status, Status.Response> statusRequest = null;
		Status statusResponse = null;
		String error = null;
		
		try {
			String uri = ConfluxUtils.toJsonRpcUri(heartbeat.getIp(), heartbeat.getPort());
			Cfx cfx = Cfx.create(uri, ConfluxConstant.TIMEOUT_HEARTBEAT_CONNECT, ConfluxConstant.TIMEOUT_HEARTBEAT_READ, TimeUnit.SECONDS);
			statusRequest = cfx.getStatus();
			statusResponse = statusRequest.sendAndGet();
		} catch (Exception e) {
			error = e.getMessage();
		}
		
		String pong = JSON.toJSONString(statusResponse);
		return Heartbeat.builder().nodeId(heartbeat.getNodeId())
				.ip(heartbeat.getIp())
				.port(heartbeat.getPort())
				.lastPong(heartbeat.getPong())
				.lastPongMd5(heartbeat.getLastPongMd5())
				
				.ping(JSON.toJSONString(statusRequest))
				.pong(pong)
				.pingTime(pingTime)
				.pongTime(System.currentTimeMillis())
				.pongMd5(pong != null ? MD5.encode(pong) : null)
				.error(error).build();
	}
}
