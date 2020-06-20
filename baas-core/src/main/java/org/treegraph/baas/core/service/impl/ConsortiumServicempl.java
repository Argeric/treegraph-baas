package org.treegraph.baas.core.service.impl;

import java.io.OutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.Certificate;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.treegraph.baas.api.dto.ConsAdminInfo;
import org.treegraph.baas.api.dto.ConsDetailInfo;
import org.treegraph.baas.api.dto.ConsInfo;
import org.treegraph.baas.api.dto.ListRequest;
import org.treegraph.baas.api.dto.NodeInfo;
import org.treegraph.baas.api.dto.Role;
import org.treegraph.baas.api.dto.request.AddNodesRequest;
import org.treegraph.baas.api.dto.request.CreateBlockchainRequest;
import org.treegraph.baas.api.dto.request.CreateConsAdminRequest;
import org.treegraph.baas.api.dto.request.CreateConsRequest;
import org.treegraph.baas.api.dto.request.DeleteConsRequest;
import org.treegraph.baas.api.dto.request.DownloadDeployScriptRequest;
import org.treegraph.baas.api.dto.request.ModifyConsRequest;
import org.treegraph.baas.api.dto.request.QueryConsDetailRequest;
import org.treegraph.baas.api.dto.request.QueryConsRequest;
import org.treegraph.baas.api.dto.response.ListConsResponse;
import org.treegraph.baas.common.enums.IntTypeEnum;
import org.treegraph.baas.common.util.WebHelper;
import org.treegraph.baas.core.constant.ConsortiumConstant;
import org.treegraph.baas.core.constant.SplitterConstant;
import org.treegraph.baas.core.constant.X509CertConstant;
import org.treegraph.baas.core.das.BlockchainDas;
import org.treegraph.baas.core.das.ConsortiumDas;
import org.treegraph.baas.core.das.KeypairDas;
import org.treegraph.baas.core.das.NodeDas;
import org.treegraph.baas.core.das.NodeKeyDas;
import org.treegraph.baas.core.das.RoleDas;
import org.treegraph.baas.core.das.UserInfoDas;
import org.treegraph.baas.core.domain.BlockChainPo;
import org.treegraph.baas.core.domain.ConsortiumPo;
import org.treegraph.baas.core.domain.KeypairPo;
import org.treegraph.baas.core.domain.NodeKeyPo;
import org.treegraph.baas.core.domain.NodePo;
import org.treegraph.baas.core.domain.RolePo;
import org.treegraph.baas.core.domain.UserInfoPo;
import org.treegraph.baas.core.domain.UserOrganizationPo;
import org.treegraph.baas.core.domain.UserRolePo;
import org.treegraph.baas.core.enums.BlockchainEnum;
import org.treegraph.baas.core.enums.ConsortiumEnum;
import org.treegraph.baas.core.enums.ConsortiumEnum.EstablishStep;
import org.treegraph.baas.core.enums.CryptoEnum;
import org.treegraph.baas.core.enums.NodeEnum;
import org.treegraph.baas.core.enums.UserEnum;
import org.treegraph.baas.core.exception.AlertException;
import org.treegraph.baas.core.service.ConsortiumService;
import org.treegraph.baas.core.service.bo.OrgBo;
import org.treegraph.baas.core.service.bo.RoleInfoBo;
import org.treegraph.baas.core.service.bo.UserInfoBo;
import org.treegraph.baas.core.util.CollectionUtils;
import org.treegraph.baas.core.util.DateTimeUtils;
import org.treegraph.baas.core.util.LogSkip;
import org.treegraph.baas.core.util.crypto.x509.CertUtil;
import org.treegraph.baas.core.util.crypto.x509.EncryptUtil;
import org.treegraph.baas.core.util.crypto.x509.KeyUtil;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Functions;
import com.google.common.collect.Lists;

import lombok.AllArgsConstructor;

@Validated
@Service
@AllArgsConstructor
public class ConsortiumServicempl implements ConsortiumService {
    
    private ConsortiumDas consortiumDas;
    
    private UserInfoDas userInfoDas;
    
    private RoleDas roleDas;
    
    private BlockchainDas blockchainDas;
    
