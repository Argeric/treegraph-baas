package org.treegraph.baas.core.crypto.x509;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.util.Date;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.Certificate;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.treegraph.baas.core.util.crypto.x509.CertUtil;
import org.treegraph.baas.core.util.crypto.x509.KeyUtil;

public class PrivateKeyRecoveryTest {
	
	private static final String ROOT_CA_SUBJECT = "CN=rootCA,OU=it-dept,O=conflux-chain,L=SH,ST=SH,C=CN";
	
	private static final String CONSORTIUM_CERT_SUBJECT = "CN=consortiumCert,OU=rd,O=conflux-chain,L=SH,ST=SH,C=CN";

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
		
		
		PrivateKey privateKey = consortiumKeyPair.getPrivate();
		String der = KeyUtil.privateKeyToPem(privateKey);
		PrivateKey recoveredKey = KeyUtil.privateKeyFromPem(der);
		System.out.println("privateKey=" + privateKey);
		System.out.println("privateKey.getAlgorithm()=" + privateKey.getAlgorithm());
		System.out.println("der=" + der);
		System.out.println("recoveredKey=" + privateKey);
		System.out.println("recoveredKey.getAlgorithm()=" + recoveredKey.getAlgorithm());
		System.out.println();

		if (privateKey.equals(recoveredKey)) {
			System.out.println("Key recovery ok");
		}else {
			System.err.println("Private key recovery failed");
		}

		if (privateKey.getAlgorithm().equals(recoveredKey.getAlgorithm())) {
			System.out.println("Key algorithm ok");
		}else {
			System.err.println("Private key algorithms do not match");
		}
	}
	
	public static void main(String[] args) throws Exception {
		 test1("ecc");
	}
}