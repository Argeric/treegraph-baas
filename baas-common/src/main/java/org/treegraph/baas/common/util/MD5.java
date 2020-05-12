package org.treegraph.baas.common.util;

import java.security.MessageDigest;

public class MD5
  implements Signature
{
  private static final String[] strDigits = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };
  
  @SuppressWarnings("unused")
  private static final String ALGORITHM = "MD5";
  
  private static String byteToArrayString(byte bByte)
  {
    return strDigits[(bByte >> 4 & 0xF)] + strDigits[(bByte & 0xF)];
  }
  
  private static String byteToString(byte[] bByte)
  {
    StringBuffer sBuffer = new StringBuffer();
    for (int i = 0; i < bByte.length; i++) {
      sBuffer.append(byteToArrayString(bByte[i]));
    }
    return sBuffer.toString();
  }
  
  public static String encode(String strObj)
  {
    String resultString = null;
    try
    {
      resultString = new String(strObj);
      MessageDigest md = MessageDigest.getInstance("MD5");
      resultString = byteToString(md.digest(strObj.getBytes("UTF-8")));
    }
    catch (Exception ex)
    {
      throw new RuntimeException(ex);
    }
    return resultString;
  }
  
  public byte[] getSignature(byte[] data, byte[] key)
  {
    try
    {
      MessageDigest md = MessageDigest.getInstance("MD5");
      return md.digest(data);
    }
    catch (Exception e)
    {
      throw new SignatureException(e);
    }
  }
  
  public boolean verify(byte[] data, byte[] key, String sign)
  {
    byte[] expectSign = getSignature(data, key);
    return sign.equals(new String(expectSign));
  }
}
