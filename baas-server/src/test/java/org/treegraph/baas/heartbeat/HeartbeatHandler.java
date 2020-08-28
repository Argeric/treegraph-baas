package org.treegraph.baas.heartbeat;
public interface HeartbeatHandler {
    public Cmder sendHeartBeat(HeartbeatEntity info);
}