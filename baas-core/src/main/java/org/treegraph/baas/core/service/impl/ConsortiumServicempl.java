package org.treegraph.baas.core.service.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.util.encoders.Base64;
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
import org.treegraph.baas.core.constant.CharacterConstant;
import org.treegraph.baas.core.constant.ConfluxConstant;
import org.treegraph.baas.core.constant.SplitterConstant;
import org.treegraph.baas.core.constant.X509CertConstant;
import org.treegraph.baas.core.das.BlockchainDas;
import org.treegraph.baas.core.das.ConsortiumDas;
import org.treegraph.baas.core.das.KeypairDas;
import org.treegraph.baas.core.das.NodeDas;
import org.treegraph.baas.core.das.RoleDas;
import org.treegraph.baas.core.das.UserInfoDas;
import org.treegraph.baas.core.das.bo.NodeKeypairBo;
import org.treegraph.baas.core.domain.BlockChainPo;
import org.treegraph.baas.core.domain.ConsortiumPo;
import org.treegraph.baas.core.domain.KeypairPo;
import org.treegraph.baas.core.domain.NodeCustomizedConfigPo;
import org.treegraph.baas.core.domain.NodePo;
import org.treegraph.baas.core.domain.RolePo;
import org.treegraph.baas.core.domain.UserInfoPo;
import org.treegraph.baas.core.domain.UserOrganizationPo;
import org.treegraph.baas.core.domain.UserRolePo;
import org.treegraph.baas.core.enums.BlockchainEnum;
import org.treegraph.baas.core.enums.ConsortiumEnum;
import org.treegraph.baas.core.enums.ConsortiumEnum.EstablishStep;
import org.treegraph.baas.core.enums.NodeEnum;
import org.treegraph.baas.core.enums.UserEnum;
import org.treegraph.baas.core.service.ConsortiumService;
import org.treegraph.baas.core.service.NodeCustomizedConfigService;
import org.treegraph.baas.core.service.bo.OrgBo;
import org.treegraph.baas.core.service.bo.RoleInfoBo;
import org.treegraph.baas.core.service.bo.UserInfoBo;
import org.treegraph.baas.core.util.ConfluxUtils;
import org.treegraph.baas.core.util.DateTimeUtils;
import org.treegraph.baas.core.util.LogSkip;
import org.treegraph.baas.core.util.crypto.x509.EncryptUtil;
import org.treegraph.baas.core.util.crypto.x509.KeyUtil;
import org.treegraph.baas.core.util.crypto.x509.SignUtil;
import org.web3j.crypto.ECKeyPair;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Functions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Validated
@Service
@AllArgsConstructor
@Slf4j
public class ConsortiumServicempl implements ConsortiumService {
    
    private ConsortiumDas consortiumDas;
    
    private UserInfoDas userInfoDas;
    
    private RoleDas roleDas;
    
    private BlockchainDas blockchainDas;
    
    private NodeDas nodeDas;
    
    private KeypairDas keypairDas;
    
    private BuildService buildService;
    
    private VaultKeypairBuildService vaultKeypairBuildService;
    
    private RpcBlockchainService rpcBlockchainService;
    
	private NodeCustomizedConfigService nodeCustomizedConfigService;
   
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

	public boolean addNodes(AddNodesRequest req) throws IOException {
		//check
		List<NodePo> existNodes = nodeDas.findByConsIdAndChainId(req.getConsId(), req.getBlockchainId(), true);
		Set<String> dbIpPortSet = existNodes.stream().map(item -> item.getNodeIp() + ":" + item.getNodePort())
				.collect(Collectors.toSet());
		List<NodeInfo> reqNodes = req.getNodes();
		reqNodes.stream().forEach(item -> {
			String ipPortPair = item.getIp() + ":" +item.getPort();
			Assert.isTrue(!dbIpPortSet.contains(ipPortPair), "节点不可重复添加["+ipPortPair+"]");
		});
		
		// build
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
		
		// before download deploy scripts
		boolean addNodeResult = Boolean.FALSE;
		int establishStep = consortiumDas.findById(req.getConsId()).get().getEstablishStep();
		if(establishStep < EstablishStep.DOWNLOADED_SCRIPTS.getCode()) {
			ConsortiumPo cons = buildService.buildToUpdateCons(req.getConsId(), EstablishStep.TO_DEPLOY);
			addNodeResult = nodeDas.addNodes(nodes, cons);
		}
		
		// after download deploy scripts, need check chain is launched already
		if(establishStep == EstablishStep.DOWNLOADED_SCRIPTS.getCode()) {
			List<NodeKeypairBo> existNodeKeypairs = existNodes.stream().map(
					item -> vaultKeypairBuildService.getNodeKeypair(req.getConsId(), X509CertConstant.DEFAULT_ALGO_NODE, item, null))
					.collect(Collectors.toList());
			List<NodeKeypairBo> toAddNodeKeypairs = nodes.stream().map(
					item -> vaultKeypairBuildService.getNodeKeypair(req.getConsId(), X509CertConstant.DEFAULT_ALGO_NODE, item, null))
					.collect(Collectors.toList());
			toAddNodeKeypairs.stream().forEach(item -> item.getNode().setStatus(NodeEnum.Status.ADDING_BY_MEMBERSHIP_CHANGE.getCode()));
			nodeDas.addNodeKeypairs(toAddNodeKeypairs);
			rpcBlockchainService.sendMembershipChangeTransactionPlus(req.getConsId(), existNodeKeypairs, toAddNodeKeypairs, null);
			List<String> nodeIds = nodes.stream().map(NodePo::getId).collect(Collectors.toList());
			nodeDas.modifyNodesStatus(nodeIds, NodeEnum.Status.ADDED_BY_MEMBERSHIP_CHANGE);
		}
		
		return addNodeResult;
	}
	
