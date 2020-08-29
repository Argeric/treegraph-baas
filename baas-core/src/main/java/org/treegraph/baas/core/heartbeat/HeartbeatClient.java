package org.treegraph.baas.core.heartbeat;

import org.treegraph.baas.core.heartbeat.bo.Heartbeat;

public interface HeartbeatClient {
	Heartbeat send(Heartbeat heartbeat);
}
