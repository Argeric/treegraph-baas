package org.treegraph.baas.core.heartbeat;

import org.treegraph.baas.core.heartbeat.bo.Heartbeat;

public interface HeartbeatHandler {

	Heartbeat send(Heartbeat heartbeat);
}
