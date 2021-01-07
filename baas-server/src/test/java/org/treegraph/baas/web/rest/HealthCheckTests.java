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
import org.treegraph.baas.core.util.Order;

public class HealthCheckTests extends AbstractBaasIntegrationTests {

  @Test
  @Order(1)
  public void testHealthCheck() throws Exception {
    MvcResult mvcResult = getMockMvc().perform(MockMvcRequestBuilders.get("/health")
            .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andDo(MockMvcResultHandlers.print())
            .andReturn();
    BaasResult<Void> baasResult = JSON.parseObject(mvcResult.getResponse().getContentAsString(),
            new TypeReference<BaasResult<Void>>() {
            });
    int status = baasResult.getStatus();
    Assert.assertEquals("health error!", 0, status);
  }

  @Override
  boolean needLogin() {
    return false;
  }
}
