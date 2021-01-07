package org.treegraph.baas.core.service;

import org.treegraph.baas.api.dto.request.DownloadRpcCertRequest;

import javax.servlet.http.HttpServletResponse;

public interface CertService {

  void downloadRpcCert(DownloadRpcCertRequest req, HttpServletResponse resp) throws Exception;
}
