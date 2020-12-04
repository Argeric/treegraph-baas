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
import org.treegraph.baas.api.dto.request.ListOperLogRequest;
import org.treegraph.baas.api.dto.response.ListOperLogResponse;
import org.treegraph.baas.core.util.Order;

public class SysTests extends AbstractBaasIntegrationTests {

	@Test
	@Order(2)
	public void testListOperLog() throws Exception {
		ListOperLogRequest listOperLogRequest = ListOperLogRequest.builder()
				.current(1)
				.pageSize(10)
				.build();
		MvcResult mvcResult = getMockMvc().perform(MockMvcRequestBuilders.post("/sys/log/list")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.header("X-Token", token)
				.content(JSON.toJSONString(listOperLogRequest)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andDo(MockMvcResultHandlers.print())
				.andReturn();
		BaasResult<ListOperLogResponse> baasResult = JSON.parseObject(mvcResult.getResponse().getContentAsString(),
				new TypeReference<BaasResult<ListOperLogResponse>>() {
				});
		ListOperLogResponse listOperLogResponse = baasResult.getData();
		Assert.assertNotNull("listOperLog error", listOperLogResponse);
	}


	@Override
	boolean needLogin() {
		return true;
	}
}
