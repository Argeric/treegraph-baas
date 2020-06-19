package org.treegraph.baas.core.crypto.x509;
//import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.security.KeyPair;
import java.security.interfaces.ECPublicKey;
import java.security.interfaces.RSAPublicKey;
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
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.util.encoders.Base64;

public class Tests {
	
	public static String getPrivateKeyString(KeyPair keyPair){
		return Base64.toBase64String(keyPair.getPrivate().getEncoded());
	}
	
	public static String getPublicKeyString(KeyPair keyPair) throws Exception {
		SubjectPublicKeyInfo subjectPublicKeyInfo = SubjectPublicKeyInfo.getInstance(keyPair.getPublic().getEncoded());
		AlgorithmIdentifier signAlgo = CertUtils.getSignAlgo(subjectPublicKeyInfo.getAlgorithm());
		if (signAlgo.getAlgorithm().equals(GMObjectIdentifiers.sm2sign_with_sm3) ||
				signAlgo.getAlgorithm().equals(X9ObjectIdentifiers.ecdsa_with_SHA256)) {
			ECPublicKey publicKey = (ECPublicKey) keyPair.getPublic();
			return Base64.toBase64String(publicKey.getEncoded());
		} else if (signAlgo.getAlgorithm().equals(PKCSObjectIdentifiers.sha256WithRSAEncryption)) {
			RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
			return Base64.toBase64String(publicKey.getEncoded());
		} else {
			throw new IllegalArgumentException("密钥算法不支持");
		}
	}
	
    public static String getCertStr(Certificate x509Certificate) throws Exception{
    	X509CertificateHolder holder = new X509CertificateHolder(x509Certificate);
    	
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        JcaPEMWriter pemWriter = new JcaPEMWriter(new OutputStreamWriter(bos));
        pemWriter.writeObject(holder);
        pemWriter.flush();
        pemWriter.close();
        
        return bos.toString();
    }
	
    public static String getCsrStr(Object obj) throws Exception{
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        JcaPEMWriter pemWriter = new JcaPEMWriter(new OutputStreamWriter(bos));
        pemWriter.writeObject(obj);
        pemWriter.flush();
        pemWriter.close();
        return bos.toString();
    }
	
	public static void test2() throws Exception{
		Date notBefore = new Date();
		Date notAfter = new Date(System.currentTimeMillis() + 1000L*60L*60L*24L*365L);   //365天
		
		KeyPair rootKeyPair_ECC = KeyUtils.generateKeyPair("ecc");
	    Certificate rootCA_ECC = CertUtils.generateRootCA(new X500Name("C=CN,CN=ECCRootCa"),rootKeyPair_ECC,notBefore,notAfter);
	    System.out.println(getPublicKeyString(rootKeyPair_ECC));
	    System.out.println(getPrivateKeyString(rootKeyPair_ECC));
	    System.out.println(getCertStr(rootCA_ECC));
	    
		KeyPair consortiumKeyPair_ECC = KeyUtils.generateKeyPair("ecc");
		PKCS10CertificationRequest consortiumCsr_ECC = CertUtils.generateCSR(new X500Name("C=CN,CN=ECCEntityCert"),consortiumKeyPair_ECC.getPublic(),consortiumKeyPair_ECC.getPrivate());
		Certificate consortiumCert_ECC = CertUtils.generateCert(consortiumCsr_ECC,rootKeyPair_ECC.getPrivate(),rootCA_ECC.getEncoded(),notBefore,notAfter);
		System.out.println(getPublicKeyString(consortiumKeyPair_ECC));
		System.out.println(getPrivateKeyString(consortiumKeyPair_ECC));
		System.out.println(getCsrStr(consortiumCsr_ECC));
		System.out.println(getCertStr(consortiumCert_ECC));
	}

	public static void test1() throws Exception{
		Date notBefore = new Date();
		Date notAfter = new Date(System.currentTimeMillis() + 1000L*60L*60L*24L*365L);   //365天
		
        //生成密钥对
        KeyPair eccrootcakey = KeyUtils.generateKeyPair("ecc");
        KeyPair sm2rootcakey  = KeyUtils.generateKeyPair("sm2");
        KeyPair rsarootcakey  = KeyUtils.generateKeyPair("rsa");
        //自签发根证书
        Certificate eccCaCert = CertUtils.generateRootCA(new X500Name("C=CN,CN=ECCRootCa"),eccrootcakey,notBefore,notAfter);
        Certificate sm2CaCert = CertUtils.generateRootCA(new X500Name("C=CN,CN=SM2RootCa"),sm2rootcakey,notBefore,notAfter);
        Certificate rsaCaCert = CertUtils.generateRootCA(new X500Name("C=CN,CN=RSARootCa"),rsarootcakey,notBefore,notAfter);
        
        //生成密钥对
        KeyPair ecckey = KeyUtils.generateKeyPair("ecc");
        KeyPair sm2key  = KeyUtils.generateKeyPair("sm2");
        KeyPair rsakey  = KeyUtils.generateKeyPair("rsa");
        //生成证书请求
        PKCS10CertificationRequest eccCsr = CertUtils.generateCSR(new X500Name("C=CN,CN=ECCEntityCert"),ecckey.getPublic(),ecckey.getPrivate());
        PKCS10CertificationRequest sm2Csr = CertUtils.generateCSR(new X500Name("C=CN,CN=SM2EntityCert"),sm2key.getPublic(),sm2key.getPrivate());
        PKCS10CertificationRequest rsaCsr = CertUtils.generateCSR(new X500Name("C=CN,CN=RSAEntityCert"),rsakey.getPublic(),rsakey.getPrivate());
        System.out.println(getCsrStr(eccCsr));
        System.out.println(getCsrStr(sm2Csr));
        System.out.println(getCsrStr(rsaCsr));
        //根证书签发生成实体证书
        Certificate eccEntityCert = CertUtils.generateCert(eccCsr,eccrootcakey.getPrivate(),eccCaCert.getEncoded(),notBefore,notAfter);
        Certificate sm2EntityCert = CertUtils.generateCert(sm2Csr,sm2rootcakey.getPrivate(),sm2CaCert.getEncoded(),notBefore,notAfter);
        Certificate rsaEntityCert = CertUtils.generateCert(rsaCsr,rsarootcakey.getPrivate(),rsaCaCert.getEncoded(),notBefore,notAfter);
        //写入证书DER到文件
        writeFile(eccCaCert.getEncoded(),"eccRootCert.cer");
        writeFile(sm2CaCert.getEncoded(),"sm2RootCert.cer");
        writeFile(rsaCaCert.getEncoded(),"rsaRootCert.cer");
        writeFile(eccEntityCert.getEncoded(),"eccEntityCert.cer");
        writeFile(sm2EntityCert.getEncoded(),"sm2EntityCert.cer");
        writeFile(rsaEntityCert.getEncoded(),"rsaEntityCert.cer");
    }

    private static void writeFile(byte[] data, String path) throws Exception{
        File f = new File(path);
        if(!f.exists()) {
            f.createNewFile();
        }
        FileOutputStream out = new FileOutputStream(f);
        out.write(data);
        out.close();
    }
    
    public static void main(String[] args) throws Exception{
    	test2();
    }
}
