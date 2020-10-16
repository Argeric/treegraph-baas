package org.treegraph.baas.core.constant;

public class MonitorConstant {
	
	// log monitor
	public static final String DEFAULT_HOST_LOG_PATH = "/opt/service/blockchain/tree_graph_node";
	public static final Integer DEFAULT_HOST_WEB_PORT = 38778;
	public static final String DEFAULT_CONTAINER_NETWORK = "178.18.0.0/16";
	public static final String PLACEHOLDER_CONTAINER_NETWORK = "178.{0}.0.0/16";
	public static final String LOG_MONITOR_ENV_FILE = 
			"COMPOSE_PROJECT_NAME={0}\n"
			+ "HOST_LOG_PATH={1}\n"
			+ "HOST_WEB_PORT={2}\n" 
			+ "CONTAINER_NETWORK={3}";
	
	
	// system monitor
	public static final String SYS_MONITOR_ENV_FILE = 
			"COMPOSE_PROJECT_NAME={0}\n"
			+ "HOST_MONITOR_PORT={1}\n"
			+ "CONTAINER_NETWORK={2}";
	public static final String SYS_MONITOR_PROMETHEUS_CONFIG_FILE =
			"global:\n"
			+ "  scrape_interval: 60s\n"
			+ "  evaluation_interval: 60s\n"
			+ "\n"
			+ "scrape_configs:\n"
			+ "  - job_name: linux\n"
			+ "    static_configs:\n"
			+ "      - targets: [\"baas_monitor_exporter{0}:9100\"]\n"
			+ "        labels:\n" 
			+ "          instance: localhost";		
	public static final String SYS_MONITOR_GRAFANA_DATASOURCE_FILE = 
			"apiVersion: 1\n"
			+ "\n"
			+ "datasources:\n"
			+ "  - name: 'Prometheus'\n"
			+ "    type: 'prometheus'\n"
			+ "    access: 'proxy'\n"
			+ "    orgId: 1\n"
			+ "    uid: 'my_unique_uid'\n"
			+ "    url: \"baas_monitor_prometheus{0}:9090\"\n"
			+ "    password:\n"
			+ "    user:\n"
			+ "    database:\n"
			+ "    basicAuth:\n"
			+ "    basicAuthUser:\n"
			+ "    basicAuthPassword:\n"
			+ "    withCredentials:\n"
			+ "    isDefault:\n"
			+ "    jsonData:\n"
			+ "     tlsAuth: false\n"
			+ "     tlsAuthWithCACert: false\n"
			+ "     timeInterval: 10\n"
			+ "     httpMode: 'GET'\n"
			+ "    version: 1\n"
			+ "    editable: false";	

			
}