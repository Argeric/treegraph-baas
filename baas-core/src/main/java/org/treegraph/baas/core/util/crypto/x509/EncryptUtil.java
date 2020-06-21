package org.treegraph.baas.core.util.crypto.x509;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.NullCipher;

import org.bouncycastle.util.encoders.Base64;
import org.treegraph.baas.core.enums.CryptoEnum;

public class EncryptUtil {

	/**
	 * 从文件中加载秘钥
	 */
	public static String loadKeyStrByFile(String pathName) throws Exception {
		try {
			BufferedReader br = new BufferedReader(new FileReader(pathName));
			String readLine = null;
			StringBuilder sb = new StringBuilder();
			while ((readLine = br.readLine()) != null) {
				sb.append(readLine);
			}
			br.close();
			return sb.toString();
		} catch (IOException e) {
			throw new Exception("秘钥数据流读取错误");
		} catch (NullPointerException e) {
			throw new Exception("秘钥输入流为空");
		}
	}
	
	/**
	 * 从字符串中加载私钥
	 */
	public static PrivateKey loadPrivateKey(String algo, String privateKeyStr) throws Exception {
		PrivateKey privateKey = null;
		if(algo.equalsIgnoreCase(CryptoEnum.Algo.ECC.getCode()) ||
				algo.equalsIgnoreCase(CryptoEnum.Algo.SM2.getCode())){
			privateKey = EncryptUtil.loadECPrivateKey(privateKeyStr);
		} else if(algo.equalsIgnoreCase(CryptoEnum.Algo.RSA.getCode())){
			privateKey = EncryptUtil.loadRSAPrivateKey(privateKeyStr);
		} else{
			throw new Exception("无此算法");
		}
		return privateKey;
	}

	/**
	 * 从字符串中加载EC公钥
	 */
	public static ECPublicKey loadECPublicKey(String publicKeyStr) throws Exception {
		try {
			byte[] buffer = Base64.decode(publicKeyStr);
			KeyFactory keyFactory = KeyFactory.getInstance("ECDSA");
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
			return (ECPublicKey) keyFactory.generatePublic(keySpec);
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("无此算法");
		} catch (InvalidKeySpecException e) {
			throw new Exception("公钥非法");
		} catch (NullPointerException e) {
			throw new Exception("公钥数据为空");
		}
	}

	/**
	 * 从字符串中加载EC私钥
	 */
	public static ECPrivateKey loadECPrivateKey(String privateKeyStr) throws Exception {
		try {
			byte[] buffer = Base64.decode(privateKeyStr);
			KeyFactory keyFactory = KeyFactory.getInstance("ECDSA");
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
			return (ECPrivateKey) keyFactory.generatePrivate(keySpec);
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("无此算法");
		} catch (InvalidKeySpecException e) {
			throw new Exception("私钥非法");
		} catch (NullPointerException e) {
			throw new Exception("私钥数据为空");
		}
	}

	/**
	 * 从字符串中加载RSA公钥
	 */
	public static RSAPublicKey loadRSAPublicKey(String publicKeyStr) throws Exception {
		try {
			byte[] buffer = Base64.decode(publicKeyStr);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
			return (RSAPublicKey) keyFactory.generatePublic(keySpec);
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("无此算法");
		} catch (InvalidKeySpecException e) {
			throw new Exception("公钥非法");
		} catch (NullPointerException e) {
			throw new Exception("公钥数据为空");
		}
	}

	/**
	 * 从字符串中加载RSA私钥
	 */
	public static RSAPrivateKey loadRSAPrivateKey(String privateKeyStr) throws Exception {
		try {
			byte[] buffer = Base64.decode(privateKeyStr);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
			return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("无此算法");
		} catch (InvalidKeySpecException e) {
			throw new Exception("私钥非法");
		} catch (NullPointerException e) {
			throw new Exception("私钥数据为空");
		}
	}

	/**
	 * EC公钥加密/解密
	 */
	public static byte[] decodeByECPublicKey(boolean encryptMode, ECPublicKey publicKey, byte[] data) throws Exception {
		Cipher cipher = null;
		try {
			cipher = new NullCipher();
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
	 * EC私钥加密/解密
	 */
	public static byte[] decodeByECPrivateKey(boolean encryptMode, ECPrivateKey privateKey, byte[] data)
			throws Exception {
		Cipher cipher = null;
		try {
			cipher = new NullCipher();
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

	/**
	 * RSA公钥加密/解密
	 */
	public static byte[] decodeByRSAPublicKey(boolean encryptMode, RSAPublicKey publicKey, byte[] data)
			throws Exception {
		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance("RSA");
			cipher.init(encryptMode ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE, publicKey);
			byte[] output = cipher.doFinal(data);
			return output;
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("无此算法");
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
			return null;
		} catch (InvalidKeyException e) {
			throw new Exception("公钥非法,请检查");
		} catch (IllegalBlockSizeException e) {
			throw new Exception("明文长度非法");
		} catch (BadPaddingException e) {
			throw new Exception("明文数据已损坏");
		}
	}

	/**
	 * RSA私钥加密/解密
	 */
	public static byte[] decodeByRSAPrivateKey(boolean encryptMode, RSAPrivateKey privateKey, byte[] data)
			throws Exception {
		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance("RSA");
			cipher.init(encryptMode ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE, privateKey);
			byte[] output = cipher.doFinal(data);
			return output;
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("无此算法");
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
			return null;
		} catch (InvalidKeyException e) {
			throw new Exception("加密私钥非法,请检查");
		} catch (IllegalBlockSizeException e) {
			throw new Exception("明文长度非法");
		} catch (BadPaddingException e) {
			throw new Exception("明文数据已损坏");
		}
	}
}
