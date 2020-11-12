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
 * @version 1.0
 * @date May 11, 2020
 */
@RestController
@Api(tags = "scan模块")
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
      "/scan/transfer/list",
      "/scan/util/type",
      "/scan/transaction/query",
      "/scan/block/query",
      "/scan/token/query",
      "/scan/address/query",
      "/contract/account/token/list",
      "/contract/contract/list",
      "/contract/contract/query",
      "/contract/contract/create",
      "/contract/contract/update",
      "/contract/contract/delete"
  })
  @ApiOperation("scan请求转发")
  public Object listBlock(HttpServletRequest request,
      @RequestBody Map<String, Map<String, String>> requestParams) {
    Map<String, String> selfParams = requestParams.get("self");
    String chainId = selfParams.get("blockchainId");

    String requestUri = request.getRequestURI().replace("/" + chainId, "");
    String scanUri =
        requestUri.startsWith("/scan") ? (uriScan + requestUri.replaceFirst("scan", "api"))
            : (uriContract + requestUri.replaceFirst("contract", "api"));
    Map<String, String> scanParams = requestParams.get("scan");

    return scanService.invoke(scanUri, scanParams);
  }
}
