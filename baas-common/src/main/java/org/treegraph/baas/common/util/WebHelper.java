package org.treegraph.baas.common.util;


import java.util.HashMap;
import java.util.Map;

public class WebHelper {

	private static final String defaultUserId = "0";
	private static final String UID = "uid";
	private static final String ACCOUNT = "account";
	private static final String NAME = "name";
	private static final String IP = "ip";
	private static final String DEVICE = "device";
	private static boolean loginRequrired = true;
	private static final ThreadLocal<Map<String, Object>> loginContext = new ThreadLocal<Map<String, Object>>();
	
	public static void init(String userId, String account, String name, String ip, String device) {
        if (loginContext.get() == null) {
			loginContext.set(getMap(UID, userId, ACCOUNT, account, NAME, name, IP, ip, DEVICE, device));
        } else {
        	loginContext.get().put(UID, userId);
        	loginContext.get().put(ACCOUNT, account);
            loginContext.get().put(NAME, name);
            loginContext.get().put(IP, ip);
            loginContext.get().put(DEVICE, device);
        }
    }

    public static void destroy() {
        loginContext.remove();
    }

    public static String getUserId() {
        if (!loginRequrired) {
            return defaultUserId;
        }
        if (loginContext.get() == null) {
            return null;
        }
        return (String) loginContext.get().get(UID);
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
    
    public static String getUserIp() {
        if (!loginRequrired || loginContext.get() == null) {
            return null;
        }
        return (String) loginContext.get().get(IP);
    }
    
    public static String getUserDevice() {
        if (!loginRequrired || loginContext.get() == null) {
            return null;
        }
        return (String) loginContext.get().get(DEVICE);
    }

    public static void setLoginRequrired(boolean loginRequrired) {
        WebHelper.loginRequrired = loginRequrired;
    }

    public static boolean isLoginRequrired() {
        return loginRequrired;
    }
    
	private static Map<String, Object> getMap(String k1, Object o1, String k2, Object o2, String k3,
			Object o3, String k4, Object o4, String k5, Object o5) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(k1, o1);
        map.put(k2, o2);
        map.put(k3, o3);
        map.put(k4, o4);
        map.put(k5, o5);
        return map;
    }
}