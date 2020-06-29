package org.treegraph.baas.core.crypto.x509;
import java.security.KeyPair;
import java.util.Date;

import org.bouncycastle.asn1.gm.GMObjectIdentifiers;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
/**    Created by IntelliJ IDEA.
    Author: Yinyx
    Date: 2019/1/30
    */
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.Certificate;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.asn1.x9.X9ObjectIdentifiers;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.util.encoders.Base64;
import org.treegraph.baas.core.util.crypto.x509.CertUtil;
import org.treegraph.baas.core.util.crypto.x509.EncryptUtil;
import org.treegraph.baas.core.util.crypto.x509.KeyUtil;

public class X509UtilTest {
	
	private static final String ROOT_CA_SUBJECT = "CN=rootCA,OU=it-dept,O=conflux-chain,L=SH,ST=SH,C=CN";
	
	private static final String CONSORTIUM_CERT_SUBJECT = "CN=consortiumCert,OU=rd,O=conflux-chain,L=SH,ST=SH,C=CN";
	
//	public static void test3(){
//		for(NodePo node : nodes){
//			String nodeInfo = node.getId() + "-" + node.getNodeIp() + "-" + node.getNodePort();
//			byte[] cipher = EncryptUtil.decodeByPrivateKey(algo, true, privateKey, nodeInfo.getBytes());
//			String cipherNodeInfo = Base64.toBase64String(cipher);
//			System.out.println("cipher node info====>"+cipherNodeInfo);//test data
//			consortiumNodeList.add(cipherNodeInfo);
//			
//			PublicKey publicKey = EncryptUtil.loadPublicKey(algo, consortiumKeypair.getPublicKey());//test data
//			byte[] plain = EncryptUtil.decodeByPublicKey(algo, false, publicKey, Base64.decode(cipherNodeInfo));//test data
//			String plainNodeInfo = new String(plain);
//			System.out.println("plain node info====>"+plainNodeInfo);//test data
//		}
//	}
	
    public static void test2(String algo) throws Exception{
    	KeyPair keyPair = KeyUtil.generateKeyPair(algo);
	    String publicKeyString = KeyUtil.publicKeyToBase64(keyPair.getPublic());
	    String privateKeyString = KeyUtil.privateKeyToBase64(keyPair.getPrivate());

	    SubjectPublicKeyInfo subjectPublicKeyInfo = SubjectPublicKeyInfo.getInstance(keyPair.getPublic().getEncoded());
	    AlgorithmIdentifier signAlgo = CertUtil.getSignAlgo(subjectPublicKeyInfo.getAlgorithm());
		
		if (signAlgo.getAlgorithm().equals(GMObjectIdentifiers.sm2sign_with_sm3) ||
				signAlgo.getAlgorithm().equals(X9ObjectIdentifiers.ecdsa_with_SHA256)) {
			String plainText = algo + "_测试_公钥加密私钥解密1";
			plainText = "111-192.168.121.11-9797";
			byte[] cipherData = EncryptUtil.codecByPublicKey(algo, true, KeyUtil.publicKeyFromBase64(algo, publicKeyString), plainText.getBytes());
			String cipher = Base64.toBase64String(cipherData);
			byte[] res = EncryptUtil.codecByPrivateKey(algo, false, KeyUtil.privateKeyFromBase64(algo, privateKeyString),Base64.decode(cipher));
			String restr = new String(res);
			System.out.println("原文：" + plainText);
			System.out.println("加密：" + cipher);
			System.out.println("解密：" + restr);
			
			String plainText2 = algo + "_测试_私钥加密公钥解密2";
			byte[] cipherData2 = EncryptUtil.codecByPrivateKey(algo, true, KeyUtil.privateKeyFromBase64(algo, privateKeyString), plainText2.getBytes());
			String cipher2 = Base64.toBase64String(cipherData2);
			byte[] res2 = EncryptUtil.codecByPublicKey(algo, false, KeyUtil.publicKeyFromBase64(algo, publicKeyString), Base64.decode(cipher2));
			String restr2 = new String(res2);
			System.out.println("原文：" + plainText2);
			System.out.println("加密：" + cipher2);
			System.out.println("解密：" + restr2);
			
		} else if (signAlgo.getAlgorithm().equals(PKCSObjectIdentifiers.sha256WithRSAEncryption)) {
			String plainText = algo + "_测试_公钥加密私钥解密1";
			byte[] cipherData = EncryptUtil.codecByPublicKey(algo, true, KeyUtil.publicKeyFromBase64(algo, publicKeyString), plainText.getBytes());
			String cipher = Base64.toBase64String(cipherData);
			byte[] res = EncryptUtil.codecByPrivateKey(algo, false, KeyUtil.privateKeyFromBase64(algo, privateKeyString),Base64.decode(cipher));
			String restr = new String(res);
			System.out.println("原文：" + plainText);
			System.out.println("加密：" + cipher);
			System.out.println("解密：" + restr);
			
			String plainText2 = algo + "_测试_私钥加密公钥解密2";
			byte[] cipherData2 = EncryptUtil.codecByPrivateKey(algo, true, KeyUtil.privateKeyFromBase64(algo, privateKeyString), plainText2.getBytes());
			String cipher2 = Base64.toBase64String(cipherData2);
			byte[] res2 = EncryptUtil.codecByPublicKey(algo, false, KeyUtil.publicKeyFromBase64(algo, publicKeyString), Base64.decode(cipher2));
			String restr2 = new String(res2);
			System.out.println("原文：" + plainText2);
			System.out.println("加密：" + cipher2);
			System.out.println("解密：" + restr2);
		} else {
			throw new IllegalArgumentException("密钥算法不支持");
		}
    }
	
	public static void test1(String algo) throws Exception{
		Date notBefore = new Date();
		Date notAfter = new Date(System.currentTimeMillis() + 1000L*60L*60L*24L*365L);   //365天
		
		KeyPair rootKeyPair = KeyUtil.generateKeyPair(algo);
	    Certificate rootCA = CertUtil.generateRootCA(new X500Name(ROOT_CA_SUBJECT),rootKeyPair,notBefore,notAfter);
	    System.out.println(KeyUtil.publicKeyToBase64(rootKeyPair.getPublic()));
	    System.out.println(KeyUtil.privateKeyToBase64(rootKeyPair.getPrivate()));
	    System.out.println(CertUtil.certToPem(rootCA));
	    
		KeyPair consortiumKeyPair = KeyUtil.generateKeyPair(algo);
		PKCS10CertificationRequest consortiumCsr = CertUtil.generateCSR(new X500Name(CONSORTIUM_CERT_SUBJECT),consortiumKeyPair.getPublic(),consortiumKeyPair.getPrivate());
		Certificate consortiumCert = CertUtil.generateCert(consortiumCsr,rootKeyPair.getPrivate(),rootCA.getEncoded(),notBefore,notAfter);
		System.out.println(KeyUtil.publicKeyToBase64(consortiumKeyPair.getPublic()));
		System.out.println(KeyUtil.privateKeyToBase64(consortiumKeyPair.getPrivate()));
		System.out.println(CertUtil.csrToPem(consortiumCsr));
		System.out.println(CertUtil.certToPem(consortiumCert));
	}
    
    public static void main(String[] args) throws Exception{
    	String algo = "sm2";
    	//test1(algo);
    	test2(algo);
    }
}
