package org.treegraph.baas.core.heartbeat.impl;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.treegraph.baas.core.enums.HeartbeatEnum;
import org.treegraph.baas.core.enums.NodeEnum;
import org.treegraph.baas.core.enums.NodeEnum.Status;
import org.treegraph.baas.core.heartbeat.bo.Heartbeat;

import com.google.common.collect.Maps;

public class HeartbeatLinstener {
    private static final ConcurrentHashMap<String, Map<String, Heartbeat>> consortiumNodes = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String, Map<String, Long>> consortiumNodeStatus = new ConcurrentHashMap<>();
    private long timeout = 10 * 1000;
 
    private static class SingleHolder {
        private static final HeartbeatLinstener INSTANCE = new HeartbeatLinstener();
    }
 
    private HeartbeatLinstener() {
    }
 
    public static HeartbeatLinstener getInstance() {
        return SingleHolder.INSTANCE;
    }
    
    // check node valid from avoiding to register twice
    public boolean isNodeValid(String consortiumId, String nodeId) {
		if (!consortiumNodes.contains(consortiumId) 
				|| !consortiumNodes.get(consortiumId).containsKey(nodeId))
			return false;
		if (!consortiumNodeStatus.contains(consortiumId) 
				|| consortiumNodeStatus.get(consortiumId).containsKey(nodeId)
				|| (System.currentTimeMillis() - consortiumNodeStatus.get(consortiumId).get(nodeId)) > timeout)
			return false;
		return true;
    }
 
    public synchronized void registerNode(String consortiumId, String nodeId, Heartbeat nodeInfo) {
        Map<String, Heartbeat> nodeMap = consortiumNodes.get(consortiumId);
        if(nodeMap == null){
        	nodeMap = Maps.newConcurrentMap();
        	consortiumNodes.put(consortiumId, nodeMap);
        }
        nodeMap.put(nodeId, nodeInfo);
        Map<String, Long> nodeStatusMap = consortiumNodeStatus.get(consortiumId);
        if(nodeStatusMap == null){
        	nodeStatusMap = Maps.newConcurrentMap();
        	consortiumNodeStatus.put(consortiumId, nodeStatusMap);
        }
        nodeStatusMap.put(nodeId, System.currentTimeMillis());
    }
    
    public Map<String, Map<String, Heartbeat>> getNodes() {
    	return consortiumNodes;
    }
    
    public Map<String, Heartbeat> getNodes(String consortiumId) {
    	Map<String, Heartbeat> nodeMap = consortiumNodes.get(consortiumId);
    	return nodeMap != null ? nodeMap : Maps.newHashMap();
    }
    
    public Heartbeat getNode(String consortiumId, String nodeId) {
    	return this.getNodes(consortiumId).get(nodeId);
    }
    
    public Map<String, Map<String, Long>> getNodeStatus() {
    	return consortiumNodeStatus;
    }
    
    public Map<String, Long> getNodeStatus(String consortiumId) {
    	Map<String, Long> nodeStatusMap = consortiumNodeStatus.get(consortiumId);
    	return nodeStatusMap != null ? nodeStatusMap : Maps.newHashMap();
    }
    
    public NodeEnum.Status getNodeStatus(String consortiumId, String nodeId){
    	Heartbeat heartbeat = this.getNodes(consortiumId).get(nodeId);
		
    	Status nodeStatus = null;
		if(heartbeat.getStatus() == HeartbeatEnum.Status.GREEN){
			nodeStatus = Status.RUNING;
		} else if(heartbeat.getStatus() == HeartbeatEnum.Status.YELLOW){
			nodeStatus = Status.EXCEPTION;
		} else{
			nodeStatus = Status.TO_LAUNCH;
		}
		
		return nodeStatus;
    }
    
    public synchronized void removeInValidNode() {
    	Iterator<Entry<String, Map<String, Long>>> consortiumIter = consortiumNodeStatus.entrySet().iterator();
    	while(consortiumIter.hasNext()){
    		Entry<String, Map<String, Long>> consortium = consortiumIter.next();
    		
    		Iterator<Entry<String, Long>> nodeIter = consortium.getValue().entrySet().iterator();
    		while(nodeIter.hasNext()){
    			Map.Entry<String, Long> node = nodeIter.next();
    			if((System.currentTimeMillis() - node.getValue()) > timeout){
    				consortiumNodes.remove(node.getKey());
    			}
    		}
    	}
    }
 
    public synchronized void removeNode(String consortiumId, String nodeID) {
        Map<String, Heartbeat> nodeMap = consortiumNodes.get(consortiumId);
        if(nodeMap != null){
        	nodeMap.remove(nodeID);
        }
    }
}
