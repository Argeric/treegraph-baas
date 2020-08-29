package org.treegraph.baas.core.heartbeat.bo;

import java.util.Map;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Heartbeat {
	
    private String id;
    private String ip;
    private int port;
    private String lastPong;
    private String lastPongMd5;
    
    private String ping;
    private String pong;
    private long pingTime;
    private long pongTime;
    private String pongMd5;
    
    private String error;
    private Map<String, Object> info;
}