package org.treegraph.baas.web.rest;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.treegraph.baas.api.rpc.dto.ScanResult;
import org.treegraph.baas.api.rpc.dto.request.ListBlockRequest;
import org.treegraph.baas.api.rpc.dto.request.ListContractRequest;
import org.treegraph.baas.api.rpc.dto.request.ListTransactionRequest;
import org.treegraph.baas.api.rpc.dto.request.QueryAddressRequest;
import org.treegraph.baas.api.rpc.dto.request.QueryBlockRequest;
import org.treegraph.baas.api.rpc.dto.request.QueryTransactionRequest;
import org.treegraph.baas.api.rpc.dto.request.QueryTypeRequest;
import org.treegraph.baas.api.rpc.dto.response.ListBlockResponse;
import org.treegraph.baas.api.rpc.dto.response.ListContractResponse;
import org.treegraph.baas.api.rpc.dto.response.ListTransactionResponse;
import org.treegraph.baas.api.rpc.dto.response.QueryAddressResponse;
import org.treegraph.baas.api.rpc.dto.response.QueryBlockResponse;
import org.treegraph.baas.api.rpc.dto.response.QueryTransactionResponse;
import org.treegraph.baas.api.rpc.dto.response.QueryTypeResponse;
import org.treegraph.baas.core.constant.ScanConstant;
import org.treegraph.baas.core.service.impl.ScanService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author zt
 * @date May 11, 2020
 * @version 1.0
 */
@RestController
@Api(tags = "浏览器模块", description = "浏览器管理")
@RequestMapping("/scan")
public class ScanController {
	
    @Resource
	private ScanService scanService;
	
	@Value("${scan.api.uri}")
	String uriApi;

	@GetMapping("/api/address/query")
	@ApiOperation("账户信息查询")
	public ScanResult<QueryAddressResponse> queryAddress(@RequestBody QueryAddressRequest queryAddressRequest)
			throws Exception {
		return scanService.invoke(uriApi + ScanConstant.URI_QUERY_ADDRESS, BeanUtils.describe(queryAddressRequest),
				new ParameterizedTypeReference<ScanResult<QueryAddressResponse>>() {});
	}

	@GetMapping("/api/block/list")
	@ApiOperation("区块列表")
	public ScanResult<ListBlockResponse> listBlock(@RequestBody ListBlockRequest listBlockRequest) throws Exception {
		return scanService.invoke(uriApi + ScanConstant.URI_LIST_BLOCK, BeanUtils.describe(listBlockRequest),
				new ParameterizedTypeReference<ScanResult<ListBlockResponse>>() {});
	}

	@GetMapping("/api/block/query")
	@ApiOperation("区块查询")
	public ScanResult<QueryBlockResponse> queryBlock(@RequestBody QueryBlockRequest queryBlockRequest)
			throws Exception {
		return scanService.invoke(uriApi + ScanConstant.URI_QUERY_BLOCK, BeanUtils.describe(queryBlockRequest),
				new ParameterizedTypeReference<ScanResult<QueryBlockResponse>>() {});
	}

	@GetMapping("/api/transaction/list")
	@ApiOperation("交易列表")
	public ScanResult<ListTransactionResponse> listTransaction(
			@RequestBody ListTransactionRequest listTransactionRequest) throws Exception {
		return scanService.invoke(uriApi + ScanConstant.URI_LIST_TRANSACTION, BeanUtils.describe(listTransactionRequest),
				new ParameterizedTypeReference<ScanResult<ListTransactionResponse>>() {});
	}

	@GetMapping("/api/transaction/query")
	@ApiOperation("交易查询")
	public ScanResult<QueryTransactionResponse> queryTransaction(
			@RequestBody QueryTransactionRequest queryTransactionRequest) throws Exception {
		return scanService.invoke(uriApi + ScanConstant.URI_QUERY_TRANSACTION, BeanUtils.describe(queryTransactionRequest),
				new ParameterizedTypeReference<ScanResult<QueryTransactionResponse>>() {});
	}

	@GetMapping("/api/util/type")
	@ApiOperation("搜索类型")
	public ScanResult<QueryTypeResponse> queryType(@RequestBody QueryTypeRequest queryTypeRequest) throws Exception {
		return scanService.invoke(uriApi + ScanConstant.URI_QUERY_TYPE, BeanUtils.describe(queryTypeRequest),
				new ParameterizedTypeReference<ScanResult<QueryTypeResponse>>() {});
	}

	@GetMapping("/api/contract/list")
	@ApiOperation("合约列表")
	public ScanResult<ListContractResponse> listContract(@RequestBody ListContractRequest listContractRequest)
			throws Exception {
		return scanService.invoke(uriApi + ScanConstant.URI_LIST_CONTRACT, BeanUtils.describe(listContractRequest),
				new ParameterizedTypeReference<ScanResult<ListContractResponse>>() {});
	}
}
