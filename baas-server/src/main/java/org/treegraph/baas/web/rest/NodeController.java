package org.treegraph.baas.web.rest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.treegraph.baas.api.dto.BaasResult;
import org.treegraph.baas.api.dto.NodeInfo;
import org.treegraph.baas.api.dto.request.DeleteNodeRequest;
import org.treegraph.baas.api.dto.request.ListNodeRequest;
import org.treegraph.baas.api.dto.request.ModifyNodeRequest;
import org.treegraph.baas.api.dto.request.QueryNodeDetailRequest;
import org.treegraph.baas.api.dto.request.QueryNodeLogRequest;
import org.treegraph.baas.api.dto.response.ListNodeResponse;
import org.treegraph.baas.core.service.NodeService;

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

	private NodeService nodeService;

	@PostMapping("/list")
	@ApiOperation("节点列表")
	public BaasResult<ListNodeResponse> listNodes(@RequestBody ListNodeRequest listNodeRequest) {
		ListNodeResponse nodes = nodeService.listNodes(listNodeRequest);
		return BaasResult.success(nodes);
	}
	
	@PostMapping("/detail")
	@ApiOperation("节点详情")
	public BaasResult<NodeInfo> getNodeDetail(@RequestBody QueryNodeDetailRequest queryNodeDetailRequest) {
		NodeInfo nodeInfo = nodeService.getNodeDetail(queryNodeDetailRequest);
		return BaasResult.success(nodeInfo);
	}
	
	@PostMapping("/log")
	@ApiOperation("节点日志")
	public BaasResult<String> getNodeLog(@RequestBody QueryNodeLogRequest queryNodeLogRequest) {
		String log = nodeService.getNodeLog(queryNodeLogRequest);
		return BaasResult.success(log);
	}
	
	@PostMapping("/modify")
	@ApiOperation("修改节点")
	public BaasResult<Void> modifyNode(@RequestBody ModifyNodeRequest modifyNodeRequest) {
		nodeService.modifyNode(modifyNodeRequest);
		return BaasResult.success();
	}
	
	@PostMapping("/del")
	@ApiOperation("修改节点")
	public BaasResult<Void> modifyNode(@RequestBody DeleteNodeRequest delNodeRequest) throws Exception {
		nodeService.deleteNode(delNodeRequest);
		return BaasResult.success();
	}
}
