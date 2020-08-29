package org.treegraph.baas.core.heartbeat;

import java.util.Map;

import org.treegraph.baas.core.heartbeat.bo.Heartbeat;

public interface HeartbeatHandler {
    void sendHeartBeat();
    void sendHeartBeat(Map<String, Heartbeat> nodes);
}