package org.treegraph.baas.core.service.impl;

import static org.treegraph.baas.core.constant.X509CertConstant.ALGO_ROOT;
import static org.treegraph.baas.core.constant.X509CertConstant.ALGO_CONS;
import static org.treegraph.baas.core.constant.X509CertConstant.ALGO_NODE;
import static org.treegraph.baas.core.constant.X509CertConstant.ALGO_RPC;
import static org.treegraph.baas.core.constant.X509CertConstant.SUBJECT_CONS_CERT;
import static org.treegraph.baas.core.constant.X509CertConstant.SUBJECT_RPC_CERT;
import static org.treegraph.baas.core.util.crypto.x509.KeyUtil.privateKeyFromBase64;
import static org.treegraph.baas.core.util.crypto.x509.KeyUtil.privateKeyToPem;

import java.security.KeyPair;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.Certificate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.vault.core.VaultOperations;
import org.springframework.vault.core.VaultSysOperations;
import org.springframework.vault.support.VaultMount;
import org.springframework.vault.support.VaultResponse;
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
import org.treegraph.baas.core.service.bo.GRpcKeypairCertBo;
import org.treegraph.baas.core.service.bo.KeypairCertBo;
import org.treegraph.baas.core.util.LogSkip;
import org.treegraph.baas.core.util.crypto.x509.CertUtil;
import org.treegraph.baas.core.util.crypto.x509.KeyUtil;
import org.treegraph.baas.core.util.crypto.x509.KeypairCertUtil;

import com.alibaba.fastjson.JSON;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Validated
@Service
@AllArgsConstructor
@Slf4j
public class VaultKeypairBuildService {
	
    private final KeypairDas keypairDas;
    
    private final NodeKeyDas nodeKeyDas;
    
    private final VaultOperations vaultOperations;
    
    private final KeypairCertUtil keypairCertUtil;

	@LogSkip
	public KeypairPo getRootKeypair() throws Exception{
		List<KeypairPo> rootKeypairs = keypairDas.findAll(ConsortiumConstant.DEFAULT_CONSORTIUM_ID,
				CryptoEnum.KeyType.CONSORTIUM.getCode(), ALGO_ROOT);
		if(CollectionUtils.isNotEmpty(rootKeypairs)){
			return rootKeypairs.get(0);
		}
		
		KeyPair rootKeyPair = KeyUtil.generateKeyPair(ALGO_ROOT);
		Certificate rootCA = CertUtil.generateRootCA(new X500Name(X509CertConstant.SUBJECT_ROOT_CA), rootKeyPair,
				CertUtil.getNotBefore(), CertUtil.getNotAfter(365 * 10));
		String rootPublicKey = KeyUtil.publicKeyToBase64(rootKeyPair.getPublic());
		String rootPrivateKey = KeyUtil.privateKeyToBase64(rootKeyPair.getPrivate());
		String rootCa = CertUtil.certToPem(rootCA);
		
		if(X509CertConstant.DEFAULT_CERT_SIGN_TYPE == CryptoEnum.CertificateSignType.VAULT) {
			writeRootCaToVault(rootCa, KeyUtil.privateKeyToPem(rootKeyPair.getPrivate()));
		}
		
		LocalDateTime now = LocalDateTime.now();
		String operUser = WebHelper.getUserId();
		return KeypairPo.builder()
				.publicKey(rootPublicKey)
				.privateKey(rootPrivateKey)
				.csrStr(null)
				.certStr(rootCa)
				.algorithm(ALGO_ROOT)
				.consortiumId(ConsortiumConstant.DEFAULT_CONSORTIUM_ID)
				.keyType(CryptoEnum.KeyType.CONSORTIUM.getCode())
				.deleteFlag(IntTypeEnum.FALSE.getValue())
				.createTime(now)
				.createBy(operUser)
				.updateTime(now)
				.updateBy(operUser)
				.build();
	}

	@LogSkip
	public KeypairPo getConsortiumKeyPair(String consortiumId, KeypairPo rootKeypair) {
		List<KeypairPo> consortiumKeypairs = keypairDas.findAll(consortiumId,
				CryptoEnum.KeyType.CONSORTIUM.getCode(), ALGO_CONS);
		if(CollectionUtils.isNotEmpty(consortiumKeypairs)){
			return consortiumKeypairs.get(0);
		}
		
		return geneNewConsortiumKeyPair(consortiumId, rootKeypair, null);
	}

