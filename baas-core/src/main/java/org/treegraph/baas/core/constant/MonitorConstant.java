package org.treegraph.baas.core.constant;

public class MonitorConstant {
	public static final String DEFAULT_HOST_LOG_PATH = "/opt/service/blockchain/tree_graph_node";
	public static final Integer DEFAULT_HOST_WEB_PORT = 38778;
	public static final String DEFAULT_CONTAINER_NETWORK = "178.18.0.0/16";
	public static final String PLACEHOLDER_CONTAINER_NETWORK = "178.{0}.0.0/16";
	public static final String LOG_MONITOR_ENV_FILE = 
			"HOST_LOG_PATH={0}\n"
			+ "HOST_WEB_PORT={1}\n" 
			+ "CONTAINER_NETWORK={2}";
}