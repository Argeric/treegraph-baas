package org.treegraph.baas.core.crypto;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.Provider;
import java.security.PublicKey;
import java.security.Security;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.ECPublicKey;

import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentVerifierProvider;
import org.bouncycastle.operator.jcajce.JcaContentVerifierProviderBuilder;

public class CertSignature {

	/**
	 * 从文件中输入流中加载公钥
	 * 
	 * @param in 公钥输入流
	 * @throws Exception 加载cert时产生的异常
	 */
	public static X509Certificate loadCertByFile(String path) throws Exception {
		try {
			CertificateFactory certificate_factory = CertificateFactory.getInstance("X.509");
			FileInputStream file_inputstream = new FileInputStream(path + "/rootCA.cer");
			X509Certificate x509certificate = (X509Certificate) certificate_factory
					.generateCertificate(file_inputstream);
			return x509certificate;
		} catch (IOException e) {
			throw new Exception("cert流读取错误");
		} catch (NullPointerException e) {
			throw new Exception("cert输入流为空");
		}
	}

	public static X509CertificateHolder loadCertByStr(X509Certificate cert) throws Exception {
		try {
			byte[] buffer = cert.getEncoded();
			return new X509CertificateHolder(buffer);
		} catch (Exception e) {
			throw e;
		}
	}

	// 验签
	public static boolean verifyCert(PublicKey publicKey, X509CertificateHolder certHolder) {
		try {
			ContentVerifierProvider contentVerifierProvider = new JcaContentVerifierProviderBuilder().setProvider("BC")
					.build(publicKey);
			boolean bool = certHolder.isSignatureValid(contentVerifierProvider);
			System.out.println("验证：" + bool);
			return bool;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	public static void main(String[] args) throws Exception {
		Security.addProvider((Provider) new BouncyCastleProvider());
		String filepath = "/Users/wangzhenting/Downloads/rootCA/";
		ECPublicKey publicKey = ECEncrypt.loadPublicKeyByStr(ECEncrypt.loadPublicKeyByFile(filepath));
		X509CertificateHolder certHolder = CertSignature.loadCertByStr(CertSignature.loadCertByFile(filepath));
		CertSignature.verifyCert(publicKey, certHolder);
	}
}