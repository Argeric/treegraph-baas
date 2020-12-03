package org.treegraph.baas.web.rest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.treegraph.baas.api.dto.BaasResult;
import org.treegraph.baas.api.dto.UserInfo;
import org.treegraph.baas.api.dto.request.ListUserRequest;
import org.treegraph.baas.api.dto.request.QueryUserRequest;
import org.treegraph.baas.api.dto.response.ListUserResponse;
import org.treegraph.baas.core.util.Order;

import java.util.List;

public class UserTests extends AbstractBaasIntegrationTests{

	@Test
	@Order(2)
	public void testListUser() throws Exception {
		ListUserRequest listUserRequest = ListUserRequest.builder()
				.current(0)
				.pageSize(10)
				.build();
		MvcResult mvcResult = getMockMvc().perform(MockMvcRequestBuilders.post("/user/list")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.header("X-Token", token)
				.content(JSON.toJSONString(listUserRequest)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andDo(MockMvcResultHandlers.print())
				.andReturn();
		BaasResult<ListUserResponse> baasResult = JSON.parseObject(mvcResult.getResponse().getContentAsString(),
				new TypeReference<>() {
				});
		ListUserResponse listUserResponse = baasResult.getData();
		Assert.assertNotNull("testListUser error", listUserResponse);
	}

	@Test
	@Order(3)
	public void testQueryUser() throws Exception {
		QueryUserRequest queryUserRequest = QueryUserRequest.builder()
				.build();
		MvcResult mvcResult = getMockMvc().perform(MockMvcRequestBuilders.post("/role/query")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.header("X-Token", token)
				.content(JSON.toJSONString(queryUserRequest)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andDo(MockMvcResultHandlers.print())
				.andReturn();
		BaasResult<List<UserInfo>> baasResult = JSON.parseObject(mvcResult.getResponse().getContentAsString(),
				new TypeReference<>() {
				});
		List<UserInfo> userList = baasResult.getData();
		Assert.assertNotNull("testQueryUser error", userList);
	}

	@Override
	boolean needLogin() {
		return true;
	}
}
