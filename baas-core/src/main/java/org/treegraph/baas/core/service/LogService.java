package org.treegraph.baas.core.service;

import javax.servlet.http.HttpServletRequest;

import org.treegraph.baas.api.dto.request.ListOperLogRequest;
import org.treegraph.baas.api.dto.response.ListOperLogResponse;
import org.treegraph.baas.core.enums.LogEnum.Login;

public interface LogService {

	public void addLoginLog(String userId, Login login, HttpServletRequest request);

	public void addOperLog(String userId, String userName, String ip, String operCode, String operInfo);

	public ListOperLogResponse listOperLog(ListOperLogRequest req);
}
