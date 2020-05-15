package org.treegraph.baas.core.service;

import org.treegraph.baas.api.dto.ListRequest;
import org.treegraph.baas.api.dto.request.AddNodesRequest;
import org.treegraph.baas.api.dto.request.CreateBlockchainRequest;
import org.treegraph.baas.api.dto.request.CreateConsAdminRequest;
import org.treegraph.baas.api.dto.request.CreateConsRequest;
import org.treegraph.baas.api.dto.request.DeleteConsRequest;
import org.treegraph.baas.api.dto.request.DownloadDeployScriptRequest;
import org.treegraph.baas.api.dto.request.ModifyConsRequest;
import org.treegraph.baas.api.dto.response.ListConsResponse;

public interface ConsortiumService {

	ListConsResponse listCons(ListRequest listRequest);

	boolean createCons(CreateConsRequest createConsRequest);

	boolean modifyCons(ModifyConsRequest modifyConsRequest);

	boolean deleteCons(DeleteConsRequest deleteConsRequest);

	boolean createConsAdmin(CreateConsAdminRequest createConsAdminRequest);

	boolean createBlockchain(CreateBlockchainRequest createBlockchainRequest);

	boolean addNodes(AddNodesRequest addNodesRequest);

	void getDeployScript(DownloadDeployScriptRequest getDeployScriptRequest);
}
