package org.treegraph.baas.core.service;

import org.treegraph.baas.api.dto.request.DeployNodeRequest;
import org.treegraph.baas.api.dto.request.DownloadDeployScriptRequest;

import javax.servlet.http.HttpServletResponse;

public interface ScriptService {

  void deployNode(DeployNodeRequest deployNodeRequest) throws Exception ;

  byte[] downloadDeployScript(DownloadDeployScriptRequest downloadDeployScriptRequest,
      HttpServletResponse response)
      throws Exception;
}
