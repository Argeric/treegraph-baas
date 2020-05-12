package org.treegraph.baas.core.util.mpay;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MpayDataSiginUtil {

	private static final String DATA_KEY = "123456!@#$";
	
	@SuppressWarnings("rawtypes")
	public static String createSign(String characterEncoding, Map<String, Object> parameters, String key){
		StringBuffer sb = new StringBuffer();
        Set es = parameters.entrySet();
        Iterator it = es.iterator();
        while(it.hasNext()) {
            Map.Entry entry = (Map.Entry)it.next();
            String k = (String)entry.getKey();
            Object v = entry.getValue();
            if(null != v && !"".equals(v) 
                    && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("key=" + key);
        String sign = MpayMD5Util.MD5Encode(sb.toString(), characterEncoding).toUpperCase();
        log.info("[createSign] signPlainText:{},sign:{}", sb.toString(), sign);
        return sign;
    }
	
	@SuppressWarnings("rawtypes")
	public static String createSign(Map<String, Object> parameters){
		String characterEncoding = "UTF-8";
		StringBuffer sb = new StringBuffer();
        Set es = parameters.entrySet();
        Iterator it = es.iterator();
        while(it.hasNext()) {
            Map.Entry entry = (Map.Entry)it.next();
            String k = (String)entry.getKey();
            Object v = entry.getValue();
            if(null != v && !"".equals(v) 
                    && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("key=" + DATA_KEY);
        String sign = MpayMD5Util.MD5Encode(sb.toString(), characterEncoding).toUpperCase();
		log.info("[createSign1] signPlainText:{},sign:{}", sb.toString(), sign);
        return sign;
    }
	
	public static Boolean verifySign(Object obj, String sign) {
		Map<String, Object> map = MpayTransUtil.beanToSortMap(obj);
		String mySign = createSign(map);
		return sign.equals(mySign);
	}
}
