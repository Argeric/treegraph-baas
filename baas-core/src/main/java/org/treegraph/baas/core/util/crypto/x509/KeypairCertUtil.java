package org.treegraph.baas.core.util.crypto.x509;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.cert.CertificateEncodingException;
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
import org.treegraph.baas.core.exception.AlertException;
import org.treegraph.baas.core.service.bo.KeypairCertBo;
import org.treegraph.baas.core.util.DateUtils;
import static org.treegraph.baas.core.enums.CryptoEnum.Algo.ECC;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Splitter;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Validated
@Service
@AllArgsConstructor
@Slf4j
public class KeypairCertUtil {
	
    private VaultOperations vaultOperations;
    
	public KeypairCertBo buildKeypairCert(String algo, KeypairPo parentKeypair, String subject,
			Date latestCertExpireDate) {
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
				PKCS10CertificationRequest csr = CertUtil.generateCSR(new X500Name(subject), keyPair.getPublic(),
						keyPair.getPrivate());
				csrStr = CertUtil.csrToPem(csr);
				if (algo.equals(ECC.getCode())
						|| X509CertConstant.SIGN_CERTIFICATE_TYPE == CryptoEnum.SignCertificateType.BC) {
					certStr = signCertificateRequestByBC(algo, parentKeypair, csr, true);
				} else if(X509CertConstant.SIGN_CERTIFICATE_TYPE == CryptoEnum.SignCertificateType.VAULT) {
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
    
	private String signCertificateRequestByBC(String algo, KeypairPo keypair,
			PKCS10CertificationRequest csr, boolean isEntityCert) throws Exception, CertificateEncodingException {
		PrivateKey rootPrivateKey = KeyUtil.privateKeyFromBase64(algo, keypair.getPrivateKey());
		byte[] rootCertBytes = CertUtil.certFromPem(keypair.getCertStr()).getEncoded();
		Certificate consortiumCert = CertUtil.generateCert(csr, rootPrivateKey, rootCertBytes, CertUtil.getNotBefore(),
				CertUtil.getNotAfter(X509CertConstant.EXPIRE_CONSORTIUM_CERT_IN_DYAS), isEntityCert);
		return CertUtil.certToPem(consortiumCert);
	}

	private String signCertificateRequestByVault(PKCS10CertificationRequest consortiumCsr, Date lastCertExpireDate) throws Exception {
		int lastCertLeftDays = 0;
		if(lastCertExpireDate != null) {
			lastCertLeftDays = DateUtils.betweenDays(Calendar.getInstance().getTime(), lastCertExpireDate);
			lastCertLeftDays = lastCertLeftDays > 0 ? lastCertLeftDays : 0;
		}
		
		VaultCertificateRequest request = VaultCertificateRequest.builder()
				.ttl(Duration.ofDays(lastCertLeftDays + X509CertConstant.EXPIRE_CONSORTIUM_CERT_IN_DYAS))
				.commonName(X509CertConstant.COMMON_NAME_CONSORTIUM_CERT).build();
		VaultSignCertificateRequestResponse certificateResponse = vaultOperations
				.opsForPki(X509CertConstant.VAULT_PKI_PATH).signCertificateRequest(
						X509CertConstant.VAULT_ROLE_ISSUE_CERTIFICATE, CertUtil.csrToPem(consortiumCsr), request);
		org.springframework.vault.support.Certificate consortiumCert = certificateResponse.getRequiredData();
		log.info("signCertificateRequestByVault resp:{}", JSON.toJSONString(consortiumCert));
		return generatePem(consortiumCert.getCertificate());
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