    private NodeDas nodeDas;
    
    private KeypairDas keypairDas;
    
    private NodeKeyDas nodeKeyDas;
    
    private BuildService buildService;
   
	public ListConsResponse listCons(ListRequest req) {
		PageHelper.startPage(req.getCurrent(), req.getPageSize());
		List<ConsortiumPo> consList = consortiumDas.findAll(null);
		PageInfo<ConsortiumPo> pageInfo = new PageInfo<>(consList);
		
		List<ConsortiumPo> conses = pageInfo.getList();
		List<ConsInfo> consInfoList = conses.stream().map(item -> ConsInfo.builder()
				.id(item.getConsortiumId())
				.name(item.getName())
				.remark(item.getDescription())
				.contact(item.getContactName())
				.phone(item.getContactPhone())
				.email(item.getContactEmail())
				.avatar(item.getAvatar() != null ? new String(item.getAvatar()) : null)
				.createUser(item.getCreateBy())
				.createTime(DateTimeUtils.formatTime(item.getCreateTime(), DateTimeUtils.FORMAT_yyyyMMddHHmmss))
				.status(item.getStatus())
				.step(item.getEstablishStep())
				.build()).collect(Collectors.toList());
		
		ListConsResponse listConsResponse = ListConsResponse.builder()
				.current(req.getCurrent())
				.pageSize(req.getPageSize())
				.total(pageInfo.getTotal())
				.list(consInfoList)
				.build();
		
		return listConsResponse;
	}
	
	public List<ConsInfo> queryCons(QueryConsRequest req) {
		List<ConsortiumPo> consList = consortiumDas.findAll(req.getId());
		return consList.stream().map(item -> ConsInfo.builder()
				.id(item.getConsortiumId())
				.name(item.getName())
				.status(item.getStatus())
				.step(item.getEstablishStep())
				.remark(item.getDescription())
				.build()).collect(Collectors.toList());
	}
	
	public List<ConsDetailInfo> queryConsDetail(QueryConsDetailRequest req){
		List<ConsortiumPo> consList = consortiumDas.findAll(req.getId());
		List<String> consIds = consList.stream().map(ConsortiumPo::getConsortiumId).collect(Collectors.toList());
		
		List<BlockChainPo> blockchains = blockchainDas.findByConsIds(consIds);
		Map<String, BlockChainPo> blockchainMap = blockchains.stream()
				.collect(Collectors.toMap(BlockChainPo::getConsortiumId, Functions.identity()));
		
		List<String> chainIds = blockchains.stream().map(BlockChainPo::getId).collect(Collectors.toList());
		List<NodePo> nodes = nodeDas.findByChainIds(chainIds);
		Map<String, List<NodePo>> nodeMap = nodes.stream().collect(Collectors.groupingBy(NodePo::getBlockchainId));
		
		return consList.stream().map(item -> {
			BlockChainPo blockChain = blockchainMap.get(item.getConsortiumId());
			String blockchainId = blockChain != null ? blockChain.getId() : null;
			String blockchainName = blockChain != null ? blockChain.getBlockchainName() : null;
			List<NodeInfo> nodeInfoList = Lists.newArrayList();
			Optional.ofNullable(nodeMap.get(blockchainId)).ifPresent(nodeList -> nodeList.stream()
					.forEach(node -> nodeInfoList.add(NodeInfo.builder().ip(node.getNodeIp())
							.port(node.getNodePort() + "").org(node.getOrganizationName()).status(node.getStatus())
							.createTime(DateTimeUtils.formatTime(node.getCreateTime(), DateTimeUtils.FORMAT_yyyyMMddHHmmss))
							.build())));

			return ConsDetailInfo.builder()
					.id(item.getConsortiumId())
					.name(item.getName())
					.blockchainId(blockchainId)
					.blockchainName(blockchainName)
					.nodes(nodeInfoList)
					.build();
		}).collect(Collectors.toList());
	}
	
