package org.treegraph.baas.web.rest;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.treegraph.baas.api.dto.BaasResult;
import org.treegraph.baas.api.dto.BlockchainInfo;
import org.treegraph.baas.api.dto.ListRequest;
import org.treegraph.baas.api.dto.request.DownloadCertRequest;
import org.treegraph.baas.api.dto.request.ListCertRequest;
import org.treegraph.baas.api.dto.request.QueryBlockchainDetailRequest;
import org.treegraph.baas.api.dto.request.QueryBlockchainRequest;
import org.treegraph.baas.api.dto.request.SwitchBlockchainStatusRequest;
import org.treegraph.baas.api.dto.response.ListBlockchainResponse;
import org.treegraph.baas.api.dto.response.ListCertResponse;
import org.treegraph.baas.api.dto.response.QueryBlockchainDetailResponse;
import org.treegraph.baas.core.enums.BlockchainEnum;
import org.treegraph.baas.core.service.BlockchainService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;

/**
 * @author zt
 * @date May 11, 2020
 * @version 1.0
 */
@RestController
@Api(tags = "区块链模块", description = "区块链管理")
@RequestMapping("/blockchain")
@AllArgsConstructor
public class BlockchainController {
	
	private BlockchainService blockchainService;

	@PostMapping("/list")
	@ApiOperation("区块链列表")
	public BaasResult<ListBlockchainResponse> listBlockchain(@RequestBody ListRequest listRequest) {
		ListBlockchainResponse data = blockchainService.listBlockchain(listRequest);
		return BaasResult.success(data);
	}
	
	@PostMapping("/query")
	@ApiOperation("区块链查询")
	public BaasResult<List<BlockchainInfo>> queryBlockchain(@RequestBody QueryBlockchainRequest queryBlockchainRequest) {
		List<BlockchainInfo> data = blockchainService.queryBlockchain(queryBlockchainRequest);
		return BaasResult.success(data);
	}
	
	@PostMapping("/detail")
	@ApiOperation("区块链详情")
	public BaasResult<QueryBlockchainDetailResponse> queryBlockchainDetail(@RequestBody QueryBlockchainDetailRequest queryBlockchainDetailRequest) {
		QueryBlockchainDetailResponse data = blockchainService.getBlockchainDetail(queryBlockchainDetailRequest);
		return BaasResult.success(data);
	}

	@PostMapping("/ca/list")
	@ApiOperation("证书生成历史列表")
	public BaasResult<ListCertResponse> listCert(@RequestBody ListCertRequest listCertRequest) {
		ListCertResponse data = blockchainService.listCert(listCertRequest);
		return BaasResult.success(data);
	}
	
	@PostMapping("/ca/download")
	@ApiOperation("下载（申请）证书")
	public BaasResult<Void> downloadCert(@RequestBody DownloadCertRequest downloadCertRequest) {
		blockchainService.downloadCert(downloadCertRequest);
		return BaasResult.success();
	}
	
	@PostMapping("/stop")
	@ApiOperation("停止区块链")
	public BaasResult<Void> stopBlockchain(@RequestBody SwitchBlockchainStatusRequest switchBlockchainStatusRequest) {
		blockchainService.switchBlockchainStatus(switchBlockchainStatusRequest, BlockchainEnum.Status.STOPED);
		return BaasResult.success();
	}
	
	@PostMapping("/start")
	@ApiOperation("启动区块链")
	public BaasResult<Void> startBlockchain(@RequestBody SwitchBlockchainStatusRequest switchBlockchainStatusRequest) {
		blockchainService.switchBlockchainStatus(switchBlockchainStatusRequest, BlockchainEnum.Status.RUNING);
		return BaasResult.success();
	}
}
