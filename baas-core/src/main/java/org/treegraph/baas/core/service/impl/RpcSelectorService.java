package org.treegraph.baas.core.service.impl;

import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.treegraph.baas.core.service.BlockchainRpcService;

@Service
@Slf4j
public class RpcSelectorService {

	@Value("${rpc.type}")
	private String rpcType;
	
	private final Map<String, BlockchainRpcService> rpcStrategyMap = Maps.newHashMap();

	public RpcSelectorService(List<BlockchainRpcService> rpcServices){
		for(BlockchainRpcService rpcService:rpcServices){
			log.info("{}.service.loaded", rpcService.getType());
			rpcStrategyMap.put(rpcService.getType(), rpcService);
		}
	}

	public BlockchainRpcService getRpcService(){
		return rpcStrategyMap.get(rpcType);
	}
}
