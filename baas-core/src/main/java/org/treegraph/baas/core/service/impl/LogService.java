package org.treegraph.baas.core.service.impl;

import java.time.LocalDateTime;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.treegraph.baas.core.domain.UserLoginLogPo;
import org.treegraph.baas.core.domain.UserOperLogPo;
import org.treegraph.baas.core.enums.LogEnum;
import org.treegraph.baas.core.enums.LogEnum.Login;
import org.treegraph.baas.core.mapper.UserLoginLogPoMapper;
import org.treegraph.baas.core.mapper.UserOperLogPoMapper;
import org.treegraph.baas.core.util.AddressUtils;
import org.treegraph.baas.core.util.WebUtils;

@Service
public class LogService {

  @Autowired
  private UserLoginLogPoMapper userLoginLogPoMapper;

  @Autowired
  private UserOperLogPoMapper userOperLogPoMapper;

  /**
   * 添加登录登出日志
   */
  @Async
  public void addLoginLog(String userId, Login login, HttpServletRequest request) {
	UserLoginLogPo loginLog = new UserLoginLogPo();
    String ip = Objects.isNull(request) ? "" : WebUtils.getIpAddress(request);
    loginLog.setUserId(userId);
    loginLog.setLoginIp(ip);
    loginLog.setIpAddress(AddressUtils.getAddressByIp(ip));
    loginLog.setType(login.getType());
    loginLog.setDevice(Objects.isNull(request) ? "" : WebUtils.getDeviceeInfo(request));
    loginLog.setLoginTime(LocalDateTime.now());
    userLoginLogPoMapper.insertSelective(loginLog);
  }

  /**
   * 添加操作记录日志
   */
  @Async
  public void addOperateLog(String content, LogEnum.Operate operateEnum, String user) {
	UserOperLogPo operLog = new UserOperLogPo();
	operLog.setCreateTime(LocalDateTime.now());
    userOperLogPoMapper.insertSelective(operLog);
  }

  /**
   * 添加操作记录日志
   */
  @Async
  public void addFormatOperateLog(String user, String contentFormat, Object... params) {
	  UserOperLogPo operLog = new UserOperLogPo();
	  operLog.setCreateTime(LocalDateTime.now());
	  userOperLogPoMapper.insertSelective(operLog);
  }
}
