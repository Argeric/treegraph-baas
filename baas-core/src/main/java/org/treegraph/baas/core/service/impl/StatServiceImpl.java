package org.treegraph.baas.core.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.treegraph.baas.api.dto.response.QueryDashboardResponse;
import org.treegraph.baas.core.das.BlockchainDas;
import org.treegraph.baas.core.das.ConsortiumDas;
import org.treegraph.baas.core.das.NodeDas;
import org.treegraph.baas.core.das.UserInfoDas;
import org.treegraph.baas.core.service.StatService;

import lombok.AllArgsConstructor;

@Validated
@Service
@AllArgsConstructor
public class StatServiceImpl implements StatService {
	
	private final ConsortiumDas consortiumDas;

	private final BlockchainDas blockchainDas;
	
	private final NodeDas nodeDas;
	
	private final UserInfoDas userInfoDas;
	
	public QueryDashboardResponse queryDashboardInfo() {
		
		long countCons = consortiumDas.count();
		long countBlockchains = blockchainDas.count();
		long countNodes = nodeDas.count();
		long countUsers = userInfoDas.count();
		
		return QueryDashboardResponse.builder()
				.consCount(countCons)
				.blockchainCount(countBlockchains)
				.nodeCount(countNodes)
				.userCount(countUsers)
				.build();
	}
}
