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
import org.treegraph.baas.api.dto.RolePermissionInfo;
import org.treegraph.baas.api.dto.request.ListRoleRequest;
import org.treegraph.baas.api.dto.request.QueryRoleRequest;
import org.treegraph.baas.api.dto.response.ListRoleResponse;
import org.treegraph.baas.core.util.Order;

import java.util.List;

public class RoleTests extends AbstractBaasIntegrationTests {

	@Test
	@Order(2)
	public void testListRole() throws Exception {
		ListRoleRequest listRoleRequest = ListRoleRequest.builder()
				.current(0)
				.pageSize(10)
				.build();
		MvcResult mvcResult = getMockMvc().perform(MockMvcRequestBuilders.post("/role/list")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.header("X-Token", token)
				.content(JSON.toJSONString(listRoleRequest)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andDo(MockMvcResultHandlers.print())
				.andReturn();
		BaasResult<ListRoleResponse> baasResult = JSON.parseObject(mvcResult.getResponse().getContentAsString(),
				new TypeReference<>() {
				});
		ListRoleResponse listRoleResponse = baasResult.getData();
		Assert.assertNotNull("testListRole error", listRoleResponse);
	}

	@Test
	@Order(3)
	public void testQueryRole() throws Exception {
		QueryRoleRequest queryRoleRequest = QueryRoleRequest.builder()
				.id("93218b34-76a6-4221-92be-f6a9785ddb80")
				.consId("00be8983-f2ad-4c2e-baf9-5b9b96f13eb3")
				.build();
		MvcResult mvcResult = getMockMvc().perform(MockMvcRequestBuilders.post("/role/query")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.header("X-Token", token)
				.content(JSON.toJSONString(queryRoleRequest)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andDo(MockMvcResultHandlers.print())
				.andReturn();
		BaasResult<List<RolePermissionInfo>> baasResult = JSON.parseObject(mvcResult.getResponse().getContentAsString(),
				new TypeReference<>() {
				});
		List<RolePermissionInfo> rolePermList = baasResult.getData();
		Assert.assertNotNull("testQueryRole error", rolePermList);
	}


	@Override
	boolean needLogin() {
		return true;
	}
}
