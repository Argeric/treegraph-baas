package org.treegraph.baas.core.service;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.treegraph.baas.api.dto.ConsDetailInfo;
import org.treegraph.baas.api.dto.ConsInfo;
import org.treegraph.baas.api.dto.ListRequest;
import org.treegraph.baas.api.dto.request.AddNodesRequest;
import org.treegraph.baas.api.dto.request.CreateBlockchainRequest;
import org.treegraph.baas.api.dto.request.CreateConsAdminRequest;
import org.treegraph.baas.api.dto.request.CreateConsRequest;
import org.treegraph.baas.api.dto.request.DeferCertificateRequest;
import org.treegraph.baas.api.dto.request.DeleteConsRequest;
import org.treegraph.baas.api.dto.request.DownloadDeployScriptRequest;
import org.treegraph.baas.api.dto.request.ModifyConsRequest;
import org.treegraph.baas.api.dto.request.QueryConsDetailRequest;
import org.treegraph.baas.api.dto.request.QueryConsRequest;
import org.treegraph.baas.api.dto.response.ListConsResponse;

public interface ConsortiumService {

  ListConsResponse listCons(ListRequest listRequest);

  List<ConsInfo> queryCons(QueryConsRequest req);

  List<ConsDetailInfo> queryConsDetail(QueryConsDetailRequest req);

  String createCons(CreateConsRequest createConsRequest);

  void modifyCons(ModifyConsRequest modifyConsRequest);

  boolean deleteCons(DeleteConsRequest deleteConsRequest);

  boolean createConsAdmin(CreateConsAdminRequest createConsAdminRequest);

  String createBlockchain(CreateBlockchainRequest createBlockchainRequest);

  boolean addNodes(AddNodesRequest addNodesRequest) throws Exception;

  void deferConsortiumCert(DeferCertificateRequest req) throws Exception;
}
