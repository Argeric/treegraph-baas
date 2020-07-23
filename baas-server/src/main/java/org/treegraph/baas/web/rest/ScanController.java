package org.treegraph.baas.web.rest;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
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
	
	@PostMapping(value = {"/scan/dashboard/trend/{chain}",
			"/scan/block/list/{chain}",
			"/scan/transaction/list/{chain}",
			"/contract/contract/list/{chain}",
			"/scan/transfer/list/{chain}",
			"/scan/util/type/{chain}",
			"/scan/transaction/query/{chain}",
			"/scan/block/query/{chain}",
			"/scan/token/query/{chain}",
			"/scan/address/query/{chain}",
			"/contract/account/token/list/{chain}",
			"/contract/contract/query/{chain}"
	})
	@ApiOperation("scan请求转发")
	public Object listBlock(HttpServletRequest request, @PathVariable("chain") String chainId, @RequestBody Map<String, String> requestParams) throws Exception {
		String requestUri = request.getRequestURI();
		requestUri = requestUri.replace("/"+chainId, "");
		String scanUri = uriScan + (requestUri.startsWith("/scan") ? requestUri.replaceFirst("scan", "api")
				: requestUri.replaceFirst("contract", "api"));
		return scanService.invoke(scanUri, requestParams);
	}
	
	public static void main(String[] args) {
		String s = "http://101.200.183.108:8885/api/block/list/6734bd03-cc14-462f-b652-af5e1016f3cc";
		String chain = "6734bd03-cc14-462f-b652-af5e1016f3cc";
		String re = s.replaceFirst("/"+chain, "");
		System.out.println(re);
	}
}
