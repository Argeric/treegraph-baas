package org.treegraph.baas.core.util;

import java.util.Random;

/**
 * 生成验证码
 * Created by haojx on 2018/3/23.
 */
public class AuthCodeUtil {

    private static final String           DIGITAL_METAS = "0123456789";

    private AuthCodeUtil(){}

    public static String newAuthCode() {
        StringBuilder sb = new StringBuilder();
        sb.append(AuthCodeUtil.createRandomDigitalValue(8));
        return sb.toString();
    }
    public static String createRandomDigitalValue(int length) {
        char[] value = new char[length];
        int metaLength = AuthCodeUtil.DIGITAL_METAS.length();
        Random r = new Random();
        for (int i = 0; i < length; i++) {
            value[i] = AuthCodeUtil.DIGITAL_METAS.charAt(r.nextInt(metaLength));
        }
        return new String(value);
    }
}
