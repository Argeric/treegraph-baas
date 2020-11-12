package org.treegraph.baas.common.util;

import java.util.Map;
import java.util.Set;

public class PermHelper {

  private static boolean loginRequired = true;
  private static final ThreadLocal<Boolean> sysAdminFlagThreadLocal = new ThreadLocal<>();
  private static final ThreadLocal<Set<String>> consAdminSetThreadLocal = new ThreadLocal<>();
  private static final ThreadLocal<Set<String>> consIdSetThreadLocal = new ThreadLocal<>();
  private static final ThreadLocal<Map<String, Set<String>>> consPermMapThreadLocal = new ThreadLocal<>();
  private static final ThreadLocal<String> consIdsCondThreadLocal = new ThreadLocal<>();

  public static void init(boolean sysAdminFlag, Set<String> consAdminSet, Set<String> consIdSet,
      Map<String, Set<String>> consPermMap, String consIdsCond) {
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
    if (!loginRequired || sysAdminFlagThreadLocal.get() == null) {
      return Boolean.FALSE;
    }
    return sysAdminFlagThreadLocal.get();
  }

  public static Set<String> getConsAdminSet() {
    if (!loginRequired || consAdminSetThreadLocal.get() == null) {
      return null;
    }
    return consAdminSetThreadLocal.get();
  }

  public static Set<String> getConsIdSet() {
    if (!loginRequired || consIdSetThreadLocal.get() == null) {
      return null;
    }
    return consIdSetThreadLocal.get();
  }

  public static Map<String, Set<String>> getConsPermMap() {
    if (!loginRequired || consPermMapThreadLocal.get() == null) {
      return null;
    }
    return consPermMapThreadLocal.get();
  }

  public static String getConsIdsCond() {
    if (!loginRequired || consIdsCondThreadLocal.get() == null) {
      return null;
    }
    return consIdsCondThreadLocal.get();
  }

  public static void setLoginRequired(boolean loginRequired) {
    PermHelper.loginRequired = loginRequired;
  }

  public static boolean isLoginRequired() {
    return loginRequired;
  }
}