	public String createCons(CreateConsRequest req) {
		List<ConsortiumPo> consList = consortiumDas.findByName(req.getName());
		Assert.isTrue(CollectionUtils.isEmpty(consList), "新创建联盟名称已被使用["+req.getName()+"]");
		
		LocalDateTime now = LocalDateTime.now();
		String oper = WebHelper.getUserId();
		ConsortiumPo cons = ConsortiumPo.builder()
				.consortiumId(UUID.randomUUID().toString())
				.name(req.getName())
				.avatar(req.getAvatar() != null ? req.getAvatar().getBytes() : null)
				.description(req.getRemark())
				.contactName(req.getContact())
				.contactEmail(req.getEmail())
				.contactPhone(req.getPhone())
				.status(ConsortiumEnum.Status.ESTABLISHING.getCode())
				.establishStep(ConsortiumEnum.EstablishStep.TO_ESTAB_CONS_ADMIN.getCode())
				.deleteFlag(IntTypeEnum.FALSE.getValue())
				.createTime(now)
				.createBy(oper)
				.updateTime(now)
				.updateBy(oper)
				.build();
		consortiumDas.createConsortium(cons);
		
		return cons.getConsortiumId();
	}

	public boolean modifyCons(ModifyConsRequest req) {
		List<ConsortiumPo> consList = consortiumDas.findByName(req.getName());
		List<ConsortiumPo> repeatNameConsList = consList.stream().filter(item -> !item.getConsortiumId().equals(req.getId()))
				.collect(Collectors.toList());
		Assert.isTrue(CollectionUtils.isEmpty(repeatNameConsList), "新创建联盟名称已被使用["+req.getName()+"]");
		
		ConsortiumPo cons = ConsortiumPo.builder()
				.consortiumId(req.getId())
				.name(req.getName())
				.description(req.getRemark())
				.contactName(req.getContact())
				.contactPhone(req.getPhone())
				.contactEmail(req.getEmail())
				.avatar(req.getAvatar() != null ? req.getAvatar().getBytes() : null)
				.updateTime(LocalDateTime.now())
				.updateBy(WebHelper.getUserId())
				.build();
		
		return consortiumDas.modifyCons(cons) > 0;
	}

	public boolean deleteCons(DeleteConsRequest req) {
		ConsortiumPo cons = ConsortiumPo.builder()
				.consortiumId(req.getId())
				.deleteFlag(IntTypeEnum.TRUE.getValue())
				.updateTime(LocalDateTime.now())
				.updateBy( WebHelper.getUserId())
				.build();
		return consortiumDas.modifyCons(cons) > 0;
	}
	
	public boolean createConsAdmin(CreateConsAdminRequest req) {
		RoleInfoBo roleBo = null;
		Optional<RolePo> roleOp = roleDas.findConsAdminRole(req.getConsId());
		if (!roleOp.isPresent()) {
			List<String> reqPermissions = UserEnum.PermissionPoint.getForConsAdmin().stream()
					.map(item -> item.getCode()).collect(Collectors.toList());
			roleBo = buildService.buildRole(UserEnum.Role.CONSORTIUM_ADMIN.getCode(),
					UserEnum.Role.CONSORTIUM_ADMIN.getDesc(), req.getConsId(), reqPermissions);
		}
		
		String roleId = roleBo != null ? roleBo.getRole().getId() : roleOp.get().getId();
		List<UserInfoBo> userBos = req.getUsers().stream().filter(item -> item.getUserId() == null).map(item -> {
			OrgBo orgBo = OrgBo.builder().consortiumId(req.getConsId()).organizationName(item.getOrg()).build();
			return buildService.buildUser(item.getAccount(), item.getName(), item.getPhone(), item.getEmail(),
					item.getPwd(), Lists.newArrayList(Role.builder().consId(req.getConsId()).roleId(roleId).build()),
					Lists.newArrayList(orgBo));
		}).collect(Collectors.toList());
		
		List<UserInfoPo> users = Lists.newArrayList();
		List<UserRolePo> userRoles = Lists.newArrayList();
		List<UserOrganizationPo> userOrgs = Lists.newArrayList();
		if(CollectionUtils.isNotEmpty(userBos)) {
			userBos.stream().forEach(item -> {
				users.add(item.getUser());
				userRoles.addAll(item.getUserRoles());
				userOrgs.addAll(item.getUserOrgs());
			});
		}
		
		List<String> existUserIds = req.getUsers().stream().filter(item -> item.getUserId() != null)
				.map(ConsAdminInfo::getUserId).collect(Collectors.toList());
		List<UserRolePo> dbUserRoles = roleDas.findUserRolesForConsAdmin(existUserIds, roleId);
		Set<String> dbUserIdSet = dbUserRoles.stream().map(UserRolePo::getUserId).collect(Collectors.toSet());
		existUserIds.stream().filter(item -> !dbUserIdSet.contains(item))
				.forEach(item -> userRoles.addAll(buildService.buildUserRoles(item,
						Lists.newArrayList(Role.builder().consId(req.getConsId()).roleId(roleId).build()))));
		
		ConsortiumPo cons = buildService.buildToUpdateCons(req.getConsId(), EstablishStep.TO_ESTAB_BLOCKCHAIN);
		
		return userInfoDas.createConsAdmin(roleBo != null ? roleBo.getRole() : null,
				roleBo != null ? roleBo.getRolePermissionList() : null, users, userRoles, userOrgs, cons);
	}

