package org.treegraph.baas.core.heartbeat.impl;

import com.google.common.collect.Maps;
import org.treegraph.baas.core.enums.HeartbeatEnum;
import org.treegraph.baas.core.enums.NodeEnum;
import org.treegraph.baas.core.enums.NodeEnum.Status;
import org.treegraph.baas.core.heartbeat.bo.Heartbeat;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HeartbeatListener {
    private static final ConcurrentHashMap<String, Map<String, Heartbeat>> consortiumNodes = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, Map<String, Long>> consortiumNodeStatus = new ConcurrentHashMap<>();
    private static final long timeout = 10 * 1000;

    private static class SingleHolder {
        private static final HeartbeatListener INSTANCE = new HeartbeatListener();
    }
 
    private HeartbeatListener() {
    }
 
    public static HeartbeatListener getInstance() {
        return SingleHolder.INSTANCE;
    }
    
    /**
     * check node valid from avoiding to register twice
     */
    public boolean isNodeValid(String consortiumId, String nodeId) {
        if (!consortiumNodes.contains(consortiumId)
                || !consortiumNodes.get(consortiumId).containsKey(nodeId))
            return true;
        return !consortiumNodeStatus.contains(consortiumId)
                || !consortiumNodeStatus.get(consortiumId).containsKey(nodeId)
                || (System.currentTimeMillis() - consortiumNodeStatus.get(consortiumId).get(nodeId)) > timeout;
    }
 
    /**
     * register a node for heartbeat
     */
    public synchronized void registerNode(String consortiumId, String nodeId, Heartbeat nodeInfo) {
        Map<String, Heartbeat> nodeMap = consortiumNodes.computeIfAbsent(consortiumId, k -> Maps.newConcurrentMap());
        nodeMap.put(nodeId, nodeInfo);
        Map<String, Long> nodeStatusMap = consortiumNodeStatus.computeIfAbsent(consortiumId, k -> Maps.newConcurrentMap());
        nodeStatusMap.put(nodeId, System.currentTimeMillis());
    }
    
    /**
     * 1. get node heartbeat(for all)
     */
    public Map<String, Map<String, Heartbeat>> getNodes() {
    	return consortiumNodes;
    }
    
    /**
     * 2. get node heartbeat(for a consortium)
     */
    public Map<String, Heartbeat> getNodes(String consortiumId) {
    	Map<String, Heartbeat> nodeMap = consortiumNodes.get(consortiumId);
    	return nodeMap != null ? nodeMap : Maps.newHashMap();
    }
    
    /**
     * 3. get node heartbeat(for a node)
     */
    public Heartbeat getNode(String consortiumId, String nodeId) {
        return this.getNodes(consortiumId).get(nodeId);
    }
    
//    /**
//     * 4. get node heartbeat(for any an alive node)
//     */
//    public Heartbeat getAliveNodeRandom(String consortiumId) {
//    	Map<String, Heartbeat> nodes = this.getNodes(consortiumId);
//		return nodes.values().stream().filter(item -> item.getStatus() == HeartbeatEnum.Status.GREEN).findFirst()
//				.orElseThrow(() -> new AlertException("alive.node.not.found"));
//    }
    
//    /**
//     * 1. get node status(for all)
//     */
//    public Map<String, Map<String, Long>> getNodeStatus() {
//    	return consortiumNodeStatus;
//    }
    
//    /**
//     * 2. get node status(for a consortium)
//     */
//    public Map<String, Long> getNodeStatus(String consortiumId) {
//    	Map<String, Long> nodeStatusMap = consortiumNodeStatus.get(consortiumId);
//    	return nodeStatusMap != null ? nodeStatusMap : Maps.newHashMap();
//    }
    
    /**
     * 3. get node status(for a node)
     */
    public NodeEnum.Status getNodeStatus(String consortiumId, String nodeId){
    	Heartbeat heartbeat = this.getNodes(consortiumId).get(nodeId);
		
    	Status nodeStatus;
		if(heartbeat.getStatus() == HeartbeatEnum.Status.GREEN){
			nodeStatus = Status.RUNNING;
		} else if(heartbeat.getStatus() == HeartbeatEnum.Status.YELLOW){
			nodeStatus = Status.EXCEPTION;
		} else{
			nodeStatus = Status.TO_LAUNCH;
		}
		
		return nodeStatus;
    }
    
//    /**
//     * 1. remove node heartbeat(for all invalid node)
//     */
//    public synchronized void removeInvalidNode() {
//    	Iterator<Entry<String, Map<String, Long>>> consortiumIter = consortiumNodeStatus.entrySet().iterator();
//    	while(consortiumIter.hasNext()){
//    		Entry<String, Map<String, Long>> consortium = consortiumIter.next();
//
//    		Iterator<Entry<String, Long>> nodeIter = consortium.getValue().entrySet().iterator();
//    		while(nodeIter.hasNext()){
//    			Map.Entry<String, Long> node = nodeIter.next();
//    			if((System.currentTimeMillis() - node.getValue()) > timeout){
//    				consortiumNodes.remove(node.getKey());
//    			}
//    		}
//    	}
//    }
 
    /**
     * 2. remove node heartbeat(for a node)
     */
    public synchronized void removeNode(String consortiumId, String nodeId) {
        Map<String, Heartbeat> nodeMap = consortiumNodes.get(consortiumId);
        if(nodeMap != null){
        	nodeMap.remove(nodeId);
        }
    }
}
