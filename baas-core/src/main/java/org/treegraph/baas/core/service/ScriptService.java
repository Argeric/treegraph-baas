package org.treegraph.baas.core.service;

import org.treegraph.baas.api.dto.request.DownloadDeployScriptRequest;

import javax.servlet.http.HttpServletResponse;

public interface ScriptService {
	void downloadDeployScript(DownloadDeployScriptRequest downloadDeployScriptRequest, HttpServletResponse response)
			throws Exception;
}
