package org.treegraph.baas.core.service;

import java.util.List;

import org.treegraph.baas.api.dto.BlockchainInfo;
import org.treegraph.baas.api.dto.ListRequest;
import org.treegraph.baas.api.dto.request.DownloadCertRequest;
import org.treegraph.baas.api.dto.request.ListCertRequest;
import org.treegraph.baas.api.dto.request.QueryBlockchainDetailRequest;
import org.treegraph.baas.api.dto.request.QueryBlockchainRequest;
import org.treegraph.baas.api.dto.request.SwitchBlockchainStatusRequest;
import org.treegraph.baas.api.dto.response.ListBlockchainResponse;
import org.treegraph.baas.api.dto.response.ListCertResponse;
import org.treegraph.baas.api.dto.response.QueryBlockchainDetailResponse;
import org.treegraph.baas.core.enums.BlockchainEnum;

public interface BlockchainService {

	ListBlockchainResponse listBlockchain(ListRequest listRequest);
	
	List<BlockchainInfo> queryBlockchain(QueryBlockchainRequest req);

	QueryBlockchainDetailResponse getBlockchainDetail(QueryBlockchainDetailRequest queryBlockchainDetailRequest);

	ListCertResponse listCert(ListCertRequest listCertRequest);

	boolean downloadCert(DownloadCertRequest downloadCertRequest);
	
    boolean switchBlockchainStatus(SwitchBlockchainStatusRequest switchBlockchainStatusRequest, BlockchainEnum.Status status);
}
