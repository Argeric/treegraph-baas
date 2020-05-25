package org.treegraph.baas.web.rest;

import javax.servlet.http.HttpServletRequest;

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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;

@RestController
@Api(tags = "系统模块", description = "登录管理相关")
@RequestMapping("/sys")
@AllArgsConstructor
public class SysController {

	private LoginService loginService;
	
	private LogService logService;

	@PostMapping("/login")
	@ApiOperation("用户登陆接口")
	public BaasResult<LoginResponse> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {

		LoginResponse userDetailInfo = loginService.login(loginRequest.getAccount(), loginRequest.getPassword(),
				request);
		BaasResult<LoginResponse> resp = BaasResult.success(userDetailInfo);
		return resp;
	}

	@PostMapping("/log/list")
	@ApiOperation("操作日志")
	@LogSkip
	public BaasResult<ListOperLogResponse> listOperLog(@RequestBody ListOperLogRequest listOperLogRequest) {
		ListOperLogResponse data = logService.listOperLog(listOperLogRequest);
		return BaasResult.success(data);
	}
}
