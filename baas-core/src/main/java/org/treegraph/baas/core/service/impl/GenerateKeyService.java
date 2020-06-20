package org.treegraph.baas.core.service.impl;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.interfaces.ECPublicKey;
import java.security.interfaces.RSAPublicKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.regex.Pattern;

import javax.security.auth.x500.X500Principal;

import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.openssl.PEMEncryptor;
import org.bouncycastle.openssl.PKCS8Generator;
import org.bouncycastle.openssl.jcajce.JcaMiscPEMGenerator;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.openssl.jcajce.JcaPKCS8Generator;
import org.bouncycastle.openssl.jcajce.JceOpenSSLPKCS8EncryptorBuilder;
import org.bouncycastle.openssl.jcajce.JcePEMEncryptorBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OutputEncryptor;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequestBuilder;
import org.bouncycastle.util.encoders.Base64;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemObjectGenerator;
import org.bouncycastle.util.io.pem.PemWriter;
import org.springframework.stereotype.Service;
import org.treegraph.baas.core.enums.CryptoEnum.Algo;
import org.treegraph.baas.core.enums.CryptoEnum.Pkcs;
import org.treegraph.baas.core.util.LogSkip;

@Service
public class GenerateKeyService {
	
	private static final String ROOT_CA_ISSUER = "CN=rootCA,OU=rd,O=conflux-chain,L=SH,ST=SH,C=CN";
	
	private static final String CONSORTIUM_CA_ISSUER = "CN=consortiumCA,OU=rd,O=conflux-chain,L=SH,ST=SH,C=CN";

	public static boolean checkPassword(String password) {
		if (password == null || password.length() < 8) {
			throw new RuntimeException("password length too short");
		}
		if (password.matches("^[A-Za-z0-9*_^&]{8,32}$")) {
			Pattern numPattern = Pattern.compile("[0-9]+");
			Pattern lowerPattern = Pattern.compile("[a-z]+");
			Pattern upperPattern = Pattern.compile("[A-Z]+");
			Pattern charPattern = Pattern.compile("[*_^&]+");
			if (numPattern.matcher(password).find() && lowerPattern.matcher(password).find()
					&& upperPattern.matcher(password).find() && charPattern.matcher(password).find()) {
				return true;
			}
		}
		throw new RuntimeException("must contains number,a-z,A-Z and *_^&");
	}

	@LogSkip
	public KeyPair generateEncryptedSmPrivateKey(String password, Pkcs pkcsType, OutputStream outputStream)
			throws Exception {
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC", "BC");
		keyPairGenerator.initialize(256, new SecureRandom());
		KeyPair keyPair = keyPairGenerator.generateKeyPair();
		if (pkcsType == Pkcs.PKCS5) {
			writePkcs5(keyPair.getPrivate(), password, outputStream);
		} else if (pkcsType == Pkcs.PKCS8) {
			writePkcs8(keyPair.getPrivate(), password, outputStream);
		} else {
			writePlain(keyPair.getPrivate(), outputStream);
		}
		return keyPair;
	}

	@LogSkip
	public KeyPair generateEncryptedEcPrivateKey(String password, Pkcs pkcsType, OutputStream outputStream)
			throws Exception {
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("ECDSA", "BC");
		keyPairGenerator.initialize(256, new SecureRandom());
		KeyPair keyPair = keyPairGenerator.generateKeyPair();
		if (pkcsType == Pkcs.PKCS5) {
			writePkcs5(keyPair.getPrivate(), password, outputStream);
		} else if (pkcsType == Pkcs.PKCS8) {
			writePkcs8(keyPair.getPrivate(), password, outputStream);
		} else {
			writePlain(keyPair.getPrivate(), outputStream);
		}
		return keyPair;
	}

	@LogSkip
	public KeyPair generateEncryptedRsaPrivateKey(String password, Pkcs pkcsType, OutputStream outputStream)
			throws Exception {
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA", "BC");
		keyPairGenerator.initialize(2048, new SecureRandom());
		KeyPair keyPair = keyPairGenerator.generateKeyPair();
		if (pkcsType == Pkcs.PKCS5) {
			writePkcs5(keyPair.getPrivate(), password, outputStream);
		} else if (pkcsType == Pkcs.PKCS8) {
			writePkcs8(keyPair.getPrivate(), password, outputStream);
		} else {
			writePlain(keyPair.getPrivate(), outputStream);
		}
		return keyPair;
	}

	@LogSkip
	public String getECPublicKeyString(KeyPair keyPair) throws Exception {
		ECPublicKey publicKey = (ECPublicKey) keyPair.getPublic();
		String publicKeyString = Base64.toBase64String(publicKey.getEncoded());
		return publicKeyString;
	}

	@LogSkip
	public String getRsaPublicKeyString(KeyPair keyPair) throws Exception {
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		String publicKeyString = Base64.toBase64String(publicKey.getEncoded());
		return publicKeyString;
	}
	
