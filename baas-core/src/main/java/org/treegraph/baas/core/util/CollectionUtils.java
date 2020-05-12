package org.treegraph.baas.core.util;

import java.util.Collection;
import java.util.Objects;

/**
 * Created by junqing.li on 17/4/17.
 */
public class CollectionUtils extends org.springframework.util.CollectionUtils {

  public static final byte[] EMPTY_ARRAY = new byte[0];


  /**
   * 数组为空
   * 
   * @param array
   * @return
   */
  public static boolean isEmpty(Object[] array) {
    return (Objects.isNull(array) || array.length == 0);
  }

  public static boolean isNotEmpty(Collection<?> collection) {
    return (collection != null && collection.size()>0);
  }


  public static boolean isEmpty(byte[] data) {
    return (data == null || data.length == 0);
  }


}
