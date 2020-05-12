package org.treegraph.baas.core.util;  
 
import java.text.ParseException;
import java.util.Collection;
import java.util.Map;  
/**
* @Title: ValidateHelper.java 
* @Package com.mljr.common.utils 
* @ClassName: ValidateHelper 
* @Description: (常用的验证方法帮助类,提供对字符串，集合，数组，数值的验证 ) 
* @author ma wei long 
* @date 2018年3月9日 下午1:24:46 
* @version V1.0
 */
public class ValidateHelper {  
    /** 
     * 判断一个字符串是否不是一个空字符串 
     *  
     * @param s 要判断的字符串 
     * @return 如果不为空返回true，否则返回false 
     */ 
    public static boolean isNotEmpty(String s) {  
        return ((s != null) && s.length() > 0);  
    }  
 
    /** 
     * 判断一个字符串是否是一个空字符串 
     *  
     * @param s 要判断的字符串 
     * @return 如果为空返回true，否则返回false 
     */ 
    public static boolean isEmpty(String s) {  
        return ((s == null) || (s.length() == 0));  
    }  
 
    /** 
     * 判断一个Collection类型的集合是否不是一个空集合 
     *  
     * @param c 要判断集合 
     * @return 如果不为空返回true，否则返回false 
     */ 
    @SuppressWarnings("rawtypes")
	public static boolean isNotEmpty(Collection c) {  
        return ((c != null) && (c.size() > 0));  
    }  
 
    /** 
     * 判断一个Collection类型的集合是否是一个空集合 
     *  
     * @param c 要判断集合 
     * @return 如果为空返回true，否则返回false 
     */ 
    @SuppressWarnings("rawtypes")
	public static boolean isEmpty(Collection c) {  
        return ((c == null) || (c.size() == 0));  
    }  
    
    /** 
     * 判断一个Map类型的集合是否不是一个空集合 
     *  
     * @param c 要判断的集合 
     * @return 如果不为空返回true，否则返回false 
     */ 
    @SuppressWarnings("rawtypes")
	public static boolean isNotEmpty(Map m) {  
        return ((m != null) && (m.size() > 0));  
    }  
 
    /** 
     * 判断一个Map类型的集合是否是一个空集合 
     *  
     * @param c 要判断的集合 
     * @return 如果为空返回true，否则返回false 
     */ 
    @SuppressWarnings("rawtypes")
	public static boolean isEmpty(Map m) {  
        return ((m == null) || (m.size() == 0));  
    }  
 
    /** 
     * 判断一个int类型的数组是否不是一个空数组 
     *  
     * @param c 要判断的数组 
     * @return 如果不为空返回true，否则返回false 
     */ 
    public static boolean isNotEmpty(int[] i) {  
        return ((i != null) && (i.length > 0));  
    }  
 
    /** 
     * 判断一个int类型的数组是否是一个空数组 
     *  
     * @param c 要判断的数组 
     * @return 如果为空返回true，否则返回false 
     */ 
    public static boolean isEmpty(int[] i) {  
        return ((i == null) || (i.length == 0));  
    }  
 
    /** 
     * 判断一个String类型的数组是否不是一个空数组 
     *  
     * @param c 要判断的数组 
     * @return 如果不为空返回true，否则返回false 
     */ 
    public static boolean isNotEmpty(String[] s) {  
        return ((s != null) && (s.length > 0));  
    }  
 
    /** 
     * 判断一个String类型的数组是否是一个空数组 
     *  
     * @param c 要判断的数组 
     * @return 如果为空返回true，否则返回false 
     */ 
    public static boolean isEmpty(String[] s) {  
        return ((s == null) || (s.length == 0));  
    }  
 
    /** 
     * 验证一个字符串是否是Double类型 
     *  
     * @param s 要验证的字符串 
     * @return 如果是Double类型则返回true,否则返回false 
     */ 
    public static boolean isDouble(String s) {  
        if (s == null || s.equals(""))  
            return false;  
        String num = "0123456789.";  
        if (s.indexOf('.') >= 0)  
            if (s.indexOf('.', s.indexOf('.') + 1) > 0)  
                return false;  
        for (int i = 0; i < s.length(); i++) {  
            if (num.indexOf(s.charAt(i)) < 0) {  
                return false;  
            } else {  
                try {  
                    Double.parseDouble(s);  
                } catch (NumberFormatException e) {  
                    return false;  
                }  
            }  
        }  
        return true;  
    }  
 
