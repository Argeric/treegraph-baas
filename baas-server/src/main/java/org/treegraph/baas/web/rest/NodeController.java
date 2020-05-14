package org.treegraph.baas.web.rest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.treegraph.baas.api.dto.BaasResult;
import org.treegraph.baas.api.dto.request.CreateConsRequest;
import org.treegraph.baas.api.dto.request.DeleteConsRequest;
import org.treegraph.baas.api.dto.request.ListConsRequest;
import org.treegraph.baas.api.dto.request.ModifyConsRequest;
import org.treegraph.baas.api.dto.response.ListConsResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;

/**
 * @author zt
 * @date May 11, 2020
 * @version 1.0
 */
@RestController
@Api(tags = "节点模块", description = "节点管理")
@RequestMapping("/node")
@AllArgsConstructor
public class NodeController {

	//private NodeService nodeService;

	@PostMapping("/list")
	@ApiOperation("节点列表")
	public BaasResult<ListConsResponse> listNodes(@RequestBody ListConsRequest listConsRequest) {
		
		return BaasResult.success();
	}
	
	@PostMapping("/detail")
	@ApiOperation("节点详情")
	public BaasResult<Void> getNodeDetail(@RequestBody CreateConsRequest createConsRequest) {
		
		return BaasResult.success();
	}
	
	@PostMapping("/log")
	@ApiOperation("节点日志")
	public BaasResult<Void> getNodeLog(@RequestBody ModifyConsRequest modifyConsRequest) {
		
		return BaasResult.success();
	}
	
	@PostMapping("/modify")
	@ApiOperation("修改节点")
	public BaasResult<Void> modifyNode(@RequestBody DeleteConsRequest deleteConsRequest) {
		
		return BaasResult.success();
	}
	
}
