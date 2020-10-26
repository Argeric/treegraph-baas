package org.treegraph.baas.core.constant;

public class ConfluxConstant {
	/**
	 * rpc 
	 */
    public static final String RPC_ADD_DELETE_NODE = "cfx_getMembershipChangeRawTransaction";
    
    public static final String RPC_ADD_DELETE_NODE_CONFIRM = "cfx_constructAndSendNewConsortiumMembershipTrans";
    
    public static final String RPC_GET_BFT_EPOCH_NUMBER = "cfx_getBFTEpochNumber";
    
    public static final String RPC_UPDATE_CERT = "cfx_updateConsortiumAndGrpcCredential";
    
    /**
     * port
     */
    public static final int DEFAULT_JSONRPC_PORT = 12537;
    
    public static final int DEFAULT_JSONRPC_LOCAL_PORT = 12539;
    
    public static final int DEFAULT_GRPC_PORT = 22537;
    
    /**
     * test
     */
    public static final boolean TEST_SWITCH = true;
    
    public static final int TEST_RPC_PORT_INTERVAL = 100;
    
    public static final int TEST_JSONRPC_PORT_STEP = 1;
    
    public static final int TEST_MONITOR_PORT_INTERVAL = 10000;
    
    public static final int TEST_LOG_MONITOR_WEB_PORT_STEP = 1;
    
    public static final int TEST_LOG_MONITOR_CONTAINER_NETWORK_STEP = 1;
    
    /**
     * customized property
     */
    public static final String PROPERTY_JSONRPC_PORT = "jsonrpc_http_port";
    
    public static final String PROPERTY_JSONRPC_LOC_PORT = "jsonrpc_local_http_port";
    
    public static final String PROPERTY_JSONRPC_PORT_STEP_FACTOR = "jsonrpc_http_port_step_factor";
    
    public static final String PROPERTY_GRPC_PORT = "grpc_tcp_port";
    
    public static final String PROPERTY_LOG_MONITOR_WEB_PORT = "logmonitor_web_port";
    
    public static final String PROPERTY_LOG_MONITOR_CONTAINER_NETWORK = "logmonitor_container_network";
    
    public static final String PROPERTY_LOG_MONITOR_WEB_PORT_STEP_FACTOR = "logmonitor_web_port_step_factor";
    
    public static final String PROPERTY_LOG_MONITOR_CONTAINER_NETWORK_STEP_FACTOR = "logmonitor_container_network_step_factor";
    
    /**
     * timeout
     */
    public static final int TIMEOUT_CONNECT = 1;
    
    public static final int TIMEOUT_READ = 1;
}