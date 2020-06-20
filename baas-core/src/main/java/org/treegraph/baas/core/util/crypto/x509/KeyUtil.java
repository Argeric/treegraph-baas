package org.treegraph.baas.core.util.crypto.x509;

import org.bouncycastle.asn1.gm.GMObjectIdentifiers;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.asn1.x9.X9ObjectIdentifiers;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.util.encoders.Base64;

import java.security.*;
import java.security.interfaces.ECPublicKey;
import java.security.interfaces.RSAPublicKey;

public class KeyUtil {
	
    public static final String BC = BouncyCastleProvider.PROVIDER_NAME;
    
    static {
        Security.addProvider(new BouncyCastleProvider());
    }
    
    public static KeyPair generateKeyPair(String algo) throws NoSuchProviderException, NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        KeyPairGenerator keyPairGenerator;
        SecureRandom random = new SecureRandom();
        if("RSA".equalsIgnoreCase(algo)) {
            keyPairGenerator = KeyPairGenerator.getInstance("RSA",BC);
            keyPairGenerator.initialize(2048,random);
        } else if("SM2".equalsIgnoreCase(algo)) {
            ECParameterSpec ecSpec = ECNamedCurveTable.getParameterSpec("sm2p256v1");
            keyPairGenerator = KeyPairGenerator.getInstance("ECDSA",BC);
            keyPairGenerator.initialize(ecSpec,random);
        } else if("ECC".equalsIgnoreCase(algo)) {
            ECParameterSpec ecSpec = ECNamedCurveTable.getParameterSpec("secp256k1");
            keyPairGenerator = KeyPairGenerator.getInstance("ECDSA",BC);
            keyPairGenerator.initialize(ecSpec,random);
        } else
            throw new IllegalArgumentException("算法不支持");
        return keyPairGenerator.generateKeyPair();
    }

    public static PublicKey getPublicKey(SubjectPublicKeyInfo subjectPublicKeyInfo) throws Exception{
        BouncyCastleProvider bouncyCastleProvider = ((BouncyCastleProvider)Security.getProvider(BC));
        bouncyCastleProvider.addKeyInfoConverter(PKCSObjectIdentifiers.rsaEncryption,new org.bouncycastle.jcajce.provider.asymmetric.rsa.KeyFactorySpi());
        bouncyCastleProvider.addKeyInfoConverter(X9ObjectIdentifiers.id_ecPublicKey,new org.bouncycastle.jcajce.provider.asymmetric.ec.KeyFactorySpi.EC());
        return BouncyCastleProvider.getPublicKey(subjectPublicKeyInfo);
    }
    
    public static String getPrivateKeyString(KeyPair keyPair){
		return Base64.toBase64String(keyPair.getPrivate().getEncoded());
	}
	
	public static String getPublicKeyString(KeyPair keyPair) throws Exception {
		SubjectPublicKeyInfo subjectPublicKeyInfo = SubjectPublicKeyInfo.getInstance(keyPair.getPublic().getEncoded());
		AlgorithmIdentifier signAlgo = CertUtil.getSignAlgo(subjectPublicKeyInfo.getAlgorithm());
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
}
