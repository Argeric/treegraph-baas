package org.treegraph.baas.core.util.crypto.x509;

import static org.treegraph.baas.core.enums.CryptoEnum.Algo.ECC;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.time.Duration;
import java.util.Calendar;
import java.util.Date;

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
import static org.treegraph.baas.core.enums.CryptoEnum.CertificateSignType;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Validated
@Service
@AllArgsConstructor
@Slf4j
public class KeypairCertUtil {
	
    private VaultOperations vaultOperations;
    
    public KeypairCertBo buildKeypairCert1(String algo, String subject, Date latestCertExpireDate,
    		CertificateSignType signType, String parentKeypairAlgo, KeypairPo parentKeypair) {
		KeyPair keyPair = null;
		String publicKey = null;
		String privateKey = null;
		try {
			keyPair = KeyUtil.generateKeyPair(algo);
			publicKey = KeyUtil.publicKeyToBase64(keyPair.getPublic());
			privateKey = KeyUtil.privateKeyToBase64(keyPair.getPrivate());
		} catch(Exception e) {
			throw new AlertException("generate keypair error!");
		}
		
		if(!Algo.RSA.getCode().equals(algo)) {
			return KeypairCertBo.builder()
					.algorithm(algo)
					.publicKey(publicKey)
					.privateKey(privateKey)
					.build();
		}
		
		String csrStr = null;
		String certStr = null;
		try {
			PKCS10CertificationRequest csr = CertUtil.generateCSR(new X500Name(subject), keyPair.getPublic(),
					keyPair.getPrivate(), Lists.newArrayList("101.200.183.108"));
			if (signType == CryptoEnum.CertificateSignType.BC) {
				certStr = signCertificateRequestByBC(parentKeypairAlgo, parentKeypair, csr, true);
			} else if(signType == CryptoEnum.CertificateSignType.VAULT) {
				certStr = signCertificateRequestByVault(csr, latestCertExpireDate);
			} else {
				throw new AlertException("certificate sign request not supported!");
			}
			csrStr = CertUtil.csrToPem(csr);
		} catch(Exception e) {
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
    
	public KeypairCertBo buildKeypairCert(String algo, String subject, Date latestCertExpireDate,
			boolean isGRpcCert, String parentKeypairAlgo, KeypairPo parentKeypair) {
		KeyPair keyPair = null;
		String publicKey = null;
		String privateKey = null;
		try {
			keyPair = KeyUtil.generateKeyPair(algo);
			publicKey = KeyUtil.publicKeyToBase64(keyPair.getPublic());
			privateKey = KeyUtil.privateKeyToBase64(keyPair.getPrivate());
		} catch(Exception e) {
			throw new AlertException("generate keypair error!");
		}
		
		String csrStr = null;
		String certStr = null;
		try {
			if(parentKeypair != null) {
				log.info("buildKeypairCert---->333, isGRpcCert:{}", isGRpcCert);
				PKCS10CertificationRequest csr = CertUtil.generateCSR(new X500Name(subject), keyPair.getPublic(),
						keyPair.getPrivate(), Lists.newArrayList("101.200.183.108"));
				csrStr = CertUtil.csrToPem(csr);
				if (algo.equals(ECC.getCode())
						|| isGRpcCert
						|| X509CertConstant.CERTIFICATE_SIGN_TYPE == CryptoEnum.CertificateSignType.BC) {
					certStr = signCertificateRequestByBC(parentKeypairAlgo, parentKeypair, csr, true);
				} else if(X509CertConstant.CERTIFICATE_SIGN_TYPE == CryptoEnum.CertificateSignType.VAULT) {
					certStr = signCertificateRequestByVault(csr, latestCertExpireDate);
				} else {
					throw new AlertException("certificate sign request not supported!");
				}
			}
		} catch(Exception e) {
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
			PKCS10CertificationRequest csr, boolean isEntityCert) throws Exception {
		log.info("buildKeypairCert---->444, parentKeypairAlgo:{}", parentKeypairAlgo);
		PrivateKey parentPrivateKey = KeyUtil.privateKeyFromBase64(parentKeypairAlgo, parentKeypair.getPrivateKey());
		byte[] parentCertBytes = CertUtil.certFromPem(parentKeypair.getCertStr()).getEncoded();
		Certificate cert = CertUtil.generateCert(csr, parentPrivateKey, parentCertBytes,
				CertUtil.getNotBefore(), CertUtil.getNotAfter(X509CertConstant.EXPIRE_CONSORTIUM_CERT_IN_DYAS),
				isEntityCert);
		return CertUtil.certToPem(cert);
	}

	private String signCertificateRequestByVault(PKCS10CertificationRequest csr, Date lastCertExpireDate)
			throws Exception {
		log.info("buildKeypairCert---->555");
		int lastCertLeftDays = 0;
		if (lastCertExpireDate != null) {
			lastCertLeftDays = DateUtils.betweenDays(Calendar.getInstance().getTime(), lastCertExpireDate);
			lastCertLeftDays = lastCertLeftDays > 0 ? lastCertLeftDays : 0;
		}
		
		VaultCertificateRequest request = VaultCertificateRequest.builder()
				.ttl(Duration.ofDays(lastCertLeftDays + X509CertConstant.EXPIRE_CONSORTIUM_CERT_IN_DYAS))
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
        StringBuffer content = new StringBuffer();  
        for (String line : lines) {  
        	content.append(line).append("\n");  
        }
        
        StringBuffer code = new StringBuffer("-----BEGIN CERTIFICATE-----\n");  
        code.append(content.toString());
        code.append("-----END CERTIFICATE-----"); 
        
        return code.toString();  
    }
}
