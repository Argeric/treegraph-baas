package org.treegraph.baas.web.rest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Maps;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.treegraph.baas.api.dto.BaasResult;
import org.treegraph.baas.api.dto.request.*;
import org.treegraph.baas.api.dto.response.ListFavoriteResponse;
import org.treegraph.baas.api.dto.response.ListOperLogResponse;
import org.treegraph.baas.core.util.Order;

import java.util.Map;

public class ScanTests extends AbstractBaasIntegrationTests {

	@Test
	@Order(2)
	public void testRedirectScan() throws Exception {
		Map<String, Map<String, String>> requestParams = Maps.newHashMap();
		Map<String, String> selfParams = Maps.newHashMap();
		//selfParams.put("blockchainId", "blockchainId");
		requestParams.put("self", selfParams);

		Map<String, String> scanParams = Maps.newHashMap();
		scanParams.put("page", "1");
		scanParams.put("pageSize", "10");
		requestParams.put("scan", scanParams);

		MvcResult mvcResult = getMockMvc().perform(MockMvcRequestBuilders.post("/scan/block/list")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.header("X-Token", token)
				.content(JSON.toJSONString(requestParams)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andDo(MockMvcResultHandlers.print())
				.andReturn();
		String scanResp = mvcResult.getResponse().getContentAsString();
		Assert.assertNotNull("testRedirectScan error", scanResp);
	}

	@Test
	@Order(3)
	public void testListOperLog() throws Exception {
		ListOperLogRequest listOperLogRequest = ListOperLogRequest.builder()
				.current(1)
				.pageSize(10)
				.build();
		MvcResult mvcResult = getMockMvc().perform(MockMvcRequestBuilders.post("/log/list")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.header("X-Token", token)
				.content(JSON.toJSONString(listOperLogRequest)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andDo(MockMvcResultHandlers.print())
				.andReturn();
		BaasResult<ListOperLogResponse> baasResult = JSON.parseObject(mvcResult.getResponse().getContentAsString(),
				new TypeReference<>() {
				});
		ListOperLogResponse listOperLogResponse = baasResult.getData();
		Assert.assertNotNull("testListOperLog error", listOperLogResponse);
	}


	@Test
	@Order(4)
	public void testAddFavorite() throws Exception {
		/*
		EPOCH(0, "纪元"),
		BLOCK(1, "区块"),
		TRANSACTION(2, "交易"),
		ACCOUNT(3, "账户"),
		SMART_CONTRACT(4, "合约")
		*/
		AddScanFavoriteRequest addScanFavoriteRequest = AddScanFavoriteRequest.builder()
				.consId(null)
				.blockchainId("blockchainId")
				.type(1)
				.hash("hash")
				.remark("remark")
				.build();
		MvcResult mvcResult = getMockMvc().perform(MockMvcRequestBuilders.post("/scan/fav/create")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.header("X-Token", token)
				.content(JSON.toJSONString(addScanFavoriteRequest)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andDo(MockMvcResultHandlers.print())
				.andReturn();
		BaasResult<Long> baasResult = JSON.parseObject(mvcResult.getResponse().getContentAsString(),
				new TypeReference<>() {
				});
		Long addScanFavoriteResp = baasResult.getData();
		Assert.assertNotNull("testAddFavorite error", addScanFavoriteResp);
	}


	@Test
	@Order(5)
	public void testModifyFavorite() throws Exception {
		ModifyFavoriteRequest modifyFavoriteRequest = ModifyFavoriteRequest.builder()
				.id(0L)
				.remark("remark")
				.build();
		MvcResult mvcResult = getMockMvc().perform(MockMvcRequestBuilders.post("/scan/fav/modify")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.header("X-Token", token)
				.content(JSON.toJSONString(modifyFavoriteRequest)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andDo(MockMvcResultHandlers.print())
				.andReturn();
		BaasResult<Boolean> baasResult = JSON.parseObject(mvcResult.getResponse().getContentAsString(),
				new TypeReference<>() {
				});
		Boolean modifyFavoriteResp = baasResult.getData();
		Assert.assertTrue("testModifyFavorite error", modifyFavoriteResp);
	}

	@Test
	@Order(6)
	public void testListFavorites() throws Exception {
		ListFavoriteRequest listFavoriteRequest = ListFavoriteRequest.builder()
				.current(1)
				.pageSize(10)
				.build();
		MvcResult mvcResult = getMockMvc().perform(MockMvcRequestBuilders.post("/scan/fav/list")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.header("X-Token", token)
				.content(JSON.toJSONString(listFavoriteRequest)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andDo(MockMvcResultHandlers.print())
				.andReturn();
		BaasResult<ListFavoriteResponse> baasResult = JSON.parseObject(mvcResult.getResponse().getContentAsString(),
				new TypeReference<>() {
				});
		ListFavoriteResponse listFavoriteResp = baasResult.getData();
		Assert.assertNotNull("testListFavorites error", listFavoriteResp);
	}

	@Test
	@Order(7)
	public void testDeleteFavorite() throws Exception {
		DeleteFavoriteRequest deleteFavoriteRequest = DeleteFavoriteRequest.builder()
				.id(0L)
				.build();
		MvcResult mvcResult = getMockMvc().perform(MockMvcRequestBuilders.post("/scan/fav/delete")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.header("X-Token", token)
				.content(JSON.toJSONString(deleteFavoriteRequest)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andDo(MockMvcResultHandlers.print())
				.andReturn();
		BaasResult<Boolean> baasResult = JSON.parseObject(mvcResult.getResponse().getContentAsString(),
				new TypeReference<>() {
				});
		Boolean deleteFavoriteResp = baasResult.getData();
		Assert.assertTrue("testDeleteFavorite error", deleteFavoriteResp);
	}

	@Override
	boolean needLogin() {
		return true;
	}
}
