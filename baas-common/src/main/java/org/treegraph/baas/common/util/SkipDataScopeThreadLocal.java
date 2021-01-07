package org.treegraph.baas.common.util;

public class SkipDataScopeThreadLocal {

  private SkipDataScopeThreadLocal() {
  }

  private static final ThreadLocal<Integer> SKIP_DATA_SCOPE_THREAD_LOCAL = new ThreadLocal<>();

  public static void set(Integer isSkip) {
    SKIP_DATA_SCOPE_THREAD_LOCAL.set(isSkip);
  }

  public static Integer get() {
    return SKIP_DATA_SCOPE_THREAD_LOCAL.get();
  }

  public static void remove() {
    SKIP_DATA_SCOPE_THREAD_LOCAL.remove();
  }
}