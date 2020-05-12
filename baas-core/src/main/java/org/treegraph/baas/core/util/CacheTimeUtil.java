package org.treegraph.baas.core.util;

import java.util.Calendar;

/**
 * 
 */
public class CacheTimeUtil {

  public static final int SECOND = 1;
  // min
  public static final int MINUTE = 60 * SECOND;

  // day
  public static final int HOUR = 60 * MINUTE;

  // day
  public static final int DAY = 24 * HOUR;

  // 7day
  public static final int WEEK = 7 * DAY;

  // 30day
  public static final int MONTH = 30 * DAY;

  /**
   * 获取当前时间到当天二十四点之间的剩余秒数
   * 
   * @return
   */
  public static int getSecondToMorning() {
    Calendar ca = Calendar.getInstance();
    ca.set(Calendar.HOUR_OF_DAY, 24);
    ca.set(Calendar.MINUTE, 0);
    ca.set(Calendar.SECOND, 0);
    ca.set(Calendar.MILLISECOND, 0);
    long time = ca.getTimeInMillis() - System.currentTimeMillis();
    int second = (int) time / 1000;

    // java client的实现源码里是这样实现的 expiry.getTime() /
    // 1000，也就是说，如果小于1000的值，除以1000以后都是0，即永不过期
    return Math.max(1, second);
  }
}
