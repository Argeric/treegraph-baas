package org.treegraph.baas.web.rest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.treegraph.baas.api.dto.BaasResult;
import org.treegraph.baas.api.dto.request.AddNodesRequest;
import org.treegraph.baas.api.dto.request.CreateBlockchainRequest;
import org.treegraph.baas.api.dto.request.CreateConsAdminRequest;
import org.treegraph.baas.api.dto.request.CreateConsRequest;
import org.treegraph.baas.api.dto.request.DeleteConsRequest;
import org.treegraph.baas.api.dto.request.GetDeployScriptRequest;
import org.treegraph.baas.api.dto.request.ListConsRequest;
import org.treegraph.baas.api.dto.request.ModifyConsRequest;
import org.treegraph.baas.api.dto.response.ListConsResponse;
import org.treegraph.baas.core.service.ConsortiumService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;

/**
 * @author zt
 * @date May 11, 2020
 * @version 1.0
 */
@RestController
@Api(tags = "联盟模块", description = "联盟管理")
@RequestMapping("/cons")
@AllArgsConstructor
public class ConsortiumController {

	private ConsortiumService consortiumService;

	@PostMapping("/list")
	@ApiOperation("联盟列表")
	public BaasResult<ListConsResponse> listCons(@RequestBody ListConsRequest listConsRequest) {
		ListConsResponse data = consortiumService.listCons(listConsRequest);
		return BaasResult.success(data);
	}
	
	@PostMapping("/create")
	@ApiOperation("创建联盟")
	public BaasResult<Void> createCons(@RequestBody CreateConsRequest createConsRequest) {
		consortiumService.createCons(createConsRequest);
		return BaasResult.success();
	}
	
	@PostMapping("/modify")
	@ApiOperation("修改联盟")
	public BaasResult<Void> modifyCons(@RequestBody ModifyConsRequest modifyConsRequest) {
		consortiumService.modifyCons(modifyConsRequest);
		return BaasResult.success();
	}
	
	@PostMapping("/del")
	@ApiOperation("删除联盟")
	public BaasResult<Void> deleteCons(@RequestBody DeleteConsRequest deleteConsRequest) {
		consortiumService.deleteCons(deleteConsRequest);
		return BaasResult.success();
	}
	
	@PostMapping("/admin/create")
	@ApiOperation("创建联盟管理员")
	public BaasResult<Void> createConsAdmin(@RequestBody CreateConsAdminRequest createConsAdminRequest) {
		consortiumService.createConsAdmin(createConsAdminRequest);
		return BaasResult.success();
	}
	
	@PostMapping("/blockchain/create")
	@ApiOperation("创建区块链")
	public BaasResult<Void> createBlockchain(@RequestBody CreateBlockchainRequest createBlockchainRequest) {
		consortiumService.createBlockchain(createBlockchainRequest);
		return BaasResult.success();
	}
	
	@PostMapping("/node/create")
	@ApiOperation("添加节点")
	public BaasResult<Void> addNodes(@RequestBody AddNodesRequest addNodesRequest) {
		consortiumService.addNodes(addNodesRequest);
		return BaasResult.success();
	}
	
	@PostMapping("/script/query")
	@ApiOperation("获取部署脚本")
	public BaasResult<Void> getDeployScript(@RequestBody GetDeployScriptRequest getDeployScriptRequest) {
		consortiumService.getDeployScript(getDeployScriptRequest);
		return BaasResult.success();
	}
}