	public boolean createBlockchain(CreateBlockchainRequest req) {
		List<BlockChainPo> blockchains = blockchainDas.findByConsId(req.getConsId());
		Assert.isTrue(CollectionUtils.isEmpty(blockchains), "联盟["+req.getConsId()+"]下已创建区块链");
		
		LocalDateTime now = LocalDateTime.now();
		String operUser = WebHelper.getUserId();
		BlockChainPo blockchain = BlockChainPo.builder()
				.id(UUID.randomUUID().toString())
				.blockchainName(req.getName())
				.consortiumId(req.getConsId())
				.alarmEmail(req.getAlertEmail())
				.consensusAlgorithm(req.getConsensus() == null ? BlockchainEnum.Consensus.TREE_GRAPH.getCode()
						: BlockchainEnum.Consensus.getByName(req.getConsensus()).getCode())
				.encryptAlgorithm(req.getAlgorithm() == null ? BlockchainEnum.ALGORITHM.TREE_GRAPH.getCode()
						: BlockchainEnum.ALGORITHM.getByName(req.getConsensus()).getCode())
				.status(BlockchainEnum.Status.TO_DEPLOY.getCode())
				.deleteFlag(IntTypeEnum.FALSE.getValue())
				.createTime(now)
				.createBy(operUser)
				.updateTime(now)
				.updateBy(operUser)
				.build();
		
		ConsortiumPo cons = buildService.buildToUpdateCons(req.getConsId(), EstablishStep.TO_ADD_NODE);
		
		return blockchainDas.createBlockchain(blockchain, cons);
	}

	public boolean addNodes(AddNodesRequest req) {
		List<NodePo> dbNodes = nodeDas.findByConsIdAndChainId(req.getConsId(), req.getBlockchainId());
		Set<String> dbIpPortSet = dbNodes.stream().map(item -> item.getNodeIp() + ":" + item.getNodePort())
				.collect(Collectors.toSet());
		List<NodeInfo> reqNodes = req.getNodes();
		reqNodes.stream().forEach(item -> {
			String ipPortPair = item.getIp() + ":" +item.getPort();
			Assert.isTrue(!dbIpPortSet.contains(ipPortPair), "节点不可重复添加["+ipPortPair+"]");
		});
		
		LocalDateTime now = LocalDateTime.now();
		String operUser = WebHelper.getUserId();
		List<NodePo> nodes = reqNodes.stream().map(item -> NodePo.builder()
				.id(UUID.randomUUID().toString())
				.consortiumId(req.getConsId())
				.blockchainId(req.getBlockchainId())
				.organizationName(item.getOrg())
				.nodeIp(item.getIp())
				.nodePort(Integer.parseInt(item.getPort()))
				.status(NodeEnum.Status.TO_DEPLOY.getCode())
				.deleteFlag(IntTypeEnum.FALSE.getValue())
				.createTime(now)
				.createBy(operUser)
				.updateTime(now)
				.updateBy(operUser)
				.build()).collect(Collectors.toList());
		
		ConsortiumPo cons = buildService.buildToUpdateCons(req.getConsId(), EstablishStep.TO_DEPLOY);
		
		return nodeDas.addNodes(nodes, cons);
	}

