package org.treegraph.baas.common.util;

public abstract interface Signature {
	public abstract byte[] getSignature(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2);

	public abstract boolean verify(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, String paramString);
}