	@LogSkip
	public KeypairPo geneNewConsortiumKeyPair(String consortiumId, KeypairPo rootKeypair,
			Date lastCertExpireDate) {
		KeypairCertBo keypairCert = keypairCertUtil.buildKeypairCert(ALGO_CONS, SUBJECT_CONS_CERT, lastCertExpireDate,
				null, CryptoEnum.KeyType.CONSORTIUM, rootKeypair);
		LocalDateTime now = LocalDateTime.now();
		String operUser = WebHelper.getUserId();
		return KeypairPo.builder()
				.publicKey(keypairCert.getPublicKey())
				.privateKey(keypairCert.getPrivateKey())
				.csrStr(keypairCert.getCsrStr())
				.certStr(keypairCert.getCertStr())
				.algorithm(keypairCert.getAlgorithm())
				.consortiumId(consortiumId)
				.keyType(CryptoEnum.KeyType.CONSORTIUM.getCode())
				.deleteFlag(IntTypeEnum.FALSE.getValue())
				.createTime(now)
				.createBy(operUser)
				.updateTime(now)
				.updateBy(operUser)
				.build();
	}

	@LogSkip
	public NodeKeypairBo getNodeKeypairBo(String consortiumId, NodePo node) {
		List<NodeKeyPo> nodeKeys = nodeKeyDas.findAll(consortiumId, node.getId());
		if(CollectionUtils.isNotEmpty(nodeKeys)) {
			List<Long> keypairIds = nodeKeys.stream().map(NodeKeyPo::getKeypairId).collect(Collectors.toList());
			List<KeypairPo> nodeKeypairs = keypairDas.findByIds(keypairIds);
			Optional<KeypairPo> keypairOp = nodeKeypairs.stream()
					.filter(item -> item.getAlgorithm().equalsIgnoreCase(ALGO_NODE)).findFirst();
			if(keypairOp.isPresent()){
				return NodeKeypairBo.builder().keypair(keypairOp.get()).node(node).build();
			}
		}
		
		KeypairCertBo keypairCert = keypairCertUtil.buildKeypair(ALGO_NODE);
		LocalDateTime now = LocalDateTime.now();
		String operUser = WebHelper.getUserId();
		KeypairPo keypair = KeypairPo.builder()
				.publicKey(keypairCert.getPublicKey())
				.privateKey(keypairCert.getPrivateKey())
				.algorithm(keypairCert.getAlgorithm())
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

	@LogSkip
	public GRpcKeypairCertBo geneNewRpcKeypair(String consortiumId, KeypairPo consKeypair, Date lastCertExpireDate,
			List<String> sanList) throws Exception {
		KeypairCertBo keypairCert = keypairCertUtil.buildKeypairCert(ALGO_RPC, SUBJECT_RPC_CERT, lastCertExpireDate,
				sanList, CryptoEnum.KeyType.RPC, consKeypair);
		String rpcCert = keypairCert.getCertStr();
		String rpcPrivate = privateKeyToPem(privateKeyFromBase64(ALGO_RPC, keypairCert.getPrivateKey()));
		//log.info("getGrpcKeypair grpcCert:{},grpcPrivate:{}", rpcCert, rpcPrivate);
		return GRpcKeypairCertBo.builder().consId(consortiumId).cert(rpcCert).privateKey(rpcPrivate).build();
	}
	
//	public GRpcKeypairCertBo geneNewRpcKeypair(String consortiumId, KeypairPo consKeypair, Date lastCertExpireDate)
//			throws Exception {
//		KeypairCertBo keypairCert = keypairCertUtil.buildKeypairCert(ALGO_RPC, SUBJECT_RPC_CERT, lastCertExpireDate,
//				consKeypair);
//		String rpcCert = keypairCert.getCertStr();
//		String rpcPrivate = privateKeyToPem(privateKeyFromBase64(ALGO_RPC, keypairCert.getPrivateKey()));
//		log.info("getGrpcKeypair grpcCert:{},grpcPrivate:{}", rpcCert, rpcPrivate);
//		return GRpcKeypairCertBo.builder().consId(consortiumId).cert(rpcCert).privateKey(rpcPrivate).build();
//	}

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
		
		Map<String, String> pemBundle = Collections.singletonMap("pem_bundle", certPem + keyPem);
		VaultResponse writeCaToVaultResponse = vaultOperations.write(X509CertConstant.VAULT_PKI_PATH + "/config/ca",
				pemBundle);
		log.info("writeCaResponse resp:{}", JSON.toJSONString(writeCaToVaultResponse));

		Map<String, String> role = new HashMap<>();
		role.put("allowed_domains", "localhost,conflux-chain.org");
		role.put("allow_subdomains", "true");
		role.put("allow_localhost", "true");
		role.put("allow_ip_sans", "true");
		role.put("max_ttl", "87600h");
		VaultResponse writeRoleToVaultResponse = vaultOperations.write(
				X509CertConstant.VAULT_PKI_PATH + "/roles/" + X509CertConstant.VAULT_ROLE_ISSUE_CERTIFICATE, role);
		log.info("writeRoleResponse resp:{}", JSON.toJSONString(writeRoleToVaultResponse));
	}
}
