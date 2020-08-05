package org.treegraph.baas.core.service.impl;

import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.cert.CertificateEncodingException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.Certificate;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.vault.core.VaultOperations;
import org.springframework.vault.core.VaultSysOperations;
import org.springframework.vault.support.VaultCertificateRequest;
import org.springframework.vault.support.VaultMount;
import org.springframework.vault.support.VaultResponse;
import org.springframework.vault.support.VaultSignCertificateRequestResponse;
import org.treegraph.baas.common.enums.IntTypeEnum;
import org.treegraph.baas.common.util.WebHelper;
import org.treegraph.baas.core.constant.ConsortiumConstant;
import org.treegraph.baas.core.constant.X509CertConstant;
import org.treegraph.baas.core.das.KeypairDas;
import org.treegraph.baas.core.das.NodeKeyDas;
import org.treegraph.baas.core.das.bo.NodeKeypairBo;
import org.treegraph.baas.core.domain.KeypairPo;
import org.treegraph.baas.core.domain.NodeKeyPo;
import org.treegraph.baas.core.domain.NodePo;
import org.treegraph.baas.core.enums.CryptoEnum;
import org.treegraph.baas.core.exception.AlertException;
import org.treegraph.baas.core.util.crypto.x509.CertUtil;
import org.treegraph.baas.core.util.crypto.x509.KeyUtil;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Splitter;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Validated
@Service
@AllArgsConstructor
@Slf4j
public class VaultKeypairBuildService {
	
    private KeypairDas keypairDas;
    
    private NodeKeyDas nodeKeyDas;
    
    private VaultOperations vaultOperations;
    
	public KeypairPo getRootKeypair(String algo) throws Exception{
		List<KeypairPo> rootKeypairs = keypairDas.findAll(ConsortiumConstant.DEFAULT_CONSORTIUM_ID,
				CryptoEnum.KeyType.CONSORTIUM.getCode(), algo);
		if(CollectionUtils.isNotEmpty(rootKeypairs)){
			return rootKeypairs.get(0);
		}
		
		KeyPair rootKeyPair = KeyUtil.generateKeyPair(algo);
		Certificate rootCA = CertUtil.generateRootCA(new X500Name(X509CertConstant.ROOT_CA_SUBJECT), rootKeyPair,
				CertUtil.getNotBefore(), CertUtil.getNotAfter(365 * 10));
		String rootPublicKey = KeyUtil.publicKeyToBase64(rootKeyPair.getPublic());
		String rootPrivateKey = KeyUtil.privateKeyToBase64(rootKeyPair.getPrivate());
		String rootCa = CertUtil.certToPem(rootCA);
		
		if(X509CertConstant.SIGN_CERTIFICATE_REQUEST == CryptoEnum.SignCertificateRequest.VAULT) {
			writeRootCaToVault(rootCa, KeyUtil.privateKeyToPem(rootKeyPair.getPrivate()));
		}
		
		LocalDateTime now = LocalDateTime.now();
		String operUser = WebHelper.getUserId();
		return KeypairPo.builder()
				.publicKey(rootPublicKey)
				.privateKey(rootPrivateKey)
				.csrStr(null)
				.certStr(rootCa)
				.algorithm(algo)
				.consortiumId(ConsortiumConstant.DEFAULT_CONSORTIUM_ID)
				.keyType(CryptoEnum.KeyType.CONSORTIUM.getCode())
				.deleteFlag(IntTypeEnum.FALSE.getValue())
				.createTime(now)
				.createBy(operUser)
				.updateTime(now)
				.updateBy(operUser)
				.build();
	}
	
