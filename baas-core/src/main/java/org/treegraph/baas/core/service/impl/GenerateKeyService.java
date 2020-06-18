package org.treegraph.baas.core.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.AlgorithmParameterSpec;
import java.util.regex.Pattern;

import javax.security.auth.x500.X500Principal;

import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.sec.ECPrivateKey;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;
import org.bouncycastle.openssl.PEMEncryptor;
import org.bouncycastle.openssl.PKCS8Generator;
import org.bouncycastle.openssl.jcajce.JcaMiscPEMGenerator;
import org.bouncycastle.openssl.jcajce.JcaPKCS8Generator;
import org.bouncycastle.openssl.jcajce.JceOpenSSLPKCS8EncryptorBuilder;
import org.bouncycastle.openssl.jcajce.JcePEMEncryptorBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OutputEncryptor;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequestBuilder;
import org.bouncycastle.util.encoders.Base64;
import org.bouncycastle.util.encoders.Hex;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemObjectGenerator;
import org.bouncycastle.util.io.pem.PemWriter;
import org.springframework.stereotype.Service;
import org.treegraph.baas.core.enums.CryptoEnum.Pkcs;
import org.treegraph.baas.core.util.LogSkip;

@Service
public class GenerateKeyService {

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
	public void writePkcs5(PrivateKey privateKey, String password, OutputStream outputStream) throws Exception {
		PemWriter pemWriter = new PemWriter(new OutputStreamWriter(outputStream));
		PEMEncryptor pemEncryptor = (new JcePEMEncryptorBuilder("AES-256-CBC")).build(password.toCharArray());
		JcaMiscPEMGenerator gen = new JcaMiscPEMGenerator(privateKey, pemEncryptor);
		PemObject obj = gen.generate();
		pemWriter.writeObject((PemObjectGenerator) obj);
		pemWriter.close();
	}

	@LogSkip
	public void writePkcs8(PrivateKey privateKey, String password, OutputStream outputStream) throws Exception {
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
	public void writePlain(PrivateKey privateKey, OutputStream outputStream) throws Exception {
		String privateKeyString = new String(Base64.toBase64String(privateKey.getEncoded()));
		outputStream.write(privateKeyString.getBytes());
	}

	@LogSkip
	public KeyPair generateEncryptedSmPrivateKey(String password, Boolean isPkcs8, OutputStream outputStream)
			throws Exception {
		ECNamedCurveParameterSpec ecNamedCurveParameterSpec = ECNamedCurveTable.getParameterSpec("sm2p256v1");
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC", "BC");
		keyPairGenerator.initialize((AlgorithmParameterSpec) ecNamedCurveParameterSpec, new SecureRandom());
		KeyPair keyPair = keyPairGenerator.generateKeyPair();
		if (!isPkcs8.booleanValue()) {
			writePkcs5(keyPair.getPrivate(), password, outputStream);
		} else {
			writePkcs8(keyPair.getPrivate(), password, outputStream);
		}
		return keyPair;
	}

	@LogSkip
	public KeyPair generateEncryptedEcPrivateKey(String password, Boolean isPkcs8, OutputStream outputStream)
			throws Exception {
		ECNamedCurveParameterSpec ecParameterSpec = ECNamedCurveTable.getParameterSpec("secp256k1");
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("ECDSA", "BC");
		keyPairGenerator.initialize((AlgorithmParameterSpec) ecParameterSpec, new SecureRandom());
		KeyPair keyPair = keyPairGenerator.generateKeyPair();
		if (!isPkcs8.booleanValue()) {
			writePkcs5(keyPair.getPrivate(), password, outputStream);
		} else {
			writePkcs8(keyPair.getPrivate(), password, outputStream);
		}
		return keyPair;
	}

	@LogSkip
	public KeyPair generateEncryptedRsaPrivateKey(String password, Boolean isPkcs8, OutputStream outputStream)
			throws Exception {
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA", "BC");
		keyPairGenerator.initialize(2048, new SecureRandom());
		KeyPair keyPair = keyPairGenerator.generateKeyPair();
		if (!isPkcs8.booleanValue()) {
			writePkcs5(keyPair.getPrivate(), password, outputStream);
		} else {
			writePkcs8(keyPair.getPrivate(), password, outputStream);
		}
		return keyPair;
	}

	@LogSkip
	public KeyPair generateEncryptedSmPrivateKey(String password, Pkcs pkcsType, OutputStream outputStream)
			throws Exception {
		ECNamedCurveParameterSpec ecNamedCurveParameterSpec = ECNamedCurveTable.getParameterSpec("sm2p256v1");
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC", "BC");
		keyPairGenerator.initialize((AlgorithmParameterSpec) ecNamedCurveParameterSpec, new SecureRandom());
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
		ECNamedCurveParameterSpec ecParameterSpec = ECNamedCurveTable.getParameterSpec("secp256k1");
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("ECDSA", "BC");
		keyPairGenerator.initialize((AlgorithmParameterSpec) ecParameterSpec, new SecureRandom());
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
	public byte[] generateCSR(String sigAlg, KeyPair keyPair) throws Exception {
		JcaPKCS10CertificationRequestBuilder jcaPKCS10CertificationRequestBuilder = new JcaPKCS10CertificationRequestBuilder(
				new X500Principal("CN=client"), keyPair.getPublic());
		JcaContentSignerBuilder csBuilder = new JcaContentSignerBuilder(sigAlg);
		ContentSigner signer = csBuilder.build(keyPair.getPrivate());
		PKCS10CertificationRequest csr = jcaPKCS10CertificationRequestBuilder.build(signer);
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		PemWriter pemWriter = new PemWriter(new OutputStreamWriter(byteArrayOutputStream));
		pemWriter.writeObject((PemObjectGenerator) new PemObject("CERTIFICATE REQUEST", csr.getEncoded()));
		pemWriter.close();
		return byteArrayOutputStream.toByteArray();
	}

	@LogSkip
	public String getECPublicKeyString(KeyPair keyPair) throws Exception {
		ECPrivateKey privateKey = ECPrivateKey
				.getInstance(PrivateKeyInfo.getInstance(keyPair.getPrivate().getEncoded()).parsePrivateKey());
		String privateKeyString = Base64.toBase64String(privateKey.getEncoded());
		return privateKeyString;
	}

	@LogSkip
	public String getRsaPublicKeyString(KeyPair keyPair) throws Exception {
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		String publicKeyString = Base64.toBase64String(publicKey.getEncoded());
		return publicKeyString;
	}
}