	// mock
	//List<NodePo> nodes = new ArrayList<>();
	//nodes.add(NodePo.builder().id("111").nodeIp("192.168.121.11").nodePort(9797).build());
	//nodes.add(NodePo.builder().id("222").nodeIp("192.168.121.12").nodePort(9797).build());
	
	@LogSkip
	public void getDeployScript(DownloadDeployScriptRequest req, HttpServletResponse resp) throws Exception {
		// nodes
		ConsortiumPo cons = consortiumDas.findById(req.getConsortiumId())
				.orElseThrow(() -> new AlertException("consortium.not.found"));
		List<NodePo> nodes = nodeDas.findByConsIdAndChainId(cons.getConsortiumId(), null);
		Assert.notEmpty(nodes, "node.not.found");
		List<String> consortiumNodeList = nodes.stream()
				.map(item -> item.getId() + "-" + item.getNodeIp() + "-" + item.getNodePort())
				.collect(Collectors.toList());
		String consortiumNodes = JSON.toJSONString(consortiumNodeList);
		
		// certificate
		KeypairPo rootKeypair = getRootKeypair();
		KeypairPo consortiumKeypair = getConsortiumKeyPair(req.getConsortiumId(), rootKeypair);
		
		// build script
		resp.setContentType("application/zip");
		resp.setHeader("Content-disposition", "attachment; filename=\"deploy.zip\"");
		ZipOutputStream zipOutputStream = new ZipOutputStream((OutputStream) resp.getOutputStream());
		for(NodePo node:nodes){
			String nodeIdentifier = node.getNodeIp()+"-"+node.getNodePort();
			zipOutputStream.putNextEntry(new ZipEntry("deploy."+nodeIdentifier+".toml"));
			String consortiumIdInfo = "consortium_id = " + req.getConsortiumId();
			String consortiumCertInfo = "consortium_cert = " + consortiumKeypair.getCertStr();
			String consortiumNodesInfo = "consortium_nodes = " + consortiumNodes;
			String consortiumKeypairInfo = "consortium_keypair = " + getNodeKeypair(req.getConsortiumId(), node);
			StringBuffer tomlFileContent = new StringBuffer();
			tomlFileContent.append(consortiumIdInfo).append(SplitterConstant.LINE_SEP);
			tomlFileContent.append(consortiumCertInfo).append(SplitterConstant.LINE_SEP);
			tomlFileContent.append(consortiumNodesInfo).append(SplitterConstant.LINE_SEP);
			tomlFileContent.append(consortiumKeypairInfo);
			zipOutputStream.write(tomlFileContent.toString().getBytes());
			zipOutputStream.closeEntry();
		}
		zipOutputStream.close();
	}
	
	public KeypairPo getRootKeypair() throws Exception{
		List<KeypairPo> rootKeypairs = keypairDas.findAll(ConsortiumConstant.DEFAULT_CONSORTIUM_ID,
				CryptoEnum.KeyType.CONSORTIUM.getCode(), X509CertConstant.DEFAULT_ALGO);
		if(CollectionUtils.isNotEmpty(rootKeypairs)){
			return rootKeypairs.get(0);
		}
		
		KeyPair rootKeyPair = KeyUtil.generateKeyPair(X509CertConstant.DEFAULT_ALGO);
		Certificate rootCA = CertUtil.generateRootCA(new X500Name(X509CertConstant.ROOT_CA_SUBJECT), rootKeyPair,
				CertUtil.getNotBefore(), CertUtil.getNotAfter(365 * 10));
		String rootPublicKey = KeyUtil.getPublicKeyString(rootKeyPair);
		String rootPrivateKey = KeyUtil.getPrivateKeyString(rootKeyPair);
		String rootCa = CertUtil.getCertStr(rootCA);
		
		LocalDateTime now = LocalDateTime.now();
		String operUser = WebHelper.getUserId();
		KeypairPo keypair = KeypairPo.builder()
				.publicKey(rootPublicKey)
				.privateKey(rootPrivateKey)
				.csrStr(null)
				.certStr(rootCa)
				.algorithm(X509CertConstant.DEFAULT_ALGO)
				.consortiumId(ConsortiumConstant.DEFAULT_CONSORTIUM_ID)
				.keyType(CryptoEnum.KeyType.CONSORTIUM.getCode())
				.deleteFlag(IntTypeEnum.FALSE.getValue())
				.createTime(now)
				.createBy(operUser)
				.updateTime(now)
				.updateBy(operUser)
				.build();
		keypairDas.save(keypair);
		
		return keypair;
	}
	