	public KeypairPo getConsortiumKeyPair(String consortiumId, String algo, KeypairPo rootKeypair)
			throws NoSuchProviderException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, Exception {
		List<KeypairPo> consortiumKeypairs = keypairDas.findAll(consortiumId,
				CryptoEnum.KeyType.CONSORTIUM.getCode(), algo);
		if(CollectionUtils.isNotEmpty(consortiumKeypairs)){
			return consortiumKeypairs.get(0);
		}
		
		KeyPair consortiumKeyPair = KeyUtil.generateKeyPair(algo);
		PKCS10CertificationRequest consortiumCsr = CertUtil.generateCSR(
				new X500Name(X509CertConstant.CONSORTIUM_CERT_SUBJECT), consortiumKeyPair.getPublic(),
				consortiumKeyPair.getPrivate());
		String consortiumPublicKey = KeyUtil.publicKeyToBase64(consortiumKeyPair.getPublic());
		String consortiumPrivateKey = KeyUtil.privateKeyToBase64(consortiumKeyPair.getPrivate());
		String consortiumCsrStr = CertUtil.csrToPem(consortiumCsr);
		
		String consortiumCertStr = null;
		if(X509CertConstant.SIGN_CERTIFICATE_REQUEST == CryptoEnum.SignCertificateRequest.BC) {
			consortiumCertStr = signCertificateRequestByBC(algo, rootKeypair, consortiumCsr);
		} else if(X509CertConstant.SIGN_CERTIFICATE_REQUEST == CryptoEnum.SignCertificateRequest.VAULT) {
			consortiumCertStr = signCertificateRequestByVault(consortiumCsr);
		} else {
			throw new RuntimeException("not support sign certificate request");
		}
		
		LocalDateTime now = LocalDateTime.now();
		String operUser = WebHelper.getUserId();
		return KeypairPo.builder()
				.publicKey(consortiumPublicKey)
				.privateKey(consortiumPrivateKey)
				.csrStr(consortiumCsrStr)
				.certStr(consortiumCertStr)
				.algorithm(algo)
				.consortiumId(consortiumId)
				.keyType(CryptoEnum.KeyType.CONSORTIUM.getCode())
				.deleteFlag(IntTypeEnum.FALSE.getValue())
				.createTime(now)
				.createBy(operUser)
				.updateTime(now)
				.updateBy(operUser)
				.build();
	}

	public NodeKeypairBo getNodeKeypair(String consortiumId, String algo, NodePo node) {
		List<NodeKeyPo> nodeKeys = nodeKeyDas.findAll(consortiumId, node.getId());
		if(CollectionUtils.isNotEmpty(nodeKeys)) {
			List<Long> keypairIds = nodeKeys.stream().map(item -> item.getKeypairId()).collect(Collectors.toList());
			List<KeypairPo> nodeKeypairs = keypairDas.findByIds(keypairIds);
			Optional<KeypairPo> keypairOp = nodeKeypairs.stream().filter(item -> item.getAlgorithm().equalsIgnoreCase(algo)).findFirst();
			if(keypairOp.isPresent()){
				return NodeKeypairBo.builder().keypair(keypairOp.get()).node(node).build();
			}
		}
		
		KeyPair keyPair = null;
		String nodePublicKey = null;
		String nodePrivateKey = null;
		try {
			keyPair = KeyUtil.generateKeyPair(algo);
			nodePublicKey = KeyUtil.publicKeyToBase64(keyPair.getPublic());
			nodePrivateKey = KeyUtil.privateKeyToBase64(keyPair.getPrivate());
		} catch(Exception e) {
			throw new AlertException("get node keypair error!");
		}
		
		LocalDateTime now = LocalDateTime.now();
		String operUser = WebHelper.getUserId();
		KeypairPo keypair = KeypairPo.builder()
				.publicKey(nodePublicKey)
				.privateKey(nodePrivateKey)
				.csrStr(null)
				.certStr(null)
				.algorithm(algo)
				.consortiumId(consortiumId)
				.keyType(CryptoEnum.KeyType.NODE.getCode())
				.deleteFlag(IntTypeEnum.FALSE.getValue())
				.createTime(now)
				.createBy(operUser)
				.updateTime(now)
				.updateBy(operUser)
				.build();
		NodeKeyPo nodeKey = NodeKeyPo.builder()
				.nodeId(node.getId())
				.keypairId(null)
				.consortiumId(consortiumId)
				.deleteFlag(IntTypeEnum.FALSE.getValue())
				.createTime(now)
				.createBy(operUser)
				.updateTime(now)
				.updateBy(operUser)
				.build();
		
		return NodeKeypairBo.builder().keypair(keypair).nodeKey(nodeKey).node(node).build();
	}
	