    /** 
     * 验证一个字符串是否是Float类型 
     *  
     * @param s 要验证的字符串 
     * @return 如果是Float类型则返回true,否则返回false 
     */ 
    public static boolean isFloat(String s) {  
        if (s == null || s.equals(""))  
            return false;  
        String num = "0123456789.";  
        if (s.indexOf('.') >= 0)  
            if (s.indexOf('.', s.indexOf('.') + 1) > 0)  
                return false;  
        for (int i = 0; i < s.length(); i++) {  
            if (num.indexOf(s.charAt(i)) < 0) {  
                return false;  
            } else {  
                try {  
                    Float.parseFloat(s);  
                } catch (NumberFormatException e) {  
                    return false;  
                }  
            }  
        }  
        return true;  
    }  
 
    /** 
     * 验证一个字符串是否是整形 
     *  
     * @param s 要验证的字符串 
     * @return 如果是整形则返回true,否则返回false 
     */ 
    public static boolean isInteger(String s) {  
        if (s == null || s.length() == 0) {  
            return false;  
        } else {  
            String str = "0123456789";  
            String num = "-0123456789";  
            if (num.indexOf(s.charAt(0)) < 0)  
                return false;  
            for (int i = 1; i < s.length(); i++) {  
                if (str.indexOf(s.charAt(i)) < 0) {  
                    return false;  
                } else {  
                    try {  
                        Integer.parseInt(s);  
                    } catch (NumberFormatException e) {  
                        return false;  
                    }  
                }  
            }  
        }  
        return true;  
    }  
 
    /** 
     * 验证一个字符串是否是一个.和一组数字组成 
     *  
     * @param s 要传入的数字字符串 
     * @return 如果是一个长类型数字返回true,否则返回false 
     */ 
    public static boolean isLongNumber(String s) {  
        if (s == null || s.equals(""))  
            return false;  
        String num = "0123456789.";  
        if (s.indexOf('.') >= 0)  
            if (s.indexOf('.', s.indexOf('.') + 1) > 0)  
                return false;  
        for (int i = 0; i < s.length(); i++) {  
            if (num.indexOf(s.charAt(i)) < 0)  
                return false;  
        }  
        return true;  
    }  
 
    /** 
     * 验证一个字符串是否是数字组成 
     *  
     * @param s 要验证的字符串 
     * @return 如果字符串是数字组成的则返回true,否则返回false 
     */ 
    public static boolean isNumber(String s) {  
        if (s == null || s.equals(""))  
            return false;  
        String num = "0123456789";  
        for (int i = 0; i < s.length(); i++) {  
            if (num.indexOf(s.charAt(i)) < 0)  
                return false;  
        }  
        return true;  
    }  
 
    /** 
     * 验证一个字符串是否一个合法日期,日期格式：yyyy-MM-dd 
     *  
     * @param date 要验证的字符串日期 
     * @return 如果字符串是一个合法的日期返回true,否则返回false 
     */ 
    public static boolean isDate(String date) {  
        java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd");  
        try {  
            df.setLenient(false);  
            df.parse(date);  
            return true;  
        } catch (ParseException e) {  
            return false;  
        }  
    }  
 
    /** 
     * 验证一个字符串是否一个合法日期时间,日期时间格式：yyyy-MM-dd HH:mm:ss 
     *  
     * @param date 要验证的字符串日期时间 
     * @return 如果字符串是一个合法的日期时间返回true,否则返回false 
     */ 
    public static boolean isTimestamp(String time) {  
        java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
        try {  
            df.setLenient(false);  
            df.parse(time);  
            return true;  
        } catch (ParseException e) {  
            return false;  
        }  
    }  
 
    /** 
     * 根据字节数组指定的开始和结尾长度来计算字符串值 
     *  
     * @param bytes 字节数组 
     * @param begin 开始索引 
     * @param end 结束索引 
     * @return 转换后的字符串结果 
     */ 
    public static String getString(byte[] bytes, int begin, int end) throws RuntimeException {  
        byte[] newBytes = new byte[end - begin];  
        for (int i = begin, j = 0; i < end; i++, j++) {  
            byte c = bytes[i];  
            newBytes[j] = c;  
        }  
        return (new String(newBytes));  
    }  
 
    /** 
     * 根据字节数组指定的开始和结尾长度来计算字符串的字节长度 
     *  
     * @param bytes 字节数组 
     * @param begin 开始索引 
     * @param end 结束索引 
     * @return 转换后的字符串长度 
     */ 
    public static int getLength(byte[] bytes, int begin, int end) {  
        byte[] newBytes = new byte[end - begin];  
        try {  
            for (int i = begin, j = 0; i < end; i++, j++) {  
                byte b = bytes[i];  
                newBytes[j] = b;  
            }  
        } catch (RuntimeException ex) {  
            ex.printStackTrace();  
        }  
        return (newBytes.length);  
    }  
 
} 