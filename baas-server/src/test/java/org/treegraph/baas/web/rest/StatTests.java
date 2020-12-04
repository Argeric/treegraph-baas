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
import org.treegraph.baas.api.dto.response.QueryDashboardResponse;
import org.treegraph.baas.core.util.Order;

public class StatTests extends AbstractBaasIntegrationTests {

	@Test
	@Order(1)
	public void testQueryDashboardInfo() throws Exception {
		MvcResult mvcResult = getMockMvc().perform(MockMvcRequestBuilders.post("/stat/dashboard")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.header("X-Token", token))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andDo(MockMvcResultHandlers.print())
				.andReturn();
		BaasResult<QueryDashboardResponse> baasResult = JSON.parseObject(mvcResult.getResponse().getContentAsString(),
				new TypeReference<BaasResult<QueryDashboardResponse>>() {
				});
		QueryDashboardResponse data = baasResult.getData();
		Assert.assertNotNull("queryDashboardInfo error", data);
	}

	@Override
	boolean needLogin() {
		return true;
	}
}
