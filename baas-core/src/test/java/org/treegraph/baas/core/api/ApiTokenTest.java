/**
 * FileName: ApiTokenTest
 * Author:   hancy
 * Date:     2018/4/17 15:58
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package org.treegraph.baas.core.api;

import java.util.HashMap;
import java.util.Map;

import org.treegraph.baas.core.util.ApiAuthUtil;

public class ApiTokenTest {
    public static void main(String[] args) {
//        Map<String, String> map = new HashMap<>();
//        map.put("name","林俊生");
//        map.put("idCardNo","441522199009023073");
//        map.put("repaymentPhone","13141369996");
//        map.put("reqTime","2018-4-17 16:17:41");
//        String value = ApiAuthUtil.calcToken("sahdkh2813sahdkDW",map);
//        System.out.println(value);



        Map<String, String> map = new HashMap<>();
        map.put("name","苏康泽");
        map.put("idCardNo","14263119960206701X");
        map.put("repaymentPhone","13141369996");
        map.put("reqTime","2018-4-18 19:39:41");
        String value = ApiAuthUtil.calcToken("DHSJ217321hJDKSH213dasdsEWehaskd1341",map);
        System.out.println(value);
    }

}