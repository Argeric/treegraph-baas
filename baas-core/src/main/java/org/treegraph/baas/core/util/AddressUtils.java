package org.treegraph.baas.core.util;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSONObject;

import lombok.extern.slf4j.Slf4j;
import okhttp3.FormBody;
import okhttp3.FormBody.Builder;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 通过ip获取真实地址
 */
@Slf4j
public class AddressUtils {

    public static String url = "http://ip.taobao.com/service/getIpInfo2.php";

    /**
     * 获取地址
     */
    public static String getAddressByIp(String ip) {

        String address = "未知地址";

        try {
            JSONObject returnStr = JSONObject.parseObject(call(ip));
            if (returnStr.getInteger("code") == 0) {
                JSONObject data = returnStr.getJSONObject("data");
                StringBuffer buffer = new StringBuffer();
                if (data != null) {
                    buffer.append(data.getString("region"));
                    buffer.append(data.getString("city"));
                    buffer.append(data.getString("county"));
                    buffer.append(data.getString("isp"));
                    return buffer.toString();
                }
            }
            return address;
        } catch (Exception e) {
            log.info("[getAddressByIp] ", ExceptionUtils.getStackTrace(e));
        }

        return address;
    }

    private static String call(String ip) throws Exception {
        log.info("ip:{}", ip);
        Response postResp = null;
        OkHttpClient client = new OkHttpClient();

        Builder build = new FormBody.Builder();
        build.add("ip", ip);
        FormBody body = build.build();
        Request request = new Request.Builder().url(url).post(body).build();
        postResp = client.newCall(request).execute();
        Assert.isTrue(postResp.code() == 200, "返回值不等于200");
        String respBody = postResp.body().string();
        log.info("调用淘宝查询ip地址接口返回值: " + respBody);
        return respBody;
    }
}