	private KeypairPo getConsortiumKeyPair(String consortiumId, KeypairPo rootKeypair)
			throws NoSuchProviderException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, Exception {
		PrivateKey rootPrivateKey = EncryptUtil.loadPrivateKey(X509CertConstant.DEFAULT_ALGO, rootKeypair.getPrivateKey());
		byte[] rootCertBytes = rootKeypair.getCertStr().getBytes();
		
		KeyPair consortiumKeyPair = KeyUtil.generateKeyPair(X509CertConstant.DEFAULT_ALGO);
		PKCS10CertificationRequest consortiumCsr = CertUtil.generateCSR(
				new X500Name(X509CertConstant.CONSORTIUM_CERT_SUBJECT), consortiumKeyPair.getPublic(),
				consortiumKeyPair.getPrivate());
		Certificate consortiumCert = CertUtil.generateCert(consortiumCsr, rootPrivateKey, rootCertBytes, CertUtil.getNotBefore(), CertUtil.getNotAfter(365));
		
		String consortiumPublicKey = KeyUtil.getPublicKeyString(consortiumKeyPair);
		String consortiumPrivateKey = KeyUtil.getPrivateKeyString(consortiumKeyPair);
		String consortiumCsrStr = CertUtil.getCsrStr(consortiumCsr);
		String consortiumCertStr = CertUtil.getCertStr(consortiumCert);
		
		LocalDateTime now = LocalDateTime.now();
		String operUser = WebHelper.getUserId();
		KeypairPo keypair = KeypairPo.builder()
				.publicKey(consortiumPublicKey)
				.privateKey(consortiumPrivateKey)
				.csrStr(consortiumCsrStr)
				.certStr(consortiumCertStr)
				.algorithm(X509CertConstant.DEFAULT_ALGO)
				.consortiumId(consortiumId)
				.keyType(CryptoEnum.KeyType.CONSORTIUM.getCode())
				.deleteFlag(IntTypeEnum.FALSE.getValue())
				.createTime(now)
				.createBy(operUser)
				.updateTime(now)
				.updateBy(operUser)
				.build();
		keypairDas.save(keypair);
		
		return keypair;
	}

	private String getNodeKeypair(String consortiumId, NodePo node)
			throws NoSuchProviderException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, Exception {
		KeyPair keyPair = KeyUtil.generateKeyPair(X509CertConstant.DEFAULT_ALGO);
		String nodePublicKey = KeyUtil.getPublicKeyString(keyPair);
		String nodePrivateKey = KeyUtil.getPrivateKeyString(keyPair);
		
		LocalDateTime now = LocalDateTime.now();
		String operUser = WebHelper.getUserId();
		KeypairPo keypair = KeypairPo.builder()
				.publicKey(nodePublicKey)
				.privateKey(nodePrivateKey)
				.csrStr(null)
				.certStr(null)
				.algorithm(X509CertConstant.DEFAULT_ALGO)
				.consortiumId(consortiumId)
				.keyType(CryptoEnum.KeyType.NODE.getCode())
				.deleteFlag(IntTypeEnum.FALSE.getValue())
				.createTime(now)
				.createBy(operUser)
				.updateTime(now)
				.updateBy(operUser)
				.build();
		keypairDas.save(keypair);
		
		NodeKeyPo nodeKey = NodeKeyPo.builder()
				.nodeId(node.getId())
				.keypairId(keypair.getId())
				.consortiumId(consortiumId)
				.deleteFlag(IntTypeEnum.FALSE.getValue())
				.createTime(now)
				.createBy(operUser)
				.updateTime(now)
				.updateBy(operUser)
				.build();
		nodeKeyDas.save(nodeKey);
		
		return node.getId() + "-" + nodePublicKey + "-" + nodePrivateKey;
	}
}
