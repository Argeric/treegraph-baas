package org.treegraph.baas.web.rest;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.treegraph.baas.api.dto.BaasResult;
import org.treegraph.baas.api.dto.request.LoginRequest;
import org.treegraph.baas.api.dto.response.LoginResponse;
import org.treegraph.baas.core.service.LoginService;
import org.treegraph.baas.core.util.LogSkip;

import com.alibaba.fastjson.JSON;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@Api(tags = "LoginController", description = "登录管理(此controller下所有接口不做登录控制)")
public class LoginController {

  @Autowired
  private LoginService loginService;

  @PostMapping("/login")
  @ApiOperation("用户登陆接口")
  @LogSkip
  public BaasResult<LoginResponse> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {
	  
	  log.info("LoginController.login req,account:{},password:{}", loginRequest.getAccount(), loginRequest.getPassword());
	  LoginResponse userDetailInfo =  loginService.login(loginRequest.getAccount(), loginRequest.getPassword(), request);
	  BaasResult<LoginResponse> resp =  BaasResult.success(userDetailInfo);
	  log.info("LoginController.login resp:{}", JSON.toJSONString(resp));
	  
	  return resp;
  }
}
