package org.treegraph.baas.core.util.crypto.x509;

import java.security.InvalidKeyException;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NullCipher;

import org.treegraph.baas.core.enums.CryptoEnum;

public class EncryptUtil {

	/**
	 * 公钥加密/解密
	 */
	public static byte[] codecByPublicKey(String algo, boolean encryptMode, PublicKey publicKey, byte[] data) throws Exception {
		Cipher cipher = null;
		try {
			if(algo.equalsIgnoreCase(CryptoEnum.Algo.ECC.getCode()) ||
					algo.equalsIgnoreCase(CryptoEnum.Algo.SM2.getCode())){
				cipher = new NullCipher();
			} else if(algo.equalsIgnoreCase(CryptoEnum.Algo.RSA.getCode())){
				cipher = Cipher.getInstance("RSA");
			} else{
				throw new Exception("无此算法");
			}
			cipher.init(encryptMode ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE, publicKey);
			byte[] output = cipher.doFinal(data);
			return output;
		} catch (InvalidKeyException e) {
			throw new Exception("公钥非法,请检查");
		} catch (IllegalBlockSizeException e) {
			throw new Exception("明文长度非法");
		} catch (BadPaddingException e) {
			throw new Exception("明文数据已损坏");
		}
	}
	
	/**
	 * 私钥加密/解密
	 */
	public static byte[] codecByPrivateKey(String algo, boolean encryptMode, PrivateKey privateKey, byte[] data)
			throws Exception {
		Cipher cipher = null;
		try {
			if(algo.equalsIgnoreCase(CryptoEnum.Algo.ECC.getCode()) ||
					algo.equalsIgnoreCase(CryptoEnum.Algo.SM2.getCode())){
				cipher = new NullCipher();
			} else if(algo.equalsIgnoreCase(CryptoEnum.Algo.RSA.getCode())){
				cipher = Cipher.getInstance("RSA");
			} else{
				throw new Exception("无此算法");
			}
			cipher.init(encryptMode ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE, privateKey);
			byte[] output = cipher.doFinal(data);
			return output;
		} catch (InvalidKeyException e) {
			throw new Exception("加密私钥非法,请检查");
		} catch (IllegalBlockSizeException e) {
			throw new Exception("明文长度非法");
		} catch (BadPaddingException e) {
			throw new Exception("明文数据已损坏");
		}
	}
}
