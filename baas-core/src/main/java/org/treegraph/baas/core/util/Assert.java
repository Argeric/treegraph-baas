package org.treegraph.baas.core.util;

/**
 * @author: junqing.li@mljr.com
 * @date: 17/7/19
 */
public class Assert extends org.springframework.util.Assert {


  public static void tokenError() {

  }

  public static void notBlank(String data, String message) {
    if (StringUtils.isBlank(data)) {
      throw new IllegalArgumentException(message);
    }
  }
}
