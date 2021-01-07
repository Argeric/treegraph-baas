package org.treegraph.baas.core.das;

import java.util.List;

import org.treegraph.baas.core.domain.UserOperLogPo;

public interface UserOperLogDas {

  void addOperLog(UserOperLogPo userOperLog);

  List<UserOperLogPo> findAllOperLog(String userId, String userName, String operCode, String ip);
}