	@LogSkip
	public void getDeployScript(DownloadDeployScriptRequest req, HttpServletResponse resp) throws Exception {
		// check and build
		String consortiumId = req.getConsId();
		Assert.notNull(consortiumId, "consortiumId.not.found");
		List<NodePo> nodes = nodeDas.findAll(consortiumId, null);
		Assert.notEmpty(nodes, "node.not.found");
		Map<String, List<String>> nodeScriptMap = buildAndSaveKeypairInfo(consortiumId, nodes);
		
		// filter to download script
		Map<String, List<String>> toDownloadNodeScriptMap = Maps.newHashMap();
		List<String> toDownloadNodeIdList = req.getNodeId();
		if(CollectionUtils.isNotEmpty(toDownloadNodeIdList)) {
			Set<String>	toDownloadNodeIdSet = new HashSet<>(toDownloadNodeIdList);
			Set<String> toDownloadNodeKeySet = nodes.stream().filter(item -> toDownloadNodeIdSet.contains(item.getId()))
					.map(item -> item.getNodeIp() + CharacterConstant.AT + item.getNodePort())
					.collect(Collectors.toSet());
			nodeScriptMap.keySet().forEach(item -> {
				if(toDownloadNodeKeySet.contains(item)) {
					toDownloadNodeScriptMap.put(item, nodeScriptMap.get(item));
				}
			});
		} else {
			toDownloadNodeScriptMap.putAll(nodeScriptMap);
		}
		
		// download deploy.zip file
		resp.setContentType("application/zip");
		resp.setHeader("Content-disposition", "attachment; filename=\"deploy.zip\"");
		ZipOutputStream zipOutputStream = new ZipOutputStream((OutputStream) resp.getOutputStream());
		for(Map.Entry<String, List<String>> item : toDownloadNodeScriptMap.entrySet()){
			zipOutputStream.putNextEntry(new ZipEntry(item.getKey() + SplitterConstant.FILE_SEP + "config.toml"));
			zipOutputStream.write(item.getValue().get(0).getBytes());
			zipOutputStream.closeEntry();
			zipOutputStream.putNextEntry(new ZipEntry(item.getKey() + SplitterConstant.FILE_SEP + "customized_config.toml"));
			zipOutputStream.write(item.getValue().get(1).getBytes());
			zipOutputStream.closeEntry();
		}
		zipOutputStream.close();
		log.info("write deploy script finished[{}]", consortiumId);
		
		// update consortium establish step
		ConsortiumPo cons = buildService.buildToUpdateCons(consortiumId, EstablishStep.DOWNLOADED_SCRIPTS);
		consortiumDas.modifyCons(cons);
		log.info("update consortium status finished[{}]", consortiumId);
	}

