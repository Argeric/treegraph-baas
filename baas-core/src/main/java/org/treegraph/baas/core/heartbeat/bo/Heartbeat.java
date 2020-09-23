package org.treegraph.baas.core.heartbeat.bo;

import java.util.Map;

import org.treegraph.baas.core.enums.HeartbeatEnum;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Heartbeat {
	 
    private String id;
    private String ip; 
    private int port;
	@JSONField(serialize=false)  
    private String lastPong;
    @JSONField(serialize=false)  
    private String lastPongMd5;
    
    private String ping;
    private String pong;
    @JSONField(serialize=false)  
    private long pingTime;
    @JSONField(serialize=false)  
    private long pongTime;
    @JSONField(serialize=false)  
    private String pongMd5;
    
    private String error;
    @JSONField(serialize=false)  
    private Map<String, Object> info;
    @JSONField(serialize=false)  
    private HeartbeatEnum.Status status;
}