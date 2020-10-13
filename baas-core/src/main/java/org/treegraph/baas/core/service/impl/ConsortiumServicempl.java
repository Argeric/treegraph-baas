package org.treegraph.baas.core.service.impl;

import static org.treegraph.baas.core.constant.CharacterConstant.AT;
import static org.treegraph.baas.core.constant.CharacterConstant.COLON;
import static org.treegraph.baas.core.constant.CharacterConstant.COMMA;
import static org.treegraph.baas.core.constant.CharacterConstant.DOT;
import static org.treegraph.baas.core.constant.ConfluxConstant.DEFAULT_GRPC_PORT;
import static org.treegraph.baas.core.constant.ConfluxConstant.DEFAULT_JSONRPC_LOCAL_PORT;
import static org.treegraph.baas.core.constant.ConfluxConstant.DEFAULT_JSONRPC_PORT;
import static org.treegraph.baas.core.constant.ConfluxConstant.PROPERTY_GRPC_PORT;
import static org.treegraph.baas.core.constant.ConfluxConstant.PROPERTY_JSONRPC_LOC_PORT;
import static org.treegraph.baas.core.constant.ConfluxConstant.PROPERTY_JSONRPC_PORT;
import static org.treegraph.baas.core.constant.ConfluxConstant.PROPERTY_JSONRPC_PORT_STEP_FACTOR;
import static org.treegraph.baas.core.constant.ConfluxConstant.PROPERTY_LOG_MONITOR_CONTAINER_NETWORK;
import static org.treegraph.baas.core.constant.ConfluxConstant.PROPERTY_LOG_MONITOR_CONTAINER_NETWORK_STEP_FACTOR;
import static org.treegraph.baas.core.constant.ConfluxConstant.PROPERTY_LOG_MONITOR_WEB_PORT;
import static org.treegraph.baas.core.constant.ConfluxConstant.PROPERTY_LOG_MONITOR_WEB_PORT_STEP_FACTOR;
import static org.treegraph.baas.core.constant.ConfluxConstant.TEST_JSONRPC_PORT_STEP;
import static org.treegraph.baas.core.constant.ConfluxConstant.TEST_LOG_MONITOR_CONTAINER_NETWORK_STEP;
import static org.treegraph.baas.core.constant.ConfluxConstant.TEST_LOG_MONITOR_WEB_PORT_STEP;
import static org.treegraph.baas.core.constant.ConfluxConstant.TEST_MONITOR_PORT_INTERVAL;
import static org.treegraph.baas.core.constant.ConfluxConstant.TEST_RPC_PORT_INTERVAL;
import static org.treegraph.baas.core.constant.ConfluxConstant.TEST_SWITCH;
import static org.treegraph.baas.core.constant.MonitorConstant.DEFAULT_CONTAINER_NETWORK;
import static org.treegraph.baas.core.constant.MonitorConstant.DEFAULT_HOST_WEB_PORT;
import static org.treegraph.baas.core.constant.SplitterConstant.FILE_SEP;
import static org.treegraph.baas.core.constant.SplitterConstant.LINE_SEP;
import static org.treegraph.baas.core.constant.X509CertConstant.ALGO_CONS;
import static org.treegraph.baas.core.constant.X509CertConstant.ALGO_NODE;
import static org.treegraph.baas.core.util.ConfluxUtils.toIpAtPort;
import static org.treegraph.baas.core.util.DateTimeUtils.FORMAT_yyyyMMddHHmmss;
import static org.treegraph.baas.core.util.crypto.x509.KeyUtil.loadECKeyPairFromBase64;
import static org.treegraph.baas.core.util.crypto.x509.KeyUtil.privateKeyFromBase64;
import static org.treegraph.baas.core.util.crypto.x509.KeyUtil.privateKeyToPem;

import java.io.OutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
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
import org.springframework.core.io.ClassPathResource;
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
import org.treegraph.baas.api.dto.request.DeferCertificateRequest;
import org.treegraph.baas.api.dto.request.DeleteConsRequest;
import org.treegraph.baas.api.dto.request.DownloadDeployScriptRequest;
import org.treegraph.baas.api.dto.request.ModifyConsRequest;
import org.treegraph.baas.api.dto.request.QueryConsDetailRequest;
import org.treegraph.baas.api.dto.request.QueryConsRequest;
import org.treegraph.baas.api.dto.response.ListConsResponse;
import org.treegraph.baas.common.enums.IntTypeEnum;
import org.treegraph.baas.common.util.WebHelper;
import org.treegraph.baas.core.constant.MonitorConstant;
import org.treegraph.baas.core.das.BlockchainDas;
import org.treegraph.baas.core.das.CertificateDeferDas;
import org.treegraph.baas.core.das.ConsortiumDas;
import org.treegraph.baas.core.das.KeypairDas;
import org.treegraph.baas.core.das.NodeCustomizedConfigDas;
import org.treegraph.baas.core.das.NodeDas;
import org.treegraph.baas.core.das.RoleDas;
import org.treegraph.baas.core.das.UserInfoDas;
import org.treegraph.baas.core.das.bo.NodeKeypairBo;
import org.treegraph.baas.core.domain.BlockChainPo;
import org.treegraph.baas.core.domain.CertificateDeferPo;
import org.treegraph.baas.core.domain.ConsortiumPo;
import org.treegraph.baas.core.domain.KeypairPo;
import org.treegraph.baas.core.domain.NodeCustomizedConfigPo;
import org.treegraph.baas.core.domain.NodePo;
import org.treegraph.baas.core.domain.RolePo;
import org.treegraph.baas.core.domain.UserInfoPo;
import org.treegraph.baas.core.domain.UserOrganizationPo;
import org.treegraph.baas.core.domain.UserRolePo;
import org.treegraph.baas.core.enums.BlockchainEnum;
import org.treegraph.baas.core.enums.CertificateEnum.DeferStatus;
import org.treegraph.baas.core.enums.ConsortiumEnum;
import org.treegraph.baas.core.enums.ConsortiumEnum.EstablishStep;
import org.treegraph.baas.core.enums.CryptoEnum.KeyType;
import org.treegraph.baas.core.enums.MembershipEnum;
import org.treegraph.baas.core.enums.NodeEnum.MembershipStatus;
import org.treegraph.baas.core.enums.UserEnum;
import org.treegraph.baas.core.exception.AlertException;
import org.treegraph.baas.core.service.ConsortiumService;
import org.treegraph.baas.core.service.bo.CustomizedConfigBo;
import org.treegraph.baas.core.service.bo.MembershipChangeBo;
import org.treegraph.baas.core.service.bo.OrgBo;
import org.treegraph.baas.core.service.bo.RoleInfoBo;
import org.treegraph.baas.core.service.bo.UserInfoBo;
import org.treegraph.baas.core.util.ConfluxUtils;
import org.treegraph.baas.core.util.DateTimeUtils;
import org.treegraph.baas.core.util.FileUtils;
import org.treegraph.baas.core.util.LogSkip;
import org.treegraph.baas.core.util.crypto.x509.CertUtil;
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
    
    private NodeCustomizedConfigDas nodeCustomizedConfigDas;
    
    private CertificateDeferDas certificateDeferDas;
    
    private BuildService buildService;
    
    private VaultKeypairBuildService vaultKeypairBuildService;
    
    private JsonRpcServiceImpl jsonRpcService;
    
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
				.createTime(DateTimeUtils.formatTime(item.getCreateTime(), FORMAT_yyyyMMddHHmmss))
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
							.createTime(DateTimeUtils.formatTime(node.getCreateTime(), FORMAT_yyyyMMddHHmmss))
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
		List<ConsortiumPo> repeatNameConsList = consList.stream()
				.filter(item -> !item.getConsortiumId().equals(req.getId())).collect(Collectors.toList());
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

	public String createBlockchain(CreateBlockchainRequest req) {
		List<BlockChainPo> blockchains = blockchainDas.findByConsId(req.getConsId());
		Assert.isTrue(CollectionUtils.isEmpty(blockchains), "联盟["+req.getConsId()+"]下已创建区块链");
		
		LocalDateTime now = LocalDateTime.now();
		String operUser = WebHelper.getUserId();
		String blockchainId = UUID.randomUUID().toString();
		BlockChainPo blockchain = BlockChainPo.builder()
				.id(blockchainId)
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
		blockchainDas.createBlockchain(blockchain, cons);
		
		return blockchainId;
	}

	/**
	 * field membership_final_status records final state
	 * field membership_status records processing state
	 * 
	 * --->membership status change
	 * --->add
	 * ADD_START -> ADD_FAILURE
	 * ADD_START -> ADD_CHECK_THREAD_LAUNCH -> ADD_SUCCESS
	 * ADD_START -> ADD_CHECK_THREAD_LAUNCH -> ADD_FAILURE
	 * --->remove
	 * REMOVE_START -> REMOVE_FAILURE
	 * REMOVE_START -> REMOVE_CHECK_THREAD_LAUNCH -> REMOVE_SUCCESS
	 * REMOVE_START -> REMOVE_CHECK_THREAD_LAUNCH -> REMOVE_FAILURE
	 */
	public boolean addNodes(AddNodesRequest req) throws Exception {
		//check
		String consId = req.getConsId();
		String blockchainId = req.getBlockchainId();
		List<NodePo> existNodes = nodeDas.findByConsIdAndChainId(consId, blockchainId);
		existNodes.stream().forEach(item -> checkExistNode(item));
		Map<String, NodePo> existNodeMap = existNodes.stream().collect(
				Collectors.toMap(item -> toIpAtPort(item.getNodeIp(), item.getNodePort()), Functions.identity()));
		Map<String, NodePo> finalStateNodeMap = Maps.newHashMap();
		List<NodeInfo> reqNodes = req.getNodes();
		reqNodes.stream().forEach(item -> {
			checkToAddNode(item, existNodeMap, finalStateNodeMap);
		});
		
		// build
		LocalDateTime now = LocalDateTime.now();
		List<NodePo> toAddNodes = reqNodes.stream()
				.map(item -> buildService.buildToAddNode(consId, blockchainId, item, now)).collect(Collectors.toList());
		List<NodePo> toInsertDbNodes = toAddNodes.stream().filter(
				item -> !existNodeMap.containsKey(toIpAtPort(item.getNodeIp(), item.getNodePort())))
				.collect(Collectors.toList());
		CustomizedConfigBo config = buildCustomizedConfig(consId, toInsertDbNodes/*exclude exist nodes*/);
		
		// before download deploy scripts
		boolean addNodeResult = Boolean.FALSE;
		int establishStep = consortiumDas.findById(consId).get().getEstablishStep();
		if(establishStep < EstablishStep.DOWNLOADED_SCRIPTS.getCode()) {
			ConsortiumPo cons = buildService.buildToUpdateCons(consId, EstablishStep.TO_DEPLOY);
			addNodeResult = nodeDas.addNodes(toInsertDbNodes/* exclude exist nodes */, cons,
					config.getStepFactorConfigList(), config.getConfigList());
		}
		
		// after download deploy scripts, need check chain is launched already
		if(establishStep == EstablishStep.DOWNLOADED_SCRIPTS.getCode()) {
			MembershipChangeBo changeBo = buildService.preMembershipChange(consId, blockchainId,
					existNodes, toAddNodes, null, finalStateNodeMap/*exclude exist final state nodes*/);
			nodeDas.addNodeKeypairs(changeBo.getToInsertDbNodeKeypairs(), config.getStepFactorConfigList(),
					config.getConfigList(), changeBo.getMembershipChange(), changeBo.getNodeMemberships());
			
			MembershipEnum.Status membershipChangeStatus = jsonRpcService.sendMembershipChangeTransactionPlus(
					changeBo.getMembershipChange().getId(), changeBo.getCommitteeNodeKeypairs(),
					changeBo.getValidatorNodeKeypairs());
			List<String> toAddNodeIds = toAddNodes.stream().map(NodePo::getId).collect(Collectors.toList());
			MembershipStatus nodeMembershipStatus = membershipChangeStatus.isFail()
					? MembershipStatus.ADD_FAILURE : MembershipStatus.ADD_CHECK_THREAD_LAUNCH;
			nodeDas.modifyNodesMembershipStatus(toAddNodeIds, nodeMembershipStatus,
					changeBo.getMembershipChange().getId(), membershipChangeStatus);
		}
		
		return addNodeResult;
	}
	
	@LogSkip
	public void downloadDeployScript(DownloadDeployScriptRequest req, HttpServletResponse resp) throws Exception {
		// check and build
		String consortiumId = req.getConsId();
		Assert.notNull(consortiumId, "consortiumId.not.found");
		List<NodePo> nodes = nodeDas.findAll(consortiumId, null);
		Assert.notEmpty(nodes, "node.not.found");
		Map<String, Map<String, String>> nodeScriptMap = buildDeployScript(consortiumId, nodes);
		
		// filter to download script
		Map<String, Map<String, String>> toDownloadNodeScriptMap = Maps.newHashMap();
		List<String> toDownloadNodeIdList = req.getNodeId();
		if(CollectionUtils.isNotEmpty(toDownloadNodeIdList)) {
			Set<String>	toDownloadNodeIdSet = new HashSet<>(toDownloadNodeIdList);
			Set<String> toDownloadNodeKeySet = nodes.stream().filter(item -> toDownloadNodeIdSet.contains(item.getId()))
					.map(item -> item.getNodeIp() + AT + item.getNodePort()).collect(Collectors.toSet());
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
		for(Map.Entry<String, Map<String, String>> item : toDownloadNodeScriptMap.entrySet()){
			String basePath = item.getKey();
			Map<String, String> scripts = item.getValue();
			Iterator<String> iter = scripts.keySet().iterator();
			while(iter.hasNext()) {
				String path = iter.next();
				String content = scripts.get(path);
				zipOutputStream.putNextEntry(new ZipEntry(basePath + FILE_SEP + path));
				zipOutputStream.write(content.getBytes());
				zipOutputStream.closeEntry();
			}
		}
		zipOutputStream.close();
		log.info("write deploy script finished[{}]", consortiumId);
		
		// update consortium establish step
		ConsortiumPo cons = buildService.buildToUpdateCons(consortiumId, EstablishStep.DOWNLOADED_SCRIPTS);
		consortiumDas.modifyCons(cons);
		log.info("update consortium status finished[{}]", consortiumId);
	}

	private Map<String, Map<String, String>> buildDeployScript(String consortiumId, List<NodePo> nodes)
			throws Exception, NoSuchProviderException, NoSuchAlgorithmException, InvalidAlgorithmParameterException {
		// certificate info
		KeypairPo rootKeypair = vaultKeypairBuildService.getRootKeypair(ALGO_CONS);
		KeypairPo consKeypair = vaultKeypairBuildService.getConsortiumKeyPair(consortiumId, ALGO_CONS, rootKeypair);
		PrivateKey privateKey = KeyUtil.privateKeyFromBase64(ALGO_CONS, consKeypair.getPrivateKey());
		
		// nodes info
		StringBuilder consortiumNodes = new StringBuilder();
		List<NodeKeypairBo> nodeKeypairBoList = new ArrayList<>();
		for(NodePo node:nodes){
			NodeKeypairBo nodeKeypairBo = vaultKeypairBuildService.getNodeKeypair(consortiumId, ALGO_NODE, node,
					consKeypair);
			nodeKeypairBoList.add(nodeKeypairBo);
			ECKeyPair ecKeypair = loadECKeyPairFromBase64(nodeKeypairBo.getKeypair().getPublicKey(),
					nodeKeypairBo.getKeypair().getPrivateKey(), nodeKeypairBo.getKeypair().getAlgorithm());
			consortiumNodes.append("cfxnode://");
			consortiumNodes.append(StringUtils.leftPad(ecKeypair.getPublicKey().toString(16), 128, '0') 
					+ AT + nodeKeypairBo.getNode().getNodeIp() + COLON + nodeKeypairBo.getNode().getNodePort());
			if(!node.getId().equalsIgnoreCase(nodes.get(nodes.size()-1).getId())) {
				consortiumNodes.append(COMMA);
			}
		}
		
		// nodes info digest
		byte[] consNodesSHA3 = SignUtil.SHA256Digest(consortiumNodes.toString().getBytes());
		byte[] consNodesDigestCipher = EncryptUtil.codecByPrivateKey(ALGO_CONS, true, privateKey, consNodesSHA3);
		String consNodesDigest = Base64.toBase64String(consNodesDigestCipher);
		
		// node config
		List<NodeCustomizedConfigPo> configList = nodeCustomizedConfigDas.findAll(consortiumId, null);
		Map<String, Map<String, String>> configMap = Maps.newHashMap();
		configList.stream().filter(item -> !item.getNodeId().equals(IntTypeEnum.FALSE.getValue().toString()))
				.collect(Collectors.groupingBy(NodeCustomizedConfigPo::getNodeId))
				.forEach((nodeId, nodeConfigs) -> {
					Map<String, String> nodeConfigMap = nodeConfigs.stream().collect(Collectors
							.toMap(NodeCustomizedConfigPo::getPropertyName, NodeCustomizedConfigPo::getPropertyValue));
					configMap.put(nodeId, nodeConfigMap);
				});
		
		// script content
		Map<String, Map<String, String>> nodeScriptContentMap = new HashMap<>();
		for (NodeKeypairBo nodeKeypair : nodeKeypairBoList) {
			NodePo node = nodeKeypair.getNode();
			KeypairPo keypair = nodeKeypair.getKeypair();
			ECKeyPair ecKeypair = loadECKeyPairFromBase64(keypair.getPublicKey(), keypair.getPrivateKey(),
					keypair.getAlgorithm());
			String ecPub = StringUtils.leftPad(ecKeypair.getPublicKey().toString(16), 128, '0');
			String ecPri = StringUtils.leftPad(ecKeypair.getPrivateKey().toString(16), 64, '0');
			String nodePrivateKeyPem = privateKeyToPem(privateKeyFromBase64(ALGO_NODE, keypair.getPrivateKey()));
			StringBuffer scriptContent = new StringBuffer();
			append(scriptContent, "consortium_cert = \"\"\"" + consKeypair.getCertStr() + "\"\"\"");
			append(scriptContent, "consortium_nodes = \"\"\"" + consortiumNodes.toString() + "\"\"\"");
			append(scriptContent, "consortium_nodes_digest = \"" + consNodesDigest + "\"");
			append(scriptContent, "consortium_keypair = \"" + ecPub + COMMA + ecPri + "\"");
			append(scriptContent, "grpc_server_cert = \"\"\"" + keypair.getCertStr() + "\"\"\"");
			append(scriptContent, "grpc_server_key = \"\"\"" + nodePrivateKeyPem + "\"\"\"");
			Map<String, String> configs = configMap.get(node.getId());
			StringBuffer customizedScriptContent = buildCustomizedDeployScript(node.getId(), ecKeypair, configs);
			Map<String, String> logMonitorScripts = buildLogMonitorScript(node.getId(), configs);
			Map<String, String> sysMonitorScripts = buildSysMonitorScript(node.getId(), configs);
			
			// build script map
			Map<String, String> scripts = Maps.newHashMap();
			scripts.put("config.toml", scriptContent.toString());
			scripts.put("customized_config.toml", customizedScriptContent.toString());
			scripts.putAll(logMonitorScripts);
			scripts.putAll(sysMonitorScripts);
			nodeScriptContentMap.put(node.getNodeIp() + AT + node.getNodePort(), scripts);
		}
		
		// save key pair
		keypairDas.addKeyPairInfo(rootKeypair, consKeypair, nodeKeypairBoList);
		log.info("build deploy script finished[{}]", consortiumId);
		return nodeScriptContentMap;
	}

	private StringBuffer buildCustomizedDeployScript(String nodeId, ECKeyPair ecKeypair, Map<String, String> configs) {
		// node customized script content
		StringBuffer customizedScriptContent = new StringBuffer();
		String jsonRpcHttpPort = configs.get(PROPERTY_JSONRPC_PORT);
		String jsonRpcLocalHttpPort = configs.get(PROPERTY_JSONRPC_LOC_PORT);
		customizedScriptContent.append(PROPERTY_JSONRPC_PORT + " = " + jsonRpcHttpPort)
				.append(LINE_SEP).append(LINE_SEP);
		customizedScriptContent.append(PROPERTY_JSONRPC_LOC_PORT + " = " + jsonRpcLocalHttpPort)
				.append(LINE_SEP).append(LINE_SEP);
		customizedScriptContent.append(PROPERTY_GRPC_PORT + "=" + ( TEST_SWITCH
				? jsonRpcLocalHttpPort + TEST_RPC_PORT_INTERVAL : DEFAULT_GRPC_PORT))
				.append(LINE_SEP).append(LINE_SEP);
		customizedScriptContent.append("mining_author = \"" + ConfluxUtils.toAddress(ecKeypair) + "\"")
				.append(LINE_SEP).append(LINE_SEP);
		customizedScriptContent.append("log_file = './conflux.log'")
				.append(LINE_SEP).append(LINE_SEP);
		customizedScriptContent.append("log_level = \"info\"")
				.append(LINE_SEP).append(LINE_SEP);
		return customizedScriptContent;
	}

	private Map<String, String> buildLogMonitorScript(String nodeId, Map<String, String> configs) {
		Map<String, String> scripts = Maps.newHashMap();
		List<String> files = Lists.newArrayList(
				"log_monitor/start_log_monitor.sh", 
				"log_monitor/stop_log_monitor.sh", 
				"log_monitor/docker-compose.yml", 
				"log_monitor/.env"//from string with placeholder
				);
		files.stream().forEach(item -> {
			String content = null;
			if(item.endsWith(".env")) {
				String webPort = configs.get(PROPERTY_LOG_MONITOR_WEB_PORT);
				String network = configs.get(PROPERTY_LOG_MONITOR_CONTAINER_NETWORK);
				content = MessageFormat.format(MonitorConstant.LOG_MONITOR_ENV_FILE, webPort,
						MonitorConstant.DEFAULT_HOST_LOG_PATH, webPort, network);
			} else {
				content = FileUtils.readFile(new ClassPathResource("scripts/" + item));
				
			}
			scripts.put(item, content);
		});
		return scripts;
	}
	
	private Map<String, String> buildSysMonitorScript(String nodeId, Map<String, String> configs){
		Map<String, String> scripts = Maps.newHashMap();
		List<String> files = Lists.newArrayList(
				"sys_monitor/start_sys_monitor.sh",
				"sys_monitor/stop_sys_monitor.sh",
				"sys_monitor/docker-compose.yml",
				"sys_monitor/.env",//from string with placeholder
				"sys_monitor/grafana/dashboard/home.json",
				"sys_monitor/grafana/datasource/home.ymal",//from string with placeholder
				"sys_monitor/grafana/data/",
				"sys_monitor/prometheus/prometheus.yml"//from string with placeholder
				);
		files.stream().forEach(item -> {
			String content = null;
			String logMonitorWebPort = configs.get(PROPERTY_LOG_MONITOR_WEB_PORT);
			String logMonitorNetwork = configs.get(PROPERTY_LOG_MONITOR_CONTAINER_NETWORK);
			String webPort = (Integer.parseInt(logMonitorWebPort) + TEST_MONITOR_PORT_INTERVAL) + "";
			String highestNetSeg = StringUtils.substringBefore(logMonitorNetwork, DOT);
			String leftNetSeg = StringUtils.substringAfter(logMonitorNetwork, DOT);
			String network = (Integer.parseInt(highestNetSeg) + 1) + DOT + leftNetSeg;
			if(item.endsWith(".env")) {
				content = MessageFormat.format(MonitorConstant.SYS_MONITOR_ENV_FILE, webPort, webPort, network);
			} else if(item.endsWith("home.ymal")) {
				content = MessageFormat.format(MonitorConstant.SYS_MONITOR_GRAFANA_DATASOURCE_FILE, webPort);
			} else if(item.endsWith("prometheus.yml")) {
				content = MessageFormat.format(MonitorConstant.SYS_MONITOR_PROMETHEUS_CONFIG_FILE, webPort);
			} else {
				content = FileUtils.readFile(new ClassPathResource("scripts/" + item));
			}
			scripts.put(item, content);
		});
		return scripts;
	}
	
	private void append(StringBuffer strBuf, String str) {
		strBuf.append(str).append(LINE_SEP).append(LINE_SEP);
	}
	
	private void checkExistNode(NodePo node) {
		int membershipStatus = node.getMembershipStatus();
		if (membershipStatus == MembershipStatus.ADD_START.getCode()
				|| membershipStatus == MembershipStatus.ADD_CHECK_THREAD_LAUNCH.getCode()
				|| membershipStatus == MembershipStatus.REMOVE_START.getCode()
				|| membershipStatus == MembershipStatus.REMOVE_CHECK_THREAD_LAUNCH.getCode()) {
			throw new AlertException(9999, "待添加节点处理中["+membershipStatus+"]，请稍候！");
		}
	}
	
	/**
	 * case 1. not added/delete ever can be added/deleted
	 * case 2. nodes which are added/deleted success or fail can be repeatly added/deleted
	 * @param item
	 * @param existNodeMap
	 * @param existAddSuccessNodeMap
	 * @param existAddFailNodeMap
	 * @param existRemoveSuccessNodeMap
	 * @param existRemoveFailNodeMap
	 */
	private void checkToAddNode(NodeInfo item, Map<String, NodePo> existNodeMap,
			Map<String, NodePo> finalStateNodeMap) {
		String ipPortPair = toIpAtPort(item.getIp(), Integer.parseInt(item.getPort()));
		NodePo node = existNodeMap.get(ipPortPair);
		if (node == null) {
			return;
		}
		checkExistNode(node);
		
		Integer membershipFinalStatus = node.getMembershipFinalStatus();
		if(membershipFinalStatus != null) {
			if (membershipFinalStatus == MembershipStatus.ADD_SUCCESS.getCode()
					|| membershipFinalStatus == MembershipStatus.ADD_FAILURE.getCode()
					|| membershipFinalStatus == MembershipStatus.REMOVE_SUCCESS.getCode()
					|| membershipFinalStatus == MembershipStatus.REMOVE_FAILURE.getCode()) {
				finalStateNodeMap.put(ipPortPair, node);
			}
		}
	}
	
	private CustomizedConfigBo buildCustomizedConfig(String consortiumId, List<NodePo> nodes) {
		String jsonRpcPortStepFactorDb = nodeCustomizedConfigDas.findPropertyValueByName(consortiumId, null,
				PROPERTY_JSONRPC_PORT_STEP_FACTOR);
		int stepFactor = jsonRpcPortStepFactorDb != null ? Integer.parseInt(jsonRpcPortStepFactorDb) : 0;
		
		List<NodeCustomizedConfigPo> configList = Lists.newArrayList();
		for(NodePo node:nodes){
			int jsonRpcHttpPort = TEST_SWITCH
					? DEFAULT_JSONRPC_PORT + (stepFactor++) * TEST_JSONRPC_PORT_STEP
					: DEFAULT_JSONRPC_PORT;
			int jsonRpcLocHttpPort = TEST_SWITCH
					? jsonRpcHttpPort + TEST_RPC_PORT_INTERVAL
					: DEFAULT_JSONRPC_LOCAL_PORT;
			configList.add(buildService.buildNodeConfig(node, PROPERTY_JSONRPC_PORT, jsonRpcHttpPort + ""));
			configList.add(buildService.buildNodeConfig(node, PROPERTY_JSONRPC_LOC_PORT, jsonRpcLocHttpPort + ""));
		}
		
		NodeCustomizedConfigPo jsonRpcStepFactorConfig = buildService.buildNodeConfig(
				NodePo.builder().id(IntTypeEnum.FALSE.getValue() + "").consortiumId(consortiumId).build(),
				PROPERTY_JSONRPC_PORT_STEP_FACTOR, stepFactor + "");
		List<NodeCustomizedConfigPo> stepFactorConfigList = Lists.newArrayList(jsonRpcStepFactorConfig);
		
		CustomizedConfigBo logMonitorConfig = buildCustomizedConfigLogMonitor(consortiumId, nodes);
		configList.addAll(logMonitorConfig.getConfigList());
		stepFactorConfigList.addAll(logMonitorConfig.getStepFactorConfigList());
		
		return CustomizedConfigBo.builder().configList(configList).stepFactorConfigList(stepFactorConfigList).build();
	}
	
	private CustomizedConfigBo buildCustomizedConfigLogMonitor(String consortiumId, List<NodePo> nodes) {
		String webPortStepFactorDb = nodeCustomizedConfigDas.findPropertyValueByName(consortiumId, null,
				PROPERTY_LOG_MONITOR_WEB_PORT_STEP_FACTOR);
		String contNetworkStepFactorDb = nodeCustomizedConfigDas.findPropertyValueByName(consortiumId, null,
				PROPERTY_LOG_MONITOR_CONTAINER_NETWORK_STEP_FACTOR);
		int webPortStepFactor = webPortStepFactorDb != null ? Integer.parseInt(webPortStepFactorDb) : 0;
		int networkStepFactor = contNetworkStepFactorDb != null ? Integer.parseInt(contNetworkStepFactorDb) : 18;
		
		List<NodeCustomizedConfigPo> configList = Lists.newArrayList();
		for(NodePo node:nodes){
			int webPort = TEST_SWITCH
					? DEFAULT_HOST_WEB_PORT + (webPortStepFactor++) * TEST_LOG_MONITOR_WEB_PORT_STEP
					: DEFAULT_HOST_WEB_PORT;
			configList.add(buildService.buildNodeConfig(node, PROPERTY_LOG_MONITOR_WEB_PORT, webPort + ""));
			
			int newNetworkSegment = (networkStepFactor++) * TEST_LOG_MONITOR_CONTAINER_NETWORK_STEP;
			String contNetwork = TEST_SWITCH
					? MessageFormat.format(MonitorConstant.PLACEHOLDER_CONTAINER_NETWORK, newNetworkSegment)
					: DEFAULT_CONTAINER_NETWORK;
			configList.add(buildService.buildNodeConfig(node, PROPERTY_LOG_MONITOR_CONTAINER_NETWORK, contNetwork));
		}
		
		NodeCustomizedConfigPo webPortStepFactorConfig = buildService.buildNodeConfig(
				NodePo.builder().id(IntTypeEnum.FALSE.getValue() + "").consortiumId(consortiumId).build(),
				PROPERTY_LOG_MONITOR_WEB_PORT_STEP_FACTOR, webPortStepFactor + "");
		NodeCustomizedConfigPo networkStepFactorConfig = buildService.buildNodeConfig(
				NodePo.builder().id(IntTypeEnum.FALSE.getValue() + "").consortiumId(consortiumId).build(),
				PROPERTY_LOG_MONITOR_CONTAINER_NETWORK_STEP_FACTOR, networkStepFactor + "");
		List<NodeCustomizedConfigPo> stepFactorConfigList = Lists.newArrayList(webPortStepFactorConfig,
				networkStepFactorConfig);
		
		return CustomizedConfigBo.builder().configList(configList).stepFactorConfigList(stepFactorConfigList).build();
	}
	
	public void deferConsortiumCert(DeferCertificateRequest req) throws Exception {
		// check
		String consId = req.getConsId();
		consortiumDas.findById(consId).orElseThrow(() -> new AlertException("consortium.not.found"));
		List<CertificateDeferPo> deferRequests = certificateDeferDas.findByConsortiumId(consId,
				Lists.newArrayList(DeferStatus.INITIAL.getCode(), DeferStatus.PROCESSING.getCode()));
		if(CollectionUtils.isNotEmpty(deferRequests)) {
			throw new AlertException("processing.defer.exist");
		}
		
		// get not after of last certificate
		List<KeypairPo> consKeypairs = keypairDas.findAll(consId, KeyType.CONSORTIUM.getCode(), ALGO_CONS);
		Date notAfter = null;
		if(CollectionUtils.isNotEmpty(consKeypairs)) {
			KeypairPo consKeypairDb = consKeypairs.get(0);
			X509Certificate cert = CertUtil.certFromPem(consKeypairDb.getCertStr());
			notAfter = cert.getNotAfter();
		}
		
		// build defer request
		KeypairPo rootKeypair = vaultKeypairBuildService.getRootKeypair(ALGO_CONS);
		KeypairPo consKeypair = vaultKeypairBuildService.geneConsKeyPair(consId, ALGO_CONS, rootKeypair, notAfter);
		CertificateDeferPo defer = buildService.buildCertificateDefer(consKeypair);
		certificateDeferDas.createCertificateDefer(defer);
		
		// get membership nodes and send request
		List<NodePo> existNodes = nodeDas.findByConsIdAndChainId(consId, req.getBlockchainId());
		List<String> membershipNodeIds = existNodes.stream().filter(item -> buildService.isMembershipNode(item))
				.map(NodePo::getId).collect(Collectors.toList());
		String certStr = consKeypair.getCertStr();
		boolean isSuccess = jsonRpcService.updateCert(consId, certStr, membershipNodeIds);
		
		// response failure
		CertificateDeferPo deferUpdate = buildService.buildCertificateDeferUpdate(defer.getId());
		if(!isSuccess) {
			deferUpdate.setStatus(DeferStatus.FAILURE.getCode());
			certificateDeferDas.modifyCertificateDefer(deferUpdate);
			return;
		}
		
		// response success
		deferUpdate.setStatus(DeferStatus.SUCCESS.getCode());
		List<Long> oldKeypairIds = consKeypairs.stream().map(KeypairPo::getId).collect(Collectors.toList());
		KeypairPo newKeypair = buildService.buildConsortiumKeypairPoFromDefer(defer);
		certificateDeferDas.modifyCertificateDefer(deferUpdate, oldKeypairIds, IntTypeEnum.TRUE.getValue(), newKeypair);
	}
}
