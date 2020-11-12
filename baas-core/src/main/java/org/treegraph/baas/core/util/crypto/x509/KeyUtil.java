package org.treegraph.baas.core.util.crypto.x509;

import org.bouncycastle.asn1.gm.GMObjectIdentifiers;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.asn1.x9.X9ObjectIdentifiers;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.util.encoders.Base64;
import org.treegraph.baas.core.enums.CryptoEnum;
import org.web3j.crypto.ECKeyPair;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.security.*;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class KeyUtil {

  public static final String BC = BouncyCastleProvider.PROVIDER_NAME;

  static {
    Security.addProvider(new BouncyCastleProvider());
  }

  public static KeyPair generateKeyPair(String algo)
      throws NoSuchProviderException, NoSuchAlgorithmException, InvalidAlgorithmParameterException {
    KeyPairGenerator keyPairGenerator;
    SecureRandom random = new SecureRandom();
    if ("RSA".equalsIgnoreCase(algo)) {
      keyPairGenerator = KeyPairGenerator.getInstance("RSA", BC);
      keyPairGenerator.initialize(2048, random);
    } else if ("SM2".equalsIgnoreCase(algo)) {
      ECParameterSpec ecSpec = ECNamedCurveTable.getParameterSpec("sm2p256v1");
      keyPairGenerator = KeyPairGenerator.getInstance("ECDSA", BC);
      keyPairGenerator.initialize(ecSpec, random);
    } else if ("ECC".equalsIgnoreCase(algo)) {
			ECParameterSpec ecSpec = ECNamedCurveTable.getParameterSpec("secp256k1");
			keyPairGenerator = KeyPairGenerator.getInstance("ECDSA", BC);
			keyPairGenerator.initialize(ecSpec, random);
		} else {
			throw new IllegalArgumentException("算法不支持");
		}
    return keyPairGenerator.generateKeyPair();
  }

  public static PublicKey getPublicKey(SubjectPublicKeyInfo subjectPublicKeyInfo) throws Exception {
    BouncyCastleProvider bouncyCastleProvider = ((BouncyCastleProvider) Security.getProvider(BC));
    bouncyCastleProvider.addKeyInfoConverter(PKCSObjectIdentifiers.rsaEncryption,
        new org.bouncycastle.jcajce.provider.asymmetric.rsa.KeyFactorySpi());
    bouncyCastleProvider.addKeyInfoConverter(X9ObjectIdentifiers.id_ecPublicKey,
        new org.bouncycastle.jcajce.provider.asymmetric.ec.KeyFactorySpi.EC());
    return BouncyCastleProvider.getPublicKey(subjectPublicKeyInfo);
  }

  public static String privateKeyToBase64(PrivateKey privateKey) {
    return Base64.toBase64String(privateKey.getEncoded());
  }

  public static PrivateKey privateKeyFromBase64(String algo, String privateKeyStr)
      throws Exception {
    PrivateKey privateKey;
    if (algo.equalsIgnoreCase(CryptoEnum.Algo.ECC.getCode()) ||
        algo.equalsIgnoreCase(CryptoEnum.Algo.SM2.getCode())) {
      privateKey = loadECDSAPrivateKey(privateKeyStr);
    } else if (algo.equalsIgnoreCase(CryptoEnum.Algo.RSA.getCode())) {
      privateKey = loadRSAPrivateKey(privateKeyStr);
    } else {
      throw new Exception("无此算法");
    }
    return privateKey;
  }

  public static String publicKeyToBase64(PublicKey publicKey) {
    SubjectPublicKeyInfo subjectPublicKeyInfo = SubjectPublicKeyInfo
        .getInstance(publicKey.getEncoded());
    AlgorithmIdentifier signAlgo = CertUtil.getSignAlgo(subjectPublicKeyInfo.getAlgorithm());
    if (signAlgo.getAlgorithm().equals(GMObjectIdentifiers.sm2sign_with_sm3) ||
        signAlgo.getAlgorithm().equals(X9ObjectIdentifiers.ecdsa_with_SHA256)) {
      ECPublicKey pubKey = (ECPublicKey) publicKey;
      return Base64.toBase64String(pubKey.getEncoded());
    } else if (signAlgo.getAlgorithm().equals(PKCSObjectIdentifiers.sha256WithRSAEncryption)) {
      RSAPublicKey pubKey = (RSAPublicKey) publicKey;
      return Base64.toBase64String(pubKey.getEncoded());
    } else {
      throw new IllegalArgumentException("密钥算法不支持");
    }
  }

//	public static PublicKey publicKeyFromBase64(String algo, String publicKeyStr) throws Exception {
//		PublicKey publicKey = null;
//		if(algo.equalsIgnoreCase(CryptoEnum.Algo.ECC.getCode()) ||
//				algo.equalsIgnoreCase(CryptoEnum.Algo.SM2.getCode())){
//			publicKey = loadECDSAPublicKey(publicKeyStr);
//		} else if(algo.equalsIgnoreCase(CryptoEnum.Algo.RSA.getCode())){
//			publicKey = loadRSAPublicKey(publicKeyStr);
//		} else{
//			throw new Exception("无此算法");
//		}
//		return publicKey;
//	}

  public static String privateKeyToPem(PrivateKey key) throws IOException {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    JcaPEMWriter pemWriter = new JcaPEMWriter(new OutputStreamWriter(bos));
    pemWriter.writeObject(key);
    pemWriter.close();
    return new String(bos.toByteArray());
  }

//	public static PrivateKey privateKeyFromPem(String pemStr) throws IOException {
//		StringReader reader = new StringReader(pemStr);
//		PEMParser pemParser = new PEMParser(reader);
//		Object obj = pemParser.readObject();
//		KeyPair kp = new JcaPEMKeyConverter().setProvider("BC").getKeyPair((PEMKeyPair)obj);
//		pemParser.close();
//		return kp.getPrivate();
//	}

//	public static String publicKeyToPem(PublicKey key) throws IOException {
//		ByteArrayOutputStream bos = new ByteArrayOutputStream();
//		JcaPEMWriter pemWriter = new JcaPEMWriter(new OutputStreamWriter(bos));
//		pemWriter.writeObject(key);
//		pemWriter.close();
//		return new String(bos.toByteArray());
//	}

//	public static PublicKey publicKeyFromPem(String pemStr) throws IOException {
//		StringReader reader = new StringReader(pemStr);
//		PEMParser pemParser = new PEMParser(reader);
//		Object obj = pemParser.readObject();
//		KeyPair kp = new JcaPEMKeyConverter().setProvider("BC").getKeyPair((PEMKeyPair)obj);
//		pemParser.close();
//		return kp.getPublic();
//	}

//	/**
//	 * 从文件中加载秘钥
//	 */
//	public static String keyStrFromFile(String pathName) throws Exception {
//		try {
//			BufferedReader br = new BufferedReader(new FileReader(pathName));
//			String readLine = null;
//			StringBuilder sb = new StringBuilder();
//			while ((readLine = br.readLine()) != null) {
//				sb.append(readLine);
//			}
//			br.close();
//			return sb.toString();
//		} catch (IOException e) {
//			throw new Exception("秘钥数据流读取错误");
//		} catch (NullPointerException e) {
//			throw new Exception("秘钥输入流为空");
//		}
//	}

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

//	/**
//	 * 从字符串中加载RSA公钥
//	 */
//	private static RSAPublicKey loadRSAPublicKey(String publicKeyStr) throws Exception {
//		try {
//			byte[] buffer = Base64.decode(publicKeyStr);
//			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
//			return (RSAPublicKey) keyFactory.generatePublic(keySpec);
//		} catch (NoSuchAlgorithmException e) {
//			throw new Exception("无此算法");
//		} catch (InvalidKeySpecException e) {
//			throw new Exception("公钥非法");
//		} catch (NullPointerException e) {
//			throw new Exception("公钥数据为空");
//		}
//	}

  public static ECKeyPair loadECKeyPairFromBase64(String publicBase64, String privateBase64,
      String algo) {
    if (!"ECC".equalsIgnoreCase(algo)) {
      throw new IllegalArgumentException("算法不支持");
    }
    try {
      ECPublicKey publicKey = loadECDSAPublicKey(publicBase64);
      ECPrivateKey privateKey = loadECDSAPrivateKey(privateBase64);
      KeyPair keyPair = new KeyPair(publicKey, privateKey);
      return ECKeyPair.create(keyPair);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
