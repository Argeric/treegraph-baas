package org.treegraph.baas.web.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.treegraph.baas.api.dto.BaasResult;
import org.treegraph.baas.api.dto.request.ListOperLogRequest;
import org.treegraph.baas.api.dto.request.LoginRequest;
import org.treegraph.baas.api.dto.response.ListOperLogResponse;
import org.treegraph.baas.api.dto.response.LoginResponse;
import org.treegraph.baas.core.service.LogService;
import org.treegraph.baas.core.service.LoginService;
import org.treegraph.baas.core.util.LogSkip;

@RestController
@Api(tags = "系统模块")
@RequestMapping("/sys")
@AllArgsConstructor
public class SysController {

  private final LoginService loginService;

  private final LogService logService;

  @PostMapping("/login")
  @ApiOperation("用户登陆接口")
  public BaasResult<LoginResponse> login(@RequestBody LoginRequest loginRequest,
      HttpServletRequest request) {
    LoginResponse userDetailInfo = loginService
        .login(loginRequest.getAccount(), loginRequest.getPassword(),
            request);
    return BaasResult.success(userDetailInfo);
  }

  @PostMapping("/log/list")
  @ApiOperation("操作日志")
  @LogSkip
  public BaasResult<ListOperLogResponse> listOperLog(
      @RequestBody ListOperLogRequest listOperLogRequest) {
    ListOperLogResponse data = logService.listOperLog(listOperLogRequest);
    return BaasResult.success(data);
  }
}
