package org.treegraph.baas.web.rest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.treegraph.baas.api.dto.BaasResult;
import org.treegraph.baas.api.dto.response.QueryDashboardResponse;
import org.treegraph.baas.core.service.StatService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;

/**
 * @author zt
 * @date May 11, 2020
 * @version 1.0
 */
@RestController
@Api(tags = "统计模块")
@RequestMapping("/stat")
@AllArgsConstructor
public class StatController {
	
	private final StatService statService;

	@PostMapping("/dashboard")
	@ApiOperation("综合数据统计")
	public BaasResult<QueryDashboardResponse> queryDashboardInfo() {
		QueryDashboardResponse data = statService.queryDashboardInfo();
		return BaasResult.success(data);
	}
}