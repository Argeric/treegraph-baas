package org.treegraph.baas.core.heartbeat.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.treegraph.baas.core.component.BizThreadPools;
import org.treegraph.baas.core.das.NodeDas;
import org.treegraph.baas.core.enums.HeartbeatEnum;
import org.treegraph.baas.core.enums.NodeEnum;
import org.treegraph.baas.core.heartbeat.HeartbeatClient;
import org.treegraph.baas.core.heartbeat.HeartbeatHandler;
import org.treegraph.baas.core.heartbeat.bo.Heartbeat;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class HeartbeatHandlerImpl implements HeartbeatHandler {
	
	private final HeartbeatClient heartbeatClient;
	
    private final NodeDas nodeDas;
	
	@Scheduled(cron="0 0/1 * * * ?")
    public void sendHeartBeat() {
        HeartbeatListener listener = HeartbeatListener.getInstance();
        Map<String, Map<String, Heartbeat>> consortiums = listener.getNodes();
		for (Entry<String, Map<String, Heartbeat>> consortium : consortiums.entrySet()) {
			Map<String, Heartbeat> nodes = consortium.getValue();
			this.sendHeartBeat(nodes);
		}
    }

	public synchronized void sendHeartBeat(Map<String, Heartbeat> nodes) {
		Collection<Heartbeat> lastHeartbeats = nodes.values();
		List<CompletableFuture<Heartbeat>> futureList = lastHeartbeats.stream().map(item -> CompletableFuture
				.supplyAsync(() -> heartbeatClient.send(item), BizThreadPools.HEARTBEAT)
				.exceptionally(e -> {
					log.error("heartbeat--->{}, error:", JSONObject.toJSONString(item, true), e);
					return item;})
		).collect(Collectors.toList());
		List<Heartbeat> heartbeats = futureList.stream().map(CompletableFuture::join).collect(Collectors.toList());
		heartbeats.forEach(item -> nodes.put(item.getId(), item));
		updateNodeStatus(heartbeats);
	}
	
	private void updateNodeStatus(List<Heartbeat> heartbeats) {
		List<String> greenList = Lists.newArrayList();
		List<String> yellowList = Lists.newArrayList();
		List<String> redList = Lists.newArrayList();
		try {
			heartbeats.forEach(item -> {
				if(item.getStatus() == HeartbeatEnum.Status.GREEN) {
					greenList.add(item.getId());
				} else if(item.getStatus() == HeartbeatEnum.Status.YELLOW) {
					yellowList.add(item.getId());
				} else{
					redList.add(item.getId());
				}
			});
			if(CollectionUtils.isNotEmpty(greenList)) {
				nodeDas.modifyNodesStatus(greenList, NodeEnum.Status.RUNNING);
			}
			if(CollectionUtils.isNotEmpty(yellowList)) {
				nodeDas.modifyNodesStatus(yellowList, NodeEnum.Status.EXCEPTION);
			}
			if(CollectionUtils.isNotEmpty(redList)) {
				nodeDas.modifyNodesStatus(redList, NodeEnum.Status.TO_LAUNCH);
			}
		} catch(Exception e) {
			log.error("updateNodeStatus heartbeats--->{}, error!", JSON.toJSONString(heartbeats), e);
		}
	}
}
