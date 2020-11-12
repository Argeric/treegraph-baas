package org.treegraph.baas.web.rest;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.assertj.core.util.Lists;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.treegraph.baas.api.dto.BaasResult;
import org.treegraph.baas.api.dto.ConsDetailInfo;
import org.treegraph.baas.api.dto.ConsInfo;
import org.treegraph.baas.api.dto.ListRequest;
import org.treegraph.baas.api.dto.request.AddNodesRequest;
import org.treegraph.baas.api.dto.request.CreateBlockchainRequest;
import org.treegraph.baas.api.dto.request.CreateConsAdminRequest;
import org.treegraph.baas.api.dto.request.CreateConsRequest;
import org.treegraph.baas.api.dto.request.DeferCertificateRequest;
import org.treegraph.baas.api.dto.request.DeleteConsRequest;
import org.treegraph.baas.api.dto.request.DownloadDeployScriptRequest;
import org.treegraph.baas.api.dto.request.ModifyConsRequest;
import org.treegraph.baas.api.dto.request.QueryConsDetailRequest;
import org.treegraph.baas.api.dto.request.QueryConsRequest;
import org.treegraph.baas.api.dto.response.ListConsResponse;
import org.treegraph.baas.common.util.PermHelper;
import org.treegraph.baas.core.service.ConsortiumService;
import org.treegraph.baas.core.service.ScriptService;
import org.treegraph.baas.core.util.LogSkip;
import org.treegraph.baas.core.util.SysManager;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.treegraph.baas.core.util.crypto.x509.KeypairCertUtil;

/**
 * @author zt
 * @version 1.0
 * @date May 11, 2020
 */
@RestController
@Api(tags = "联盟模块")
@RequestMapping("/cons")
@AllArgsConstructor
public class ConsortiumController {

  private final ConsortiumService consortiumService;

  private final ScriptService scriptService;

  private final KeypairCertUtil keypairCertUtil;

  @PostMapping("/list")
  @ApiOperation("联盟列表")
  public BaasResult<ListConsResponse> listCons(@RequestBody ListRequest listConsRequest) {
    ListConsResponse data = consortiumService.listCons(listConsRequest);
    return BaasResult.success(data);
  }

  @PostMapping("/query")
  @ApiOperation("查询联盟")
  public BaasResult<List<ConsInfo>> queryCons(@RequestBody QueryConsRequest queryConsRequest) {
    List<ConsInfo> data = consortiumService.queryCons(queryConsRequest);
    return BaasResult.success(data);
  }

  @PostMapping("/detail")
  @ApiOperation("联盟详情")
  public BaasResult<List<ConsDetailInfo>> queryConsDetail(
      @RequestBody QueryConsDetailRequest queryConsDetailRequest) {
    List<ConsDetailInfo> data = consortiumService.queryConsDetail(queryConsDetailRequest);
    return BaasResult.success(data);
  }

  @PostMapping("/create")
  @ApiOperation("创建联盟")
  public BaasResult<String> createCons(@RequestBody CreateConsRequest createConsRequest) {
    checkCreateConsAndAdmin();
    String consId = consortiumService.createCons(createConsRequest);
    return BaasResult.success(consId);
  }

  @PostMapping("/modify")
  @ApiOperation("修改联盟")
  public BaasResult<Void> modifyCons(@RequestBody ModifyConsRequest modifyConsRequest) {
    consortiumService.modifyCons(modifyConsRequest);
    return BaasResult.success();
  }

  @PostMapping("/del")
  @ApiOperation("删除联盟")
  public BaasResult<Boolean> deleteCons(@RequestBody DeleteConsRequest deleteConsRequest) {
    boolean successFlag = consortiumService.deleteCons(deleteConsRequest);
    return BaasResult.success(successFlag);
  }

  @PostMapping("/admin/create")
  @ApiOperation("创建联盟管理员")
  public BaasResult<Boolean> createConsAdmin(
      @RequestBody CreateConsAdminRequest createConsAdminRequest) {
    checkCreateConsAndAdmin();
    boolean successFlag = consortiumService.createConsAdmin(createConsAdminRequest);
    return BaasResult.success(successFlag);
  }

  @PostMapping("/blockchain/create")
  @ApiOperation("创建区块链")
  public BaasResult<String> createBlockchain(
      @RequestBody CreateBlockchainRequest createBlockchainRequest) {
    checkCreateBlockchainAndNode(createBlockchainRequest.getConsId());
    String blockchainId = consortiumService.createBlockchain(createBlockchainRequest);
    return BaasResult.success(blockchainId);
  }

  @PostMapping("/node/create")
  @ApiOperation("添加节点")
  public BaasResult<Boolean> addNodes(@RequestBody AddNodesRequest addNodesRequest)
      throws Exception {
    checkCreateBlockchainAndNode(addNodesRequest.getConsId());
    boolean successFlag = consortiumService.addNodes(addNodesRequest);
    return BaasResult.success(successFlag);
  }

  @LogSkip
  @PostMapping("/script/download")
  @ApiOperation("获取部署脚本")
  public void getDeployScript(@RequestBody DownloadDeployScriptRequest downloadDeployScriptRequest,
      HttpServletResponse response) throws Exception {
    scriptService.downloadDeployScript(downloadDeployScriptRequest, response);
  }

  @PostMapping("/cert/defer")
  @ApiOperation("证书延期")
  public BaasResult<Void> deferCert(@RequestBody DeferCertificateRequest deferCertificateRequest)
      throws Exception {
    consortiumService.deferConsortiumCert(deferCertificateRequest);
    return BaasResult.success();
  }

  @PostMapping("/root/gene")
  @ApiOperation("root gene")
  public BaasResult<Void> geneRootCa() {
    keypairCertUtil.geneRootCaFromVault();
    return BaasResult.success();
  }

  private void checkCreateConsAndAdmin() {
    Assert.isTrue(PermHelper.getSysAdminFlag(), "此操作需系统管理员权限");
  }

  private void checkCreateBlockchainAndNode(String consId) {
    SysManager.checkOwnCons(Lists.newArrayList(consId));
  }
}