	@LogSkip
	public void generateRootCA(Algo algo, int time, ChronoUnit timeUnit, KeyPair keyPair, OutputStream outputStream) throws Exception {
		LocalDateTime notBeforeLDT = LocalDateTime.now();
		LocalDateTime notAfterLDT = notBeforeLDT.plus(time, timeUnit);
		Date notBefore = Date.from(notBeforeLDT.atZone(ZoneId.systemDefault()).toInstant());
		Date notAfter = Date.from(notAfterLDT.atZone(ZoneId.systemDefault()).toInstant());

		// x.509 certificate builder
		X509v3CertificateBuilder v3CertBuilder = new JcaX509v3CertificateBuilder(
				new X500Principal(ROOT_CA_ISSUER),
				BigInteger.valueOf(System.currentTimeMillis()), 
				notBefore, 
				notAfter,
				new X500Principal(ROOT_CA_ISSUER),
				keyPair.getPublic());

		// content signer
		ContentSigner signer = null;
		if(Algo.ECC.getCode().equals(algo)) {
			signer = new JcaContentSignerBuilder("SHA256withECDSA")
					.setProvider("BC")
					.build(keyPair.getPrivate());
		} else if(Algo.RSA.getCode().equals(algo)) {
			signer = new JcaContentSignerBuilder("SHA256WithRSA")
					.setProvider("BC")
					.build(keyPair.getPrivate());
		} else {
			throw new RuntimeException("not support content signer["+algo.getCode()+"]");
		}

		// build x.509 certificate
		X509CertificateHolder holder = v3CertBuilder.build(signer);

		// certificate to write to string
		JcaPEMWriter jcaPEMWriter = new JcaPEMWriter(new OutputStreamWriter(outputStream));
		jcaPEMWriter.writeObject(holder);
		jcaPEMWriter.close();
	}

	@LogSkip
	public void generateConsortiumCSR(Algo alg, KeyPair keyPair, OutputStream outputStream) throws Exception {
		// certificate request builder
		JcaPKCS10CertificationRequestBuilder certRequestBuilder = new JcaPKCS10CertificationRequestBuilder(
				new X500Principal(CONSORTIUM_CA_ISSUER), keyPair.getPublic());
		
		// content signer
		ContentSigner signer = null;
		if(Algo.ECC == alg) {
			signer = new JcaContentSignerBuilder("SHA256withECDSA")
					.setProvider("BC")
					.build(keyPair.getPrivate());
		} else if(Algo.RSA == alg) {
			signer = new JcaContentSignerBuilder("SHA256WithRSA")
					.setProvider("BC")
					.build(keyPair.getPrivate());
		} else {
			throw new RuntimeException("not support content signer["+alg.getCode()+"]");
		}
		
		// build x.509 certificate
		PKCS10CertificationRequest csr = certRequestBuilder.build(signer);
		
		// certificate request to write to string
	    PemWriter pemWriter = new PemWriter(new OutputStreamWriter(outputStream));
	    pemWriter.writeObject((PemObjectGenerator)new PemObject("CERTIFICATE REQUEST", csr.getEncoded()));
	    pemWriter.close();
	}

	@LogSkip
	private void writePkcs5(PrivateKey privateKey, String password, OutputStream outputStream) throws Exception {
		PemWriter pemWriter = new PemWriter(new OutputStreamWriter(outputStream));
		PEMEncryptor pemEncryptor = (new JcePEMEncryptorBuilder("AES-256-CBC")).build(password.toCharArray());
		JcaMiscPEMGenerator gen = new JcaMiscPEMGenerator(privateKey, pemEncryptor);
		PemObject obj = gen.generate();
		pemWriter.writeObject((PemObjectGenerator) obj);
		pemWriter.close();
	}

	@LogSkip
	private void writePkcs8(PrivateKey privateKey, String password, OutputStream outputStream) throws Exception {
		PemWriter pemWriter = new PemWriter(new OutputStreamWriter(outputStream));
		JceOpenSSLPKCS8EncryptorBuilder encryptorBuilder = new JceOpenSSLPKCS8EncryptorBuilder(
				PKCS8Generator.PBE_SHA1_3DES);
		encryptorBuilder.setRandom(new SecureRandom());
		encryptorBuilder.setPasssword(password.toCharArray());
		OutputEncryptor oe = encryptorBuilder.build();
		JcaPKCS8Generator gen = new JcaPKCS8Generator(privateKey, oe);
		pemWriter.writeObject((PemObjectGenerator) gen.generate());
		pemWriter.close();
	}

	@LogSkip
	private void writePlain(PrivateKey privateKey, OutputStream outputStream) throws Exception {
		String privateKeyString = new String(Base64.toBase64String(privateKey.getEncoded()));
		outputStream.write(privateKeyString.getBytes());
	}
}