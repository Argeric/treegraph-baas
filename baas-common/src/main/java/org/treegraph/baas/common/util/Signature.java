package org.treegraph.baas.common.util;

public interface Signature {

  byte[] getSignature(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2);
  //boolean verify(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, String paramString);
}
