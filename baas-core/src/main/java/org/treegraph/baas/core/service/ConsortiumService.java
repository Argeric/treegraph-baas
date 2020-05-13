package org.treegraph.baas.core.service;

import org.treegraph.baas.api.dto.request.AddNodesRequest;
import org.treegraph.baas.api.dto.request.CreateBlockchainRequest;
import org.treegraph.baas.api.dto.request.CreateConsAdminRequest;
import org.treegraph.baas.api.dto.request.CreateConsRequest;
import org.treegraph.baas.api.dto.request.DeleteConsRequest;
import org.treegraph.baas.api.dto.request.GetDeployScriptRequest;
import org.treegraph.baas.api.dto.request.ListConsRequest;
import org.treegraph.baas.api.dto.request.ModifyConsRequest;
import org.treegraph.baas.api.dto.response.ListConsResponse;

public interface ConsortiumService {

	ListConsResponse listCons(ListConsRequest listConsRequest);

	boolean createCons(CreateConsRequest createConsRequest);

	boolean modifyCons(ModifyConsRequest modifyConsRequest);

	boolean deleteCons(DeleteConsRequest deleteConsRequest);

	boolean createConsAdmin(CreateConsAdminRequest createConsAdminRequest);

	boolean createBlockchain(CreateBlockchainRequest createBlockchainRequest);

	boolean addNodes(AddNodesRequest addNodesRequest);

	void getDeployScript(GetDeployScriptRequest getDeployScriptRequest);
}
