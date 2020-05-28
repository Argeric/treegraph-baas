package org.treegraph.baas.common.util;

import java.util.Map;
import java.util.Set;

public class PermManager {
	
	private static boolean loginRequrired = true;
	private static final ThreadLocal<Boolean> sysAdminFlagThreadLocal = new ThreadLocal<>();
	private static final ThreadLocal<Set<String>> consAdminSetThreadLocal = new ThreadLocal<>();
	private static final ThreadLocal<Set<String>> consIdSetThreadLocal = new ThreadLocal<>();
	private static final ThreadLocal<Map<String, Set<String>>> consPermMapThreadLocal = new ThreadLocal<>();
	private static final ThreadLocal<String> consIdsCondThreadLocal = new ThreadLocal<>();
	
	
	public static void init(boolean sysAdminFlag, Set<String> consAdminSet, Set<String> consIdSet, Map<String, Set<String>> consPermMap, String consIdsCond) {
		sysAdminFlagThreadLocal.set(sysAdminFlag);
		consAdminSetThreadLocal.set(consAdminSet);
		consIdSetThreadLocal.set(consIdSet);
		consPermMapThreadLocal.set(consPermMap);
		consIdsCondThreadLocal.set(consIdsCond);
    }

    public static void destroy() {
    	sysAdminFlagThreadLocal.remove();
    	consAdminSetThreadLocal.remove();
    	consIdSetThreadLocal.remove();
    	consPermMapThreadLocal.remove();
    	consIdsCondThreadLocal.remove();
    }

    public static boolean getSysAdminFlag() {
        if (!loginRequrired || sysAdminFlagThreadLocal.get() == null) {
            return Boolean.FALSE;
        }
        return sysAdminFlagThreadLocal.get();
    }
    
    public static Set<String> getConsAdminSet() {
        if (!loginRequrired || consAdminSetThreadLocal.get() == null) {
            return null;
        }
        return consAdminSetThreadLocal.get();
    }
    
    public static Set<String> getConsIdSet() {
        if (!loginRequrired || consIdSetThreadLocal.get() == null) {
            return null;
        }
        return consIdSetThreadLocal.get();
    }
    
    public static Map<String, Set<String>> getConsPermMap() {
        if (!loginRequrired || consPermMapThreadLocal.get() == null) {
            return null;
        }
        return consPermMapThreadLocal.get();
    }
    
    public static String getConsIdsCond() {
        if (!loginRequrired || consIdsCondThreadLocal.get() == null) {
            return null;
        }
        return consIdsCondThreadLocal.get();
    }
    
    public static void setLoginRequrired(boolean loginRequrired) {
        PermManager.loginRequrired = loginRequrired;
    }

    public static boolean isLoginRequrired() {
        return loginRequrired;
    }
}