	private Map<String, List<String>> buildAndSaveKeypairInfo(String consortiumId, List<NodePo> nodes)
			throws Exception, NoSuchProviderException, NoSuchAlgorithmException, InvalidAlgorithmParameterException {
		// certificate info
		String algoCons = X509CertConstant.DEFAULT_ALGO_CONS;
		String algoNode = X509CertConstant.DEFAULT_ALGO_NODE;
		KeypairPo rootKeypair = vaultKeypairBuildService.getRootKeypair(algoCons);
		KeypairPo consortiumKeypair = vaultKeypairBuildService.getConsortiumKeyPair(consortiumId, algoCons, rootKeypair);
		PrivateKey privateKey = KeyUtil.privateKeyFromBase64(algoCons, consortiumKeypair.getPrivateKey());
		
		// nodes info
		StringBuilder consortiumNodes = new StringBuilder();
		List<NodeKeypairBo> nodeKeypairBoList = new ArrayList<>();
		for(NodePo node:nodes){
			NodeKeypairBo nodeKeypairBo = vaultKeypairBuildService.getNodeKeypair(consortiumId, algoNode, node, consortiumKeypair);
			nodeKeypairBoList.add(nodeKeypairBo);
			ECKeyPair ecKeypair = KeyUtil.loadECKeyPairFromBase64(nodeKeypairBo.getKeypair().getPublicKey(),
					nodeKeypairBo.getKeypair().getPrivateKey(), nodeKeypairBo.getKeypair().getAlgorithm());
			consortiumNodes.append("cfxnode://");
			consortiumNodes.append(StringUtils.leftPad(ecKeypair.getPublicKey().toString(16), 128, '0') + CharacterConstant.AT + nodeKeypairBo.getNode().getNodeIp()
					+ CharacterConstant.COLON + nodeKeypairBo.getNode().getNodePort());
			if(!node.getId().equalsIgnoreCase(nodes.get(nodes.size()-1).getId())) {
				consortiumNodes.append(CharacterConstant.COMMA);
			}
		}
		
		// nodes info digest
		byte[] consortiumNodesSHA3 = SignUtil.SHA256Digest(consortiumNodes.toString().getBytes());
		byte[] consortiumNodesDigestCipher = EncryptUtil.codecByPrivateKey(algoCons, true, privateKey, consortiumNodesSHA3);
		String consortiumNodesDigest = Base64.toBase64String(consortiumNodesDigestCipher);
		
		// script content
		Map<String, List<String>> nodeScriptContentMap = new HashMap<>();
		String stepFactorPropertyValue = nodeCustomizedConfigService.getPropertyValueByName(consortiumId,
				ConfluxConstant.PROPERTY_JSON_RPC_HTTP_PORT_STEP_FACTOR);
		int stepFactor = stepFactorPropertyValue != null ? Integer.parseInt(stepFactorPropertyValue) : 0;
		List<NodeCustomizedConfigPo> customizedConfigList = Lists.newArrayList();
		for(NodeKeypairBo nodeKeypairBo:nodeKeypairBoList){
			StringBuffer scriptContent = new StringBuffer();
			scriptContent.append("consortium_cert = \"\"\"" + consortiumKeypair.getCertStr() + "\"\"\"")
					.append(SplitterConstant.LINE_SEP).append(SplitterConstant.LINE_SEP);
			scriptContent.append("consortium_nodes = \"\"\"" + consortiumNodes.toString() + "\"\"\"")
					.append(SplitterConstant.LINE_SEP).append(SplitterConstant.LINE_SEP);
			scriptContent.append("consortium_nodes_digest = \"" + consortiumNodesDigest + "\"")
					.append(SplitterConstant.LINE_SEP).append(SplitterConstant.LINE_SEP);
			ECKeyPair ecKeypair = KeyUtil.loadECKeyPairFromBase64(nodeKeypairBo.getKeypair().getPublicKey(),
					nodeKeypairBo.getKeypair().getPrivateKey(), nodeKeypairBo.getKeypair().getAlgorithm());
			scriptContent.append("consortium_keypair = \"" + StringUtils.leftPad(ecKeypair.getPublicKey().toString(16), 128, '0') + CharacterConstant.COMMA
					+ StringUtils.leftPad(ecKeypair.getPrivateKey().toString(16), 64, '0') + "\"")
				.append(SplitterConstant.LINE_SEP).append(SplitterConstant.LINE_SEP);
			scriptContent.append("grpc_server_cert = \"\"\"" + nodeKeypairBo.getKeypair().getCertStr() + "\"\"\"")
				.append(SplitterConstant.LINE_SEP).append(SplitterConstant.LINE_SEP);
			String nodePublicKeyPem = KeyUtil
					.publicKeyToPem(KeyUtil.publicKeyFromBase64(algoNode, nodeKeypairBo.getKeypair().getPublicKey()));
			scriptContent.append("grpc_server_key = \"\"\"" + nodePublicKeyPem + "\"\"\"")
				.append(SplitterConstant.LINE_SEP).append(SplitterConstant.LINE_SEP);
			
			// node customized script content
			NodePo node = nodeKeypairBo.getNode();
			String jsonRpcHttpPort = null;
			String jsonRpcLocalHttpPort = null;
			String miningAuthor = null;
			List<NodeCustomizedConfigPo> customizedConfigs = nodeCustomizedConfigService.getCustomizedConfig(Lists.newArrayList(node));
			if(CollectionUtils.isNotEmpty(customizedConfigs)) {
				for(NodeCustomizedConfigPo config:customizedConfigs) {
					if(ConfluxConstant.PROPERTY_JSON_RPC_HTTP_PORT.equalsIgnoreCase(config.getPropertyName())) {
						jsonRpcHttpPort = config.getPropertyValue();
					}
					if(ConfluxConstant.PROPERTY_JSON_RPC_LOCAL_HTTP_PORT.equalsIgnoreCase(config.getPropertyName())) {
						jsonRpcLocalHttpPort = config.getPropertyValue();
					}
					if(ConfluxConstant.PROPERTY_MINING_AUTHOR.equalsIgnoreCase(config.getPropertyName())) {
						miningAuthor = config.getPropertyValue();
					}
				}
			} else {
				int portDelta = (ConfluxConstant.TEST_SWITCH ? stepFactor++ : 0) * ConfluxConstant.PROPERTY_JSON_RPC_HTTP_PORT_STEP;
				jsonRpcHttpPort = (ConfluxConstant.DEFAULT_JSON_RPC_HTTP_PORT + portDelta) + "";
				jsonRpcLocalHttpPort = (Integer.parseInt(jsonRpcHttpPort) + ConfluxConstant.PROPERTY_JSON_RPC_HTTP_PORT_INTERVAL) + "";
				miningAuthor = ConfluxUtils.toAddress(ecKeypair);
				customizedConfigList.add(buildService.buildNodeCustomizedConfig(node, ConfluxConstant.PROPERTY_JSON_RPC_HTTP_PORT, jsonRpcHttpPort));
				customizedConfigList.add(buildService.buildNodeCustomizedConfig(node, ConfluxConstant.PROPERTY_JSON_RPC_LOCAL_HTTP_PORT, jsonRpcLocalHttpPort));
				customizedConfigList.add(buildService.buildNodeCustomizedConfig(node, ConfluxConstant.PROPERTY_MINING_AUTHOR, miningAuthor));
			}
			StringBuffer customizedScriptContent = new StringBuffer();
			customizedScriptContent.append(ConfluxConstant.PROPERTY_JSON_RPC_HTTP_PORT + " = " + jsonRpcHttpPort)
					.append(SplitterConstant.LINE_SEP).append(SplitterConstant.LINE_SEP);
			customizedScriptContent.append(ConfluxConstant.PROPERTY_JSON_RPC_LOCAL_HTTP_PORT + " = " + jsonRpcLocalHttpPort)
					.append(SplitterConstant.LINE_SEP).append(SplitterConstant.LINE_SEP);
			customizedScriptContent.append(ConfluxConstant.PROPERTY_MINING_AUTHOR + " = \"" + miningAuthor + "\"")
					.append(SplitterConstant.LINE_SEP).append(SplitterConstant.LINE_SEP);
			customizedScriptContent.append("log_file='./conflux.log'")
					.append(SplitterConstant.LINE_SEP).append(SplitterConstant.LINE_SEP);
			customizedScriptContent.append("log_level=\"trace\"")
					.append(SplitterConstant.LINE_SEP).append(SplitterConstant.LINE_SEP);
			
			// build script map
			List<String> scripts = Lists.newArrayList();
			scripts.add(scriptContent.toString());
			scripts.add(customizedScriptContent.toString());
			nodeScriptContentMap.put(nodeKeypairBo.getNode().getNodeIp() + CharacterConstant.AT + nodeKeypairBo.getNode().getNodePort(),
					scripts);
		}
		
		// save key pair and write script
		NodeCustomizedConfigPo stepFactorCustomizedConfig = buildService.buildNodeCustomizedConfig(
				NodePo.builder().id(IntTypeEnum.FALSE.getValue() + "").consortiumId(consortiumId).build(),
				ConfluxConstant.PROPERTY_JSON_RPC_HTTP_PORT_STEP_FACTOR, stepFactor + "");
		keypairDas.addKeyPairInfo(rootKeypair, consortiumKeypair, nodeKeypairBoList, customizedConfigList, stepFactorCustomizedConfig);
		log.info("build deploy script finished[{}]", consortiumId);
		return nodeScriptContentMap;
	}
	
	public static void main(String[] args) {
		String s = StringUtils.leftPad("51ee302acfcd727c066fc16588272d89ca5758822babd6d957aed6e07af61f1", 64, '0');
		System.out.println(s);
	}
}
