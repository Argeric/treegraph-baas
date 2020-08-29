package org.treegraph.baas.core.heartbeat.impl;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.treegraph.baas.core.component.BizThreadPools;
import org.treegraph.baas.core.heartbeat.HeartbeatClient;
import org.treegraph.baas.core.heartbeat.HeartbeatHandler;
import org.treegraph.baas.core.heartbeat.bo.Heartbeat;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class HeartbeatHandlerImpl implements HeartbeatHandler {
	
	private HeartbeatClient heartbeatClient;
	
	@Scheduled(cron="0/30 * * * * ?")
    public void sendHeartBeat() {
        HeartbeatLinstener linstener = HeartbeatLinstener.getInstance();
        Map<String, Map<String, Heartbeat>> consortiums = linstener.getNodes();
        Iterator<Entry<String, Map<String, Heartbeat>>> consortiumIter = consortiums.entrySet().iterator();
        
        while(consortiumIter.hasNext()){
        	Entry<String, Map<String, Heartbeat>> consortium = consortiumIter.next();
        	Map<String, Heartbeat> nodes = consortium.getValue();
        	this.sendHeartBeat(nodes);
        }
    }

	public synchronized void sendHeartBeat(Map<String, Heartbeat> nodes) {
		Collection<Heartbeat> lastHeartbeats = nodes.values();
		List<CompletableFuture<Heartbeat>> futureList = lastHeartbeats.stream().map(item -> CompletableFuture
				.supplyAsync(() -> heartbeatClient.send(item), BizThreadPools.HEARTBEAT)
				.exceptionally(e -> null)
		).collect(Collectors.toList());
		List<Heartbeat> heartbeats = futureList.stream().map(CompletableFuture::join).collect(Collectors.toList());
		heartbeats.stream().forEach(item -> nodes.put(item.getId(), item));
	}
}
