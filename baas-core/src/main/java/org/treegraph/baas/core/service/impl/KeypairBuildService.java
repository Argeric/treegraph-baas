package org.treegraph.baas.core.service.impl;

import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.time.LocalDateTime;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.Certificate;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.treegraph.baas.common.enums.IntTypeEnum;
import org.treegraph.baas.common.util.WebHelper;
import org.treegraph.baas.core.constant.ConsortiumConstant;
import org.treegraph.baas.core.constant.X509CertConstant;
import org.treegraph.baas.core.das.KeypairDas;
import org.treegraph.baas.core.das.bo.NodeKeypairBo;
import org.treegraph.baas.core.domain.KeypairPo;
import org.treegraph.baas.core.domain.NodeKeyPo;
import org.treegraph.baas.core.domain.NodePo;
import org.treegraph.baas.core.enums.CryptoEnum;
import org.treegraph.baas.core.util.crypto.x509.CertUtil;
import org.treegraph.baas.core.util.crypto.x509.KeyUtil;

import lombok.AllArgsConstructor;

@Validated
@Service
@AllArgsConstructor
public class KeypairBuildService {
	
    private KeypairDas keypairDas;
    
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
		
		PrivateKey rootPrivateKey = KeyUtil.privateKeyFromBase64(algo, rootKeypair.getPrivateKey());
		byte[] rootCertBytes = CertUtil.certFromPem(rootKeypair.getCertStr()).getEncoded();
		Certificate consortiumCert = CertUtil.generateCert(consortiumCsr, rootPrivateKey, rootCertBytes,
				CertUtil.getNotBefore(), CertUtil.getNotAfter(365), false);
		String consortiumCertStr = CertUtil.certToPem(consortiumCert);
		
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

	public NodeKeypairBo getNodeKeypair(String consortiumId, String algo, NodePo node)
			throws NoSuchProviderException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, Exception {
		List<KeypairPo> nodeKeypairs = keypairDas.findAll(consortiumId,
				CryptoEnum.KeyType.NODE.getCode(), algo);
		if(CollectionUtils.isNotEmpty(nodeKeypairs)){
			return NodeKeypairBo.builder().keypair(nodeKeypairs.get(0)).build();
		}
		
		KeyPair keyPair = KeyUtil.generateKeyPair(algo);
		String nodePublicKey = KeyUtil.publicKeyToBase64(keyPair.getPublic());
		String nodePrivateKey = KeyUtil.privateKeyToBase64(keyPair.getPrivate());
		
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
		
		return NodeKeypairBo.builder().keypair(keypair).nodeKey(nodeKey).build();
	}
}
