package org.treegraph.baas.core.service;

import javax.servlet.http.HttpServletRequest;

import org.treegraph.baas.api.dto.request.ListOperLogRequest;
import org.treegraph.baas.api.dto.response.ListOperLogResponse;
import org.treegraph.baas.core.enums.LogEnum.Login;

public interface LogService {

  void addLoginLog(String userId, String userName, Login login, HttpServletRequest request);

  void addOperLog(String userId, String userName, String ip, String device, String operCode,
      String operInfo);

  ListOperLogResponse listOperLog(ListOperLogRequest req);
}
