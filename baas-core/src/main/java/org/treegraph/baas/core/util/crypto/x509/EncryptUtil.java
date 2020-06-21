package org.treegraph.baas.core.util.crypto.x509;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
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
			privateKey = EncryptUtil.loadECDSAPrivateKey(privateKeyStr);
		} else if(algo.equalsIgnoreCase(CryptoEnum.Algo.RSA.getCode())){
			privateKey = EncryptUtil.loadRSAPrivateKey(privateKeyStr);
		} else{
			throw new Exception("无此算法");
		}
		return privateKey;
	}
	
	/**
	 * 从字符串中加载公钥
	 */
	public static PublicKey loadPublicKey(String algo, String publicKeyStr) throws Exception {
		PublicKey publicKey = null;
		if(algo.equalsIgnoreCase(CryptoEnum.Algo.ECC.getCode()) ||
				algo.equalsIgnoreCase(CryptoEnum.Algo.SM2.getCode())){
			publicKey = EncryptUtil.loadECDSAPublicKey(publicKeyStr);
		} else if(algo.equalsIgnoreCase(CryptoEnum.Algo.RSA.getCode())){
			publicKey = EncryptUtil.loadRSAPublicKey(publicKeyStr);
		} else{
			throw new Exception("无此算法");
		}
		return publicKey;
	}

	/**
	 * 公钥加密/解密
	 */
	public static byte[] decodeByPublicKey(String algo, boolean encryptMode, PublicKey publicKey, byte[] data) throws Exception {
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
	public static byte[] decodeByPrivateKey(String algo, boolean encryptMode, PrivateKey privateKey, byte[] data)
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
	
	/**
	 * 从字符串中加载EC私钥
	 */
	private static ECPrivateKey loadECDSAPrivateKey(String privateKeyStr) throws Exception {
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
	 * 从字符串中加载RSA私钥
	 */
	private static RSAPrivateKey loadRSAPrivateKey(String privateKeyStr) throws Exception {
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
	 * 从字符串中加载EC公钥
	 */
	private static ECPublicKey loadECDSAPublicKey(String publicKeyStr) throws Exception {
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
	 * 从字符串中加载RSA公钥
	 */
	private static RSAPublicKey loadRSAPublicKey(String publicKeyStr) throws Exception {
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
}
