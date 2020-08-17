package org.treegraph.baas.core.constant;

public class ConfluxConstant {
	
	/**
	 * rpc 
	 */
    public static final String RPC_BEATHEART = "getstatus";
    
    public static final String RPC_ADD_DELETE_NODE = "cfx_getMembershipChangeRawTransaction";
    
    public static final String RPC_ADD_DELETE_NODE_CONFIRM = "cfx_constructAndSendNewConsortiumMembershipTrans";
    
    /**
     * port
     */
    public static final int DEFAULT_JSON_RPC_HTTP_PORT = 12537;
    
    public static final int DEFAULT_JSON_RPC_LOCAL_HTTP_PORT = 12539;
    
    /**
     * test
     */
    public static final boolean TEST_SWITCH = true;
    
    public static final int TEST_PORT_STEP = 1000;
    
    /**
     * customized property
     */
    public static final String PROPERTY_JSON_RPC_HTTP_PORT = "jsonrpc_http_port";
    
    public static final String PROPERTY_JSON_RPC_LOCAL_HTTP_PORT = "jsonrpc_local_http_port";
    
    public static final String PROPERTY_MINING_AUTHOR = "mining_author";
    
    public static final String PROPERTY_JSON_RPC_HTTP_PORT_STEP_FACTOR = "jsonrpc_http_port_step_factor";
    
    public static final int PROPERTY_JSON_RPC_HTTP_PORT_STEP = 100;
}