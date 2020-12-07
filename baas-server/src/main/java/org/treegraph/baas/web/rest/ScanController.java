package org.treegraph.baas.web.rest;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.treegraph.baas.api.dto.BaasResult;
import org.treegraph.baas.api.dto.ConsDetailInfo;
import org.treegraph.baas.api.dto.request.AddScanFavoriteRequest;
import org.treegraph.baas.api.dto.request.DeleteFavoriteRequest;
import org.treegraph.baas.api.dto.request.DownloadRpcCertRequest;
import org.treegraph.baas.api.dto.request.ListFavoriteRequest;
import org.treegraph.baas.api.dto.request.ListNodeRequest;
import org.treegraph.baas.api.dto.request.ModifyFavoriteRequest;
import org.treegraph.baas.api.dto.response.ListFavoriteResponse;
import org.treegraph.baas.api.dto.response.ListNodeResponse;
import org.treegraph.baas.core.service.impl.ScanService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.treegraph.baas.core.util.LogSkip;

/**
 * @author zt
 * @version 1.0
 * @date May 11, 2020
 */
@RestController
@Api(tags = "scan模块")
public class ScanController {

  @Autowired
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
  public Object redirectScan(HttpServletRequest request,
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

  @LogSkip
  @PostMapping("/scan/fav/create")
  @ApiOperation("添加收藏")
  public BaasResult<Long> addFavorite(@RequestBody AddScanFavoriteRequest addScanFavoriteRequest) {
    Long favoriteId = scanService.addFavorite(addScanFavoriteRequest);
    return BaasResult.success(favoriteId);
  }

  @LogSkip
  @PostMapping("/scan/fav/modify")
  @ApiOperation("编辑收藏")
  public BaasResult<Boolean> modifyFavorite(@RequestBody ModifyFavoriteRequest modifyFavoriteRequest) {
    boolean successFlag = scanService.modifyFavorite(modifyFavoriteRequest);
    return BaasResult.success(successFlag);
  }

  @LogSkip
  @PostMapping("/scan/fav/delete")
  @ApiOperation("删除收藏")
  public BaasResult<Boolean> deleteFavorite(@RequestBody DeleteFavoriteRequest deleteFavoriteRequest) {
    boolean successFlag = scanService.deleteFavorite(deleteFavoriteRequest);
    return BaasResult.success(successFlag);
  }

  @PostMapping("/scan/fav/list")
  @ApiOperation("收藏列表")
  public BaasResult<ListFavoriteResponse> listFavorites(@RequestBody ListFavoriteRequest listFavoriteRequest) {
    ListFavoriteResponse favorites = scanService.listFavorites(listFavoriteRequest);
    return BaasResult.success(favorites);
  }
}
