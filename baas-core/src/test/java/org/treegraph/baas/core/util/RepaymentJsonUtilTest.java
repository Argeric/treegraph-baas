package org.treegraph.baas.core.util;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.Date;

import org.junit.Test;
import org.treegraph.baas.core.entity.bo.ClientBo;

public class RepaymentJsonUtilTest {
    @Test
    public void testSerializeToJsonStr() {
        ClientBo clientBo = new ClientBo();
        clientBo.setId(20L);
        clientBo.setIdCardName("张三");
        clientBo.setToken("adsajhd");
        clientBo.setValidDateEnd(new Date(1544011950000L));
        String jsonStr = RepaymentJsonUtil.serializeToJsonStr(clientBo);
        assertThat(jsonStr, equalTo("{\"id\":20,\"idCardName\":\"张三\",\"idCardNo\":null,\"token\":\"adsajhd\",\"tokenUpdateTime\":null,\"authCode\":null,\"validDateEnd\":1544011950000}"));
    }

    @Test
    public void testDeserializeToObj() {
        String jsonStr = "{\"id\":20,\"idCardName\":\"张三\",\"idCardNo\":null,\"token\":\"adsajhd\",\"tokenUpdateTime\":null,\"authCode\":null,\"validDateEnd\":1544011950000}";
        ClientBo actualClientBo = RepaymentJsonUtil.deserializeToObj(jsonStr, ClientBo.class);
        ClientBo expectedClientBo = new ClientBo();
        expectedClientBo.setId(20L);
        expectedClientBo.setIdCardName("张三");
        expectedClientBo.setToken("adsajhd");
        expectedClientBo.setValidDateEnd(new Date(1544011950000L));
        assertThat(actualClientBo, equalTo(expectedClientBo));
    }
}