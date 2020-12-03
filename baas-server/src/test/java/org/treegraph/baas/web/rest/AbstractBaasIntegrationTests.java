package org.treegraph.baas.web.rest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.treegraph.baas.OrderedRunner;
import org.treegraph.baas.TreeGraphBaasApp;
import org.treegraph.baas.api.dto.BaasResult;
import org.treegraph.baas.api.dto.request.LoginRequest;
import org.treegraph.baas.api.dto.response.LoginResponse;

@RunWith(OrderedRunner.class)
@SpringBootTest(classes = TreeGraphBaasApp.class)
@Slf4j
abstract public class AbstractBaasIntegrationTests {

  private static final boolean initialized = false;

  private static MockMvc mockMvc;

  private static final String account = "sys-admin";

  private static final String password = "123456";

  @Autowired
  private WebApplicationContext context;

  String token;

  @Before
  public void setup() throws Exception {
    if(!initialized) {
      mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
      if(needLogin()){
        testLogin();
      }
    }
  }

  abstract boolean needLogin();

  private void testLogin() throws Exception {
    LoginRequest loginRequest = LoginRequest.builder()
            .account(account)
            .password(password).build();
    MvcResult mvcResult = getMockMvc().perform(MockMvcRequestBuilders.post("/sys/login")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .content(JSON.toJSONString(loginRequest)))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andDo(MockMvcResultHandlers.print())
            .andReturn();
    BaasResult<LoginResponse> baasResult = JSON.parseObject(mvcResult.getResponse().getContentAsString(),
            new TypeReference<>() {});
    token = baasResult.getData().getToken();
    Assert.assertNotNull("testLogin error", token);
  }

  MockMvc getMockMvc(){
    return  mockMvc;
  }
}