	private void vaultMount() {
		VaultSysOperations adminOperations = vaultOperations.opsForSys();
		if (adminOperations.getMounts().containsKey(X509CertConstant.VAULT_PKI_PATH + "/")) {
			return;
		}

		VaultMount mount = VaultMount.builder().type("pki")
				.config(Collections.singletonMap("max_lease_ttl", "87600h")).description("bass pki").build();
		adminOperations.mount(X509CertConstant.VAULT_PKI_PATH, mount);

		Map<String, VaultMount> mounts = adminOperations.getMounts();
		log.info("vaultMounts resp:{}", JSON.toJSONString(mounts));
		Assert.isTrue(mounts.containsKey(X509CertConstant.VAULT_PKI_PATH + "/"), "vault mount pki path error");
	}
	
	private void writeRootCaToVault(String certPem, String keyPem) {
		vaultMount();
		
		Map<String, String> pembundle = Collections.singletonMap("pem_bundle", certPem + keyPem);
		VaultResponse writeCaToVaultResponse = vaultOperations.write(X509CertConstant.VAULT_PKI_PATH + "/config/ca",
				pembundle);
		log.info("writeCaResponse resp:{}", JSON.toJSONString(writeCaToVaultResponse));

		Map<String, String> role = new HashMap<String, String>();
		role.put("allowed_domains", "localhost,conflux-chain.org");
		role.put("allow_subdomains", "true");
		role.put("allow_localhost", "true");
		role.put("allow_ip_sans", "true");
		role.put("max_ttl", "87600h");
		VaultResponse writeRoleToVaultResponse = vaultOperations.write(
				X509CertConstant.VAULT_PKI_PATH + "/roles/" + X509CertConstant.VAULT_ROLE_ISSUE_CERTIFICATE, role);
		log.info("writeRoleResponse resp:{}", JSON.toJSONString(writeRoleToVaultResponse));
	}

	private String signCertificateRequestByBC(String algo, KeypairPo rootKeypair,
			PKCS10CertificationRequest consortiumCsr) throws Exception, CertificateEncodingException {
		PrivateKey rootPrivateKey = KeyUtil.privateKeyFromBase64(algo, rootKeypair.getPrivateKey());
		byte[] rootCertBytes = CertUtil.certFromPem(rootKeypair.getCertStr()).getEncoded();
		Certificate consortiumCert = CertUtil.generateCert(consortiumCsr, rootPrivateKey, rootCertBytes,
				CertUtil.getNotBefore(), CertUtil.getNotAfter(X509CertConstant.CONSORTIUM_CERT_EXPIRE_DYAS));
		return CertUtil.certToPem(consortiumCert);
	}

	private String signCertificateRequestByVault(PKCS10CertificationRequest consortiumCsr) throws Exception {
		VaultCertificateRequest request = VaultCertificateRequest.builder()
				.ttl(Duration.ofDays(X509CertConstant.CONSORTIUM_CERT_EXPIRE_DYAS))
				.commonName(X509CertConstant.CONSORTIUM_CERT_COMMON_NAME).build();
		VaultSignCertificateRequestResponse certificateResponse = vaultOperations
				.opsForPki(X509CertConstant.VAULT_PKI_PATH).signCertificateRequest(
						X509CertConstant.VAULT_ROLE_ISSUE_CERTIFICATE, CertUtil.csrToPem(consortiumCsr), request);
		org.springframework.vault.support.Certificate consortiumCert = certificateResponse.getRequiredData();
		log.info("signCertificateRequestByVault resp:{}", JSON.toJSONString(consortiumCert));
		return generatePem(consortiumCert.getCertificate());
	}
	
	private static String generatePem(String base64Str) {  
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
