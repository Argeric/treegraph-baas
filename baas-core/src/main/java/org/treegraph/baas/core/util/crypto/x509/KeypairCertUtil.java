package org.treegraph.baas.core.util.crypto.x509;

import static org.treegraph.baas.core.constant.X509CertConstant.TTL_CONSORTIUM_CA_IN_DAY;
import static org.treegraph.baas.core.constant.X509CertConstant.TTL_RPC_CERT_IN_DAY;

import com.google.common.base.Splitter;
import com.google.common.collect.Maps;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.time.Duration;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x509.Certificate;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.vault.core.VaultOperations;
import org.springframework.vault.support.VaultCertificateRequest;
import org.springframework.vault.support.VaultSignCertificateRequestResponse;
import org.treegraph.baas.core.constant.X509CertConstant;
import org.treegraph.baas.core.domain.KeypairPo;
import org.treegraph.baas.core.enums.CryptoEnum;
import org.treegraph.baas.core.enums.CryptoEnum.CertSignType;
import org.treegraph.baas.core.enums.CryptoEnum.KeyType;
import org.treegraph.baas.core.exception.AlertException;
import org.treegraph.baas.core.service.bo.KeypairCertBo;
import org.treegraph.baas.core.util.DateUtils;

@Validated
@Service
@AllArgsConstructor
@Slf4j
public class KeypairCertUtil {

	private final VaultOperations vaultOperations;

	public KeypairCertBo buildRootKeypairCert() {
		return this.buildKeypairCert(KeyType.ROOT_CA, null, null, null, null);
	}

	public KeypairCertBo buildConsKeypairCert(Date lastCrtExpire, KeypairPo bcRootCa) {
			return this.buildKeypairCert(KeyType.CONSORTIUM, lastCrtExpire, null, null, bcRootCa);
	}

	public KeypairCertBo buildNodeKeypairCert() {
			return this.buildKeypairCert(KeyType.NODE, null, null, null, null);
	}

	public KeypairCertBo buildRpcKeypairCert(Date lastCrtExpire, List<String> sanList, String vaultCa, KeypairPo bcRootCa) {
		return this.buildKeypairCert(KeyType.RPC, lastCrtExpire, sanList, vaultCa, bcRootCa);
	}

	private KeypairCertBo buildKeypairCert(CryptoEnum.KeyType keyType, Date lastCrtExpire,
			List<String> sanList, String vaultCa, KeypairPo bcCa) {
		KeyPair keyPair;
		String publicKey;
		String privateKey;
		int keyTypeCode = keyType.getCode();
		try {
			keyPair = KeyUtil.generateKeyPair(keyType.getAlgo().getCode());
			publicKey = KeyUtil.publicKeyToBase64(keyPair.getPublic());
			privateKey = KeyUtil.privateKeyToBase64(keyPair.getPrivate());
		} catch (Exception e) {
			throw new AlertException("generate.keypair.error");
		}
		if (keyTypeCode == KeyType.NODE.getCode()) {
			return KeypairCertBo.builder()
					.algorithm(keyType.getAlgo().getCode())
					.publicKey(publicKey)
					.privateKey(privateKey)
					.build();
		}

		String certStr = null, csrStr = null;
		try {
			if(keyTypeCode == KeyType.ROOT_CA.getCode()){
				Certificate rootCA = CertUtil.generateRootCA(new X500Name(keyType.getSubject().getCode()), keyPair,
						CertUtil.getNotBefore(), CertUtil.getNotAfter(X509CertConstant.TTL_ROOT_CA_IN_DAY));
				certStr = CertUtil.certToPem(rootCA);
			} else{
				PKCS10CertificationRequest csr = CertUtil.generateCSR(new X500Name(keyType.getSubject().getCode()),
						keyPair.getPublic(), keyPair.getPrivate(), sanList);
				CertSignType signType = keyType.getSignType();
				Assert.isTrue(signType == CertSignType.BC || signType == CertSignType.VAULT, "sign.csr.not.supported");
				int ttl = keyTypeCode == KeyType.CONSORTIUM.getCode() ? TTL_CONSORTIUM_CA_IN_DAY : TTL_RPC_CERT_IN_DAY;
				if (keyType.getSignType() == CertSignType.BC) {
					Assert.notNull(bcCa,"parentKeypair.not.found");
					certStr = signCertificateRequestByBC(bcCa, csr, lastCrtExpire, ttl);
				}
				if (keyType.getSignType()  == CertSignType.VAULT) {
					certStr = signCertificateRequestByVault(vaultCa, csr, lastCrtExpire, ttl);
				}
				csrStr = CertUtil.csrToPem(csr);
			}
		} catch (Exception e) {
			throw new RuntimeException("generate.cert.error", e);
		}

		return KeypairCertBo.builder()
				.algorithm(keyType.getAlgo().getCode())
				.publicKey(publicKey)
				.privateKey(privateKey)
				.csrStr(csrStr)
				.certStr(certStr)
				.build();
	}

	private String signCertificateRequestByBC(KeypairPo parentKeypair, PKCS10CertificationRequest csr,
			Date lastCrtExpire, int ttl) throws Exception {
		PrivateKey parentPrivateKey = KeyUtil.privateKeyFromBase64(parentKeypair.getAlgorithm(),
				parentKeypair.getPrivateKey());
		byte[] parentCertBytes = CertUtil.certFromPem(parentKeypair.getCertStr()).getEncoded();
		int lastCertTtlInDay = getLastCrtTtlInDay(lastCrtExpire);
		Certificate cert = CertUtil.generateCert(csr, parentPrivateKey, parentCertBytes,
				CertUtil.getNotBefore(), CertUtil.getNotAfter(lastCertTtlInDay + ttl), true);
		return CertUtil.certToPem(cert);
	}

	private String signCertificateRequestByVault(String pkiPath, PKCS10CertificationRequest csr,
			Date lastCrtExpire, int ttl) throws Exception {
		int lastCertTtlInDay = getLastCrtTtlInDay(lastCrtExpire);
		Map<String, String> displayNameMap = parseSubject(csr.getSubject());
		String cn = displayNameMap.get(BCStyle.INSTANCE.oidToDisplayName(BCStyle.CN));

		VaultCertificateRequest request = VaultCertificateRequest.builder()
				.ttl(Duration.ofDays(lastCertTtlInDay + ttl))
				.commonName(cn).build();
		VaultSignCertificateRequestResponse certificateResponse = vaultOperations
				.opsForPki(pkiPath)
				.signCertificateRequest(X509CertConstant.VAULT_ROLE_ISSUER_RPC, CertUtil.csrToPem(csr), request);
		org.springframework.vault.support.Certificate cert = certificateResponse.getRequiredData();
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

	private Map<String, String> parseSubject(X500Name subject) {
		String displayName = BCStyle.INSTANCE.toString(subject);
		Map<String, String> displayNameMap = Maps.newHashMap();
		Arrays.stream(StringUtils.split(displayName, ",")).forEach(item -> {
			String[] paras = item.split("=");
			displayNameMap.put(paras[0], paras[1]);
		});
		return displayNameMap;
	}

	private int getLastCrtTtlInDay(Date lastCrtExpire){
		int lastCrtLeftDays = 0;
		if (lastCrtExpire != null) {
			lastCrtLeftDays = DateUtils.betweenDays(Calendar.getInstance().getTime(), lastCrtExpire);
			lastCrtLeftDays = Math.max(lastCrtLeftDays, 0);
		}
		return lastCrtLeftDays;
	}
}
