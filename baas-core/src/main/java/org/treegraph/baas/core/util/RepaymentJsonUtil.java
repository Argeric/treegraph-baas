package org.treegraph.baas.core.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RepaymentJsonUtil {
    private RepaymentJsonUtil() {
        // 工具类不允许实例化
    }

    public static String serializeToJsonStr(Object obj) {
        try {
             return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            log.error("对象序列化为json字符串异常, object: {}, exception: {}", obj, e);
            return "";
        }
    }

    public static <T> T deserializeToObj(String jsonStr, Class<T> clazz) {
        try {
            return new ObjectMapper()
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                    .readValue(jsonStr, clazz);
        } catch (Exception e) {
            log.error("json字符串反序列化为Java对象异常, jsonStr: {}, exception: {}", jsonStr, e);
            return null;
        }
    }
}
