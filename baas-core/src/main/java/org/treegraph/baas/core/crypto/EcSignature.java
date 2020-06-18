package org.treegraph.baas.core.crypto;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;

import org.bouncycastle.util.encoders.Hex;

/**
 * RSA签名验签类
 */
public class EcSignature {

	/**
	 * 签名算法
	 */
	public static final String SIGN_ALGORITHMS = "SHA1withECDSA";

	// 加签
	public static String signECDSA(PrivateKey privateKey, String message) {
		String result = "";
		try {
			// 执行签名
			Signature signature = Signature.getInstance(SIGN_ALGORITHMS);
			signature.initSign(privateKey);
			signature.update(message.getBytes());
			byte[] sign = signature.sign();
			return Hex.toHexString(sign);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	// 验签
	public static boolean verifyECDSA(PublicKey publicKey, String signed, String message) {
		try {
			// 验证签名
			Signature signature = Signature.getInstance(SIGN_ALGORITHMS);
			signature.initVerify(publicKey);
			signature.update(message.getBytes());

			byte[] hex = Hex.decode(signed);
			boolean bool = signature.verify(hex);
			System.out.println("验证：" + bool);
			return bool;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}