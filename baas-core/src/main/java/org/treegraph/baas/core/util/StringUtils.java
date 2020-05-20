package org.treegraph.baas.core.util;

import java.text.MessageFormat;

import org.slf4j.helpers.MessageFormatter;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by junqing.li on 17/4/17.
 */
@Slf4j
public class StringUtils extends org.apache.commons.lang3.StringUtils {

  public static final char UNDERLINE = '_';

  /**
   * 驼峰转下划线
   * 
   * @param param
   * @return
   */
  public static String camelToUnderline(String param) {

    if (isBlank(param)) {
      return EMPTY;
    }
    int len = param.length();
    StringBuilder sb = new StringBuilder(len);
    for (int i = 0; i < len; i++) {
      char c = param.charAt(i);
      if (Character.isUpperCase(c)) {
        sb.append(UNDERLINE);
        sb.append(Character.toLowerCase(c));
      } else {
        sb.append(c);
      }
    }
    return sb.toString();
  }

  public static void main(String[] args) {

    String arys = MessageFormat.format("xxx{0}, sddsfsf{1}", 1233, "xxx");
    log.info(arys);

    arys = MessageFormatter.format("xxx{}, sddsfsf{}", 1233, "xxx").getMessage();

    log.info(arys);

  }
}

