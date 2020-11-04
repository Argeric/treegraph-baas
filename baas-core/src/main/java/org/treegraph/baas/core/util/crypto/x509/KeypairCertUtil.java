package org.treegraph.baas.core.util.crypto.x509;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Splitter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.Certificate;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.vault.core.VaultOperations;
import org.springframework.vault.support.VaultCertificateRequest;
import org.springframework.vault.support.VaultSignCertificateRequestResponse;
import org.treegraph.baas.core.constant.X509CertConstant;
import org.treegraph.baas.core.domain.KeypairPo;
import org.treegraph.baas.core.enums.CryptoEnum;
import org.treegraph.baas.core.enums.CryptoEnum.Algo;
import org.treegraph.baas.core.exception.AlertException;
import org.treegraph.baas.core.service.bo.KeypairCertBo;
import org.treegraph.baas.core.util.DateUtils;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.time.Duration;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Validated
@Service
@AllArgsConstructor
@Slf4j
public class KeypairCertUtil {

	private final VaultOperations vaultOperations;

	public KeypairCertBo buildKeypair(String algo) {
		String publicKey;
		String privateKey;
		try {
			KeyPair keyPair = KeyUtil.generateKeyPair(algo);
			publicKey = KeyUtil.publicKeyToBase64(keyPair.getPublic());
			privateKey = KeyUtil.privateKeyToBase64(keyPair.getPrivate());
		} catch (Exception e) {
			throw new AlertException("generate keypair error!");
		}

		return KeypairCertBo.builder()
				.algorithm(algo)
				.publicKey(publicKey)
				.privateKey(privateKey)
				.build();
	}

	public KeypairCertBo buildKeypairCert(String algo, String subject, Date latestCertExpireDate, List<String> sanList,
										  CryptoEnum.KeyType keyType, KeypairPo parentKeypair) {
		KeyPair keyPair;
		String publicKey;
		String privateKey;
		try {
			keyPair = KeyUtil.generateKeyPair(algo);
			publicKey = KeyUtil.publicKeyToBase64(keyPair.getPublic());
			privateKey = KeyUtil.privateKeyToBase64(keyPair.getPrivate());
		} catch (Exception e) {
			throw new AlertException("generate keypair error!");
		}
		if (!Algo.RSA.getCode().equals(algo)) {
			return KeypairCertBo.builder()
					.algorithm(algo)
					.publicKey(publicKey)
					.privateKey(privateKey)
					.build();
		}

		String csrStr;
		String certStr;
		if (parentKeypair == null) {
			throw new AlertException("parentKeypair.not.found");
		}
		try {
			PKCS10CertificationRequest csr = CertUtil.generateCSR(new X500Name(subject), keyPair.getPublic(),
					keyPair.getPrivate(), sanList);
			if (keyType == CryptoEnum.KeyType.RPC ||
					X509CertConstant.DEFAULT_CERT_SIGN_TYPE == CryptoEnum.CertificateSignType.BC) {
				certStr = signCertificateRequestByBC(parentKeypair.getAlgorithm(), parentKeypair, csr);
			} else if (X509CertConstant.DEFAULT_CERT_SIGN_TYPE == CryptoEnum.CertificateSignType.VAULT) {
				certStr = signCertificateRequestByVault(csr, latestCertExpireDate);
			} else {
				throw new AlertException("certificate sign request not supported!");
			}
			csrStr = CertUtil.csrToPem(csr);
		} catch (Exception e) {
			throw new AlertException(e);
		}

		return KeypairCertBo.builder()
				.algorithm(algo)
				.publicKey(publicKey)
				.privateKey(privateKey)
				.csrStr(csrStr)
				.certStr(certStr)
				.build();
	}

	private String signCertificateRequestByBC(String parentKeypairAlgo, KeypairPo parentKeypair,
											  PKCS10CertificationRequest csr) throws Exception {
		PrivateKey parentPrivateKey = KeyUtil.privateKeyFromBase64(parentKeypairAlgo, parentKeypair.getPrivateKey());
		byte[] parentCertBytes = CertUtil.certFromPem(parentKeypair.getCertStr()).getEncoded();
		Certificate cert = CertUtil.generateCert(csr, parentPrivateKey, parentCertBytes,
				CertUtil.getNotBefore(), CertUtil.getNotAfter(X509CertConstant.EXPIRE_CONSORTIUM_CERT_IN_DAY),
				true);
		return CertUtil.certToPem(cert);
	}

	private String signCertificateRequestByVault(PKCS10CertificationRequest csr, Date lastCertExpireDate)
			throws Exception {
		int lastCertLeftDays = 0;
		if (lastCertExpireDate != null) {
			lastCertLeftDays = DateUtils.betweenDays(Calendar.getInstance().getTime(), lastCertExpireDate);
			lastCertLeftDays = Math.max(lastCertLeftDays, 0);
		}

		VaultCertificateRequest request = VaultCertificateRequest.builder()
				.ttl(Duration.ofDays(lastCertLeftDays + X509CertConstant.EXPIRE_CONSORTIUM_CERT_IN_DAY))
				.commonName(X509CertConstant.COMMON_NAME_CONSORTIUM_CERT).build();
		VaultSignCertificateRequestResponse certificateResponse = vaultOperations
				.opsForPki(X509CertConstant.VAULT_PKI_PATH)
				.signCertificateRequest(X509CertConstant.VAULT_ROLE_ISSUE_CERTIFICATE, CertUtil.csrToPem(csr), request);
		org.springframework.vault.support.Certificate cert = certificateResponse.getRequiredData();
		log.info("signCertificateRequestByVault resp:{}", JSON.toJSONString(cert));
		return generatePem(cert.getCertificate());
	}

	private String generatePem(String base64Str) {
		Iterable<String> lines = Splitter.fixedLength(64).split(base64Str);
		StringBuilder content = new StringBuilder();
		for (String line : lines) {
			content.append(line).append("\n");
		}
		return "-----BEGIN CERTIFICATE-----\n" + content.toString() +
				"-----END CERTIFICATE-----";
	}
}