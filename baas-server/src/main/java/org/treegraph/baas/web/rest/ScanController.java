package org.treegraph.baas.web.rest;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.treegraph.baas.core.service.impl.ScanService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author zt
 * @date May 11, 2020
 * @version 1.0
 */
@RestController
@Api(tags = "scan模块", description = "scan管理")
public class ScanController {
	
    @Resource
	private ScanService scanService;
	
	@Value("${uri.scan.scan}")
	String uriScan;
	
	@Value("${uri.scan.contract}")
	String uriContract;
	
	@PostMapping(value = {"/scan/dashboard/trend",
			"/scan/block/list",
			"/scan/transaction/list",
			"/contract/contract/list",
			"/scan/transfer/list",
			"/scan/util/type",
			"/scan/transaction/query",
			"/scan/block/query",
			"/scan/token/query",
			"/scan/address/query",
			"/contract/account/token/list",
			"/contract/contract/query"
	})
	@ApiOperation("scan请求转发")
	public Object listBlock(HttpServletRequest request, @RequestBody Map<String, String> requestParams) throws Exception {
		String requestUri = request.getRequestURI();
		String scanUri = uriScan + (requestUri.startsWith("/scan") ? requestUri.replaceFirst("scan", "api")
				: requestUri.replaceFirst("contract", "api"));
		return scanService.invoke(scanUri, requestParams);
	}
}
