package org.treegraph.baas.core.util.mpay;
import lombok.extern.slf4j.Slf4j;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author ZhaoPeng
 * @create 2017-07-27 10:35
 **/
@Slf4j
public class MpayTransUtil {

    /**
     * 实体转换map
     * @param obj
     * @return
     */
    public static Map<String, Object> beanToMap(Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
                // 过滤class属性
                if (!key.equals("class")) {
                    // 得到property对应的getter方法
                    Method getter = property.getReadMethod();
                    Object value = getter.invoke(obj);
                    if (null == value) {
                        continue;
                    }
                    map.put(key, value);
                }

            }
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("实体转换map异常",e);
            return null;
        }
    }
    
    /**
     * 实体转换map
     * @param obj
     * @return
     */
    public static Map<String, Object> beanToSortMap(Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            Map<String, Object> map = new TreeMap<String, Object>();
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
                // 过滤class属性
                if (!key.equals("class")) {
                    // 得到property对应的getter方法
                    Method getter = property.getReadMethod();
                    Object value = getter.invoke(obj);
                    if (null == value) {
                        continue;
                    }
                    map.put(key, value);
                }
            }
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("实体转换map异常",e);
            return null;
        }
    }

    /**
     * 实体转换map
     * @param obj
     * @return
     */
    public static Map<String, String> beanToMapAllString(Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            Map<String, String> map = new HashMap<String, String>();
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
                // 过滤class属性
                if (!key.equals("class")) {
                    // 得到property对应的getter方法
                    Method getter = property.getReadMethod();
                    String value = (String)getter.invoke(obj);
                    if (null == value) {
                        continue;
                    }
                    map.put(key, value);
                }
            }
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("实体转换map异常",e);
            return null;
        }
    }


}
