package org.treegraph.baas.core.service;

import org.treegraph.baas.api.dto.request.DeployNodeRequest;
import org.treegraph.baas.api.dto.request.DetectReachableRequest;
import org.treegraph.baas.api.dto.request.DownloadDeployScriptRequest;
import org.treegraph.baas.api.dto.response.DetectReachableResponse;

import javax.servlet.http.HttpServletResponse;

public interface ScriptService {

  DetectReachableResponse detectReachable(DetectReachableRequest req);

  void deployNode(DeployNodeRequest deployNodeRequest) throws Exception ;

  byte[] downloadDeployScript(DownloadDeployScriptRequest downloadDeployScriptRequest,
      HttpServletResponse response)
      throws Exception;
}
