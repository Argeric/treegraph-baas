package org.treegraph.baas.core.constant;

import java.text.MessageFormat;

public class HeartbeatConstant {

	public static final String URI_HEARTBEAT_NODE = "http://{0}:{1}/heartbeat";
	
	public static void main(String[] args) {
		String nodeIp = "219.123.34.55";
		String nodePort = Integer.toString(9797);
		String uri = MessageFormat.format(HeartbeatConstant.URI_HEARTBEAT_NODE, new Object[] {nodeIp, nodePort});
		System.out.println(uri);
		System.out.println(new Integer(nodePort));
	}
}