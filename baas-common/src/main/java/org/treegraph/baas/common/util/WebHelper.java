package org.treegraph.baas.common.util;


import java.util.HashMap;
import java.util.Map;

public class WebHelper {

	private static final Long defaultUserId = 0L;
	private static final String UID = "uid";
	private static final String ACCOUNT = "account";
	private static final String NAME = "name";
	private static boolean loginRequrired = true;
	private static final ThreadLocal<Map<String, Object>> loginContext = new ThreadLocal<Map<String, Object>>();
	
	public static void init(String userId, String account, String name) {
        if (loginContext.get() == null) {
			loginContext.set(getMap(UID, userId, ACCOUNT, account, NAME, name));
        } else {
        	loginContext.get().put(UID, userId);
        	loginContext.get().put(ACCOUNT, account);
            loginContext.get().put(NAME, name);
        }
    }

    public static void destroy() {
        loginContext.remove();
    }

    public static Long getUserId() {
        if (!loginRequrired) {
            return defaultUserId;
        }
        if (loginContext.get() == null) {
            return null;
        }
        return (Long) loginContext.get().get(UID);
    }
    
    public static String getUserAccount() {
        if (!loginRequrired || loginContext.get() == null) {
            return null;
        }
        return (String) loginContext.get().get(ACCOUNT);
    }
    
    public static String getUserName() {
        if (!loginRequrired || loginContext.get() == null) {
            return null;
        }
        return (String) loginContext.get().get(NAME);
    }

    public static void setLoginRequrired(boolean loginRequrired) {
        WebHelper.loginRequrired = loginRequrired;
    }

    public static boolean isLoginRequrired() {
        return loginRequrired;
    }
    
	private static Map<String, Object> getMap(String k1, Object o1, String k2, Object o2, String k3,
			Object o3) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(k1, o1);
        map.put(k2, o2);
        map.put(k3, o3);
        return map;
    }
}