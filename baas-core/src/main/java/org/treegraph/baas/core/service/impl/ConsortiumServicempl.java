package org.treegraph.baas.core.service.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.security.PrivateKey;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
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
import org.treegraph.baas.core.service.bo.OrgBo;
import org.treegraph.baas.core.service.bo.RoleInfoBo;
import org.treegraph.baas.core.service.bo.UserInfoBo;
import org.treegraph.baas.core.util.DateTimeUtils;
import org.treegraph.baas.core.util.LogSkip;
import org.treegraph.baas.core.util.crypto.x509.EncryptUtil;
import org.treegraph.baas.core.util.crypto.x509.KeyUtil;
import org.treegraph.baas.core.util.crypto.x509.SignUtil;
import org.web3j.crypto.ECKeyPair;
import org.web3j.utils.Numeric;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Functions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import conflux.web3j.AccountManager;
import conflux.web3j.Cfx;
import conflux.web3j.Request;
import conflux.web3j.response.StringResponse;
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
    
    private HeartbeatService heartbeatService;
   
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
		List<NodePo> dbNodes = nodeDas.findByConsIdAndChainId(req.getConsId(), req.getBlockchainId());
		Set<String> dbIpPortSet = dbNodes.stream().map(item -> item.getNodeIp() + ":" + item.getNodePort())
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
			Map<String, Boolean> heartbeatMap = heartbeatService.batchNodeHeartbeat(dbNodes);
			Set<Boolean> heartbeatSet = Sets.newHashSet(heartbeatMap.values());
			Assert.isTrue(heartbeatSet.contains(Boolean.TRUE), "blockchain not lunched!");
			adjustNodes(req.getConsId(), dbNodes, nodes, heartbeatMap);
		}
		
		return addNodeResult;
	}
	
	private void adjustNodes(String consortiumId, List<NodePo> dbNodes, List<NodePo> newNodes, Map<String, Boolean> heartbeatMap) throws IOException {
		List<NodeKeypairBo> dbNodeKeypairs = dbNodes.stream().map(
				item -> vaultKeypairBuildService.getNodeKeypair(consortiumId, X509CertConstant.DEFAULT_ALGO_NODE, item))
				.collect(Collectors.toList());
		Optional<NodeKeypairBo> aliveNode = dbNodeKeypairs.stream()
				.filter(item -> heartbeatMap.get(item.getNode().getNodeIp() + ":" + item.getNode().getNodePort()))
				.findFirst();
		Assert.isTrue(aliveNode.isPresent(), "not found alive node!");
		NodeKeypairBo proposerNodeKeypair = aliveNode.get();
		NodePo proposerNode = proposerNodeKeypair.getNode();
		Cfx cfx = Cfx.create("http://" + proposerNode.getNodeIp() + ":" + proposerNode.getNodePort());
		
		/*
		 * 1. RPC name:cfx_getMembershipChangeRawTransaction 
		 * 
		 * input : 
		 * ---Vector<public key> new_validators,// all validators in the next consensus round 
		 * ---public_key proposer 
		 * 
		 * output: 
		 * ---string raw_json
		 */
		List<String> publicKeys = dbNodeKeypairs.stream().map(item -> item.getKeypair().getPublicKey())
				.collect(Collectors.toList());
		String proposerPublicKey = proposerNodeKeypair.getKeypair().getPublicKey();
		Request<String, StringResponse> request = cfx.getCustomizedRequest(StringResponse.class,
				ConfluxConstant.INTERFACE_ADD_DELETE_NODE, publicKeys, proposerPublicKey);
		StringResponse response = request.send();
		String rawJson = response.getResult();
		Assert.isTrue(rawJson != null, ConfluxConstant.INTERFACE_ADD_DELETE_NODE + " error");
		
		/*
		 * 2. Keccak(raw_json) and ring signature
		 * 
		 * input : 
		 * ---Vector<(public key,private key)>, secp256k1(Keccak(raw_json)) 
		 * 
		 * output: 
		 * ---Vector<signature>
		 */
		List<NodeKeypairBo> newNodeKeypairs = newNodes.stream().map(
				item -> vaultKeypairBuildService.getNodeKeypair(consortiumId, X509CertConstant.DEFAULT_ALGO_NODE, item))
				.collect(Collectors.toList());
		List<KeypairPo> keypairs = Lists.newArrayList();
		dbNodeKeypairs.stream().forEach(item -> keypairs.add(item.getKeypair()));
		newNodeKeypairs.stream().forEach(item -> keypairs.add(item.getKeypair()));
		Map<KeypairPo, String> ringSignMap = Maps.newHashMap();
		keypairs.stream().forEach(item -> {
			try {
				ECKeyPair ecKeypair = KeyUtil.loadECKeyPairFromBase64(item.getPublicKey(), item.getPrivateKey(),
						item.getAlgorithm());
				String sign = AccountManager.signMessage(Numeric.hexStringToByteArray(rawJson), true, ecKeypair);
				ringSignMap.put(item, sign);
			} catch (Exception e) {
				throw new RuntimeException("ring signature error!");
			}
		});

		/*
		 * 3. RPC name: cfx_constructAndSendNewConsortiumMembershipTrans 
		 * 
		 * input :
		 * ---string raw_json 
		 * ---Vector<public key> 
		 * ---Vector<signature> 
		 * 
		 * output:
		 * ---string okay/fail
		 */
		newNodeKeypairs.stream().forEach(item -> publicKeys.add(item.getKeypair().getPublicKey()));
		List<String> signs = Lists.newArrayList(ringSignMap.values());
		Request<String, StringResponse> requestConfirm = cfx.getCustomizedRequest(StringResponse.class,
				ConfluxConstant.INTERFACE_ADD_DELETE_NODE_CONFIRM, rawJson, publicKeys, signs);
		StringResponse responseConfirm = requestConfirm.send();
		Assert.isTrue(responseConfirm != null 
				&& responseConfirm.getResult() != null
				&& responseConfirm.getResult().equalsIgnoreCase("ok"),
				ConfluxConstant.INTERFACE_ADD_DELETE_NODE_CONFIRM + "error");
	}

	@LogSkip
	public void getDeployScript(DownloadDeployScriptRequest req, HttpServletResponse resp) throws Exception {
		List<NodePo> nodes = nodeDas.findAll(req.getConsortiumId(), null);
		if(CollectionUtils.isEmpty(nodes)) { // mock
			nodes.add(NodePo.builder().id("111111111111111111111111111111111111").nodeIp("101.200.183.108").nodePort(12537).build());
			nodes.add(NodePo.builder().id("222222222222222222222222222222222222").nodeIp("101.200.183.108").nodePort(12538).build());
			nodes.add(NodePo.builder().id("333333333333333333333333333333333333").nodeIp("101.200.183.108").nodePort(12539).build());
			nodes.add(NodePo.builder().id("444444444444444444444444444444444444").nodeIp("101.200.183.108").nodePort(12540).build());
		}
		Assert.notEmpty(nodes, "node.not.found");
		
		// certificate info
		String algoCons = X509CertConstant.DEFAULT_ALGO_CONS;
		String algoNode = X509CertConstant.DEFAULT_ALGO_NODE;
		KeypairPo rootKeypair = vaultKeypairBuildService.getRootKeypair(algoCons);
		KeypairPo consortiumKeypair = vaultKeypairBuildService.getConsortiumKeyPair(req.getConsortiumId(), algoCons, rootKeypair);
		PrivateKey privateKey = KeyUtil.privateKeyFromBase64(algoCons, consortiumKeypair.getPrivateKey());
		
		// nodes info
		StringBuilder consortiumNodes = new StringBuilder();
		StringBuilder bootNodes = new StringBuilder();
		List<NodeKeypairBo> nodeKeypairBoList = new ArrayList<>();
		for(NodePo node:nodes){
			NodeKeypairBo nodeKeypairBo = vaultKeypairBuildService.getNodeKeypair(req.getConsortiumId(), algoNode, node);
			nodeKeypairBoList.add(nodeKeypairBo);
			ECKeyPair ecKeypair = KeyUtil.loadECKeyPairFromBase64(nodeKeypairBo.getKeypair().getPublicKey(),
					nodeKeypairBo.getKeypair().getPrivateKey(), nodeKeypairBo.getKeypair().getAlgorithm());
			consortiumNodes.append(ecKeypair.getPublicKey().toString(16));
			bootNodes.append("cfxnode://");
			bootNodes.append(ecKeypair.getPublicKey().toString(16) + CharacterConstant.AT + nodeKeypairBo.getNode().getNodeIp()
					+ CharacterConstant.COLON + nodeKeypairBo.getNode().getNodePort());
			if(!node.getId().equalsIgnoreCase(nodes.get(nodes.size()-1).getId())) {
				consortiumNodes.append(CharacterConstant.COMMA);
				bootNodes.append(CharacterConstant.COMMA);
			}
		}
		
		// nodes info digest
		byte[] consortiumNodesSHA3 = SignUtil.SHA256Digest(bootNodes.toString().getBytes());
		byte[] consortiumNodesDigestCipher = EncryptUtil.codecByPrivateKey(algoCons, true, privateKey, consortiumNodesSHA3);
		String consortiumNodesDigest = Base64.toBase64String(consortiumNodesDigestCipher);
		
		// script content
		Map<String, String> nodeScriptContentMap = new HashMap<>();
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
			scriptContent.append("consortium_keypair = \"" + ecKeypair.getPublicKey().toString(16) + CharacterConstant.COMMA
					+ ecKeypair.getPrivateKey().toString(16) + "\"");
			scriptContent.append("jsonrpc_http_port = " + nodeKeypairBo.getNode().getNodePort());
			scriptContent.append("bootnodes = \"\"\"" + bootNodes.toString() + "\"\"\"");
			nodeScriptContentMap.put(nodeKeypairBo.getNode().getNodeIp() + "-" + nodeKeypairBo.getNode().getNodePort(),
					scriptContent.toString());
		}
		
		// save key and write script
		keypairDas.addKeyPairInfo(rootKeypair, consortiumKeypair, nodeKeypairBoList);
		log.info("build deploy script finished["+req.getConsortiumId()+"]");
		resp.setContentType("application/zip");
		resp.setHeader("Content-disposition", "attachment; filename=\"deploy.zip\"");
		ZipOutputStream zipOutputStream = new ZipOutputStream((OutputStream) resp.getOutputStream());
		for(Map.Entry<String, String> item : nodeScriptContentMap.entrySet()){
			zipOutputStream.putNextEntry(new ZipEntry("deploy."+item.getKey()+".toml"));
			zipOutputStream.write(item.getValue().getBytes());
			zipOutputStream.closeEntry();
		}
		zipOutputStream.close();
		log.info("write deploy script finished["+req.getConsortiumId()+"]");
		ConsortiumPo cons = buildService.buildToUpdateCons(req.getConsortiumId(), EstablishStep.DOWNLOADED_SCRIPTS);
		consortiumDas.modifyCons(cons);
		log.info("update consortium status finished["+req.getConsortiumId()+"]");
	}
}
