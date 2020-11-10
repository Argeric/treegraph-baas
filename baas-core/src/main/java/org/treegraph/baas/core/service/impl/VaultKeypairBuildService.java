package org.treegraph.baas.core.service.impl;

import static org.treegraph.baas.core.util.crypto.x509.KeyUtil.privateKeyFromBase64;
import static org.treegraph.baas.core.util.crypto.x509.KeyUtil.privateKeyToPem;

import com.alibaba.fastjson.JSON;
import java.security.PrivateKey;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
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
import org.treegraph.baas.core.enums.CryptoEnum.KeyType;
import org.treegraph.baas.core.service.bo.GRpcKeypairCertBo;
import org.treegraph.baas.core.service.bo.KeypairCertBo;
import org.treegraph.baas.core.util.LogSkip;
import org.treegraph.baas.core.util.crypto.x509.KeyUtil;
import org.treegraph.baas.core.util.crypto.x509.KeypairCertUtil;

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
	public KeypairPo getRootKeypair() {
		List<KeypairPo> rootKeypairList = keypairDas.findAll(ConsortiumConstant.DEFAULT_CONSORTIUM_ID,
				KeyType.ROOT_CA.getCode(), KeyType.ROOT_CA.getAlgo().getCode());
		if(CollectionUtils.isNotEmpty(rootKeypairList)){
			return rootKeypairList.get(0);
		}

		KeypairCertBo keypairCert = keypairCertUtil.buildRootKeypairCert();
		String rootPublicKey = keypairCert.getPublicKey();
		String rootPrivateKey = keypairCert.getPrivateKey();
		String rootCa = keypairCert.getCertStr();

		LocalDateTime now = LocalDateTime.now();
		String operUser = WebHelper.getUserId();
		return KeypairPo.builder()
				.publicKey(rootPublicKey)
				.privateKey(rootPrivateKey)
				.csrStr(null)
				.certStr(rootCa)
				.algorithm(KeyType.ROOT_CA.getAlgo().getCode())
				.consortiumId(ConsortiumConstant.DEFAULT_CONSORTIUM_ID)
				.keyType(KeyType.ROOT_CA.getCode())
				.deleteFlag(IntTypeEnum.FALSE.getValue())
				.createTime(now)
				.createBy(operUser)
				.updateTime(now)
				.updateBy(operUser)
				.build();
	}

	@LogSkip
	public KeypairPo getConsortiumKeyPair(String consortiumId, KeypairPo rootKeypair)
			throws Exception {
		List<KeypairPo> consortiumKeypairList = keypairDas.findAll(consortiumId,
				CryptoEnum.KeyType.CONSORTIUM.getCode(), KeyType.CONSORTIUM.getAlgo().getCode());
		if(CollectionUtils.isNotEmpty(consortiumKeypairList)){
			return consortiumKeypairList.get(0);
		}
		
		return geneNewConsortiumKeyPair(consortiumId, rootKeypair, null);
	}

	@LogSkip
	public KeypairPo geneNewConsortiumKeyPair(String consortiumId, KeypairPo rootKeypair,
			Date lastCertExpireDate) throws Exception {
		KeypairCertBo keypairCert = keypairCertUtil.buildConsKeypairCert(lastCertExpireDate, rootKeypair);
		if(KeyType.CONSORTIUM.getSignType() == CryptoEnum.CertificateSignType.VAULT) {
			PrivateKey privateKey = KeyUtil.privateKeyFromBase64(KeyType.CONSORTIUM.getAlgo().getCode(), keypairCert.getPrivateKey());
			submitCaToVault(consortiumId, keypairCert.getCertStr(), KeyUtil.privateKeyToPem(privateKey));
		}

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
			List<KeypairPo> nodeKeypairList = keypairDas.findByIds(keypairIds);
			Optional<KeypairPo> keypairOp = nodeKeypairList.stream()
					.filter(item -> item.getAlgorithm().equalsIgnoreCase(KeyType.NODE.getAlgo().getCode())).findFirst();
			if(keypairOp.isPresent()){
				return NodeKeypairBo.builder().keypair(keypairOp.get()).node(node).build();
			}
		}
		
		KeypairCertBo keypairCert = keypairCertUtil.buildNodeKeypairCert();
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
		KeypairCertBo keypairCert = keypairCertUtil.buildRpcKeypairCert(lastCertExpireDate, sanList, consortiumId, consKeypair);
		String rpcCert = keypairCert.getCertStr();
		String rpcPrivate = privateKeyToPem(privateKeyFromBase64(KeyType.RPC.getAlgo().getCode(), keypairCert.getPrivateKey()));
		return GRpcKeypairCertBo.builder().consId(consortiumId).cert(rpcCert).privateKey(rpcPrivate).build();
	}

	private void submitCaToVault(String pkiPath, String certPem, String keyPem) {
		mountToVault(pkiPath);

		Map<String, String> pemBundle = Collections.singletonMap("pem_bundle", certPem + keyPem);
		VaultResponse writeCaToVaultResponse = vaultOperations.write(pkiPath + "config/ca",
				pemBundle);
		log.info("writeCaResponse resp:{}", JSON.toJSONString(writeCaToVaultResponse));

		Map<String, String> role = new HashMap<>();
		role.put("allowed_domains", "localhost,baas.conflux-chain.org");
		role.put("allow_subdomains", "true");
		role.put("allow_localhost", "true");
		role.put("allow_ip_sans", "true");
		role.put("max_ttl", "87600h");
		VaultResponse writeRoleToVaultResponse = vaultOperations.write(
				pkiPath + "roles/" + X509CertConstant.VAULT_ROLE_ISSUE_CERTIFICATE, role);
		log.info("writeRoleResponse resp:{}", JSON.toJSONString(writeRoleToVaultResponse));
	}

	private void mountToVault(String pkiPath) {
		VaultSysOperations adminOperations = vaultOperations.opsForSys();
		if (adminOperations.getMounts().containsKey(pkiPath)) {
			return;
		}

		VaultMount mount = VaultMount.builder().type("pki")
				.config(Collections.singletonMap("max_lease_ttl", "87600h")).description("bass pki").build();
		adminOperations.mount(pkiPath, mount);

		Map<String, VaultMount> mounts = adminOperations.getMounts();
		log.info("vaultMount path:{},resp:{}", pkiPath, JSON.toJSONString(mounts));
		Assert.isTrue(mounts.containsKey(pkiPath), "vault mount pki path error");
	}
}
