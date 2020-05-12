package org.treegraph.baas.core.util;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.common.hash.Hashing;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ApiAuthUtil {
    private static final Joiner.MapJoiner REQ_PARAMS_JOINER = Joiner.on('&').withKeyValueSeparator("=");

    private ApiAuthUtil() {
        // 工具类不允许实例化
    }

    public static String calcToken(String securityKey, Map<String, ?> params) {
    	
        checkArgument(!Strings.isNullOrEmpty(securityKey));
        checkNotNull(params);

        Map<String, String> sortedParams = Maps.newTreeMap();
        for (Map.Entry<String, ?> param : params.entrySet()) {
            Object valObj = param.getValue();
            if (valObj != null) {
                sortedParams.put(param.getKey(), String.valueOf(param.getValue()));
            }
        }
        
        String sortedParamsStr = REQ_PARAMS_JOINER.join(sortedParams);
        String rtn =  Hashing.md5().newHasher().putString(sortedParamsStr + securityKey, StandardCharsets.UTF_8).hash().toString();
        log.info("[calcToken] sortedParamsStr:{},securityKey:{},token:{}", sortedParamsStr, securityKey, rtn);
        
        return rtn;
    }
}
