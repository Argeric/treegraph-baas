package org.treegraph.baas.core.service.impl;

import static org.treegraph.baas.core.constant.X509CertConstant.ALGO_CONS;
import static org.treegraph.baas.core.constant.X509CertConstant.ALGO_NODE;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.treegraph.baas.api.dto.NodeInfo;
import org.treegraph.baas.api.dto.Role;
import org.treegraph.baas.common.enums.IntTypeEnum;
import org.treegraph.baas.common.util.WebHelper;
import org.treegraph.baas.core.constant.SplitterConstant;
import org.treegraph.baas.core.das.PermissionDas;
import org.treegraph.baas.core.das.bo.NodeKeypairBo;
import org.treegraph.baas.core.domain.CertificateDeferPo;
import org.treegraph.baas.core.domain.ConsortiumPo;
import org.treegraph.baas.core.domain.KeypairPo;
import org.treegraph.baas.core.domain.MembershipChangePo;
import org.treegraph.baas.core.domain.MembershipPo;
import org.treegraph.baas.core.domain.NodeCustomizedConfigPo;
import org.treegraph.baas.core.domain.NodePo;
import org.treegraph.baas.core.domain.RolePermissionPo;
import org.treegraph.baas.core.domain.RolePo;
import org.treegraph.baas.core.domain.UserInfoPo;
import org.treegraph.baas.core.domain.UserOrganizationPo;
import org.treegraph.baas.core.domain.UserRolePo;
import org.treegraph.baas.core.enums.CertificateEnum.DeferStatus;
import org.treegraph.baas.core.enums.ConsortiumEnum.EstablishStep;
import org.treegraph.baas.core.enums.CryptoEnum.KeyType;
import org.treegraph.baas.core.enums.HeartbeatEnum;
import org.treegraph.baas.core.enums.MembershipEnum;
import org.treegraph.baas.core.enums.NodeEnum;
import org.treegraph.baas.core.enums.NodeEnum.MembershipStatus;
import org.treegraph.baas.core.exception.AlertException;
import org.treegraph.baas.core.heartbeat.impl.HeartbeatLinstener;
import org.treegraph.baas.core.enums.UserEnum;
import org.treegraph.baas.core.service.bo.MembershipChangeBo;
import org.treegraph.baas.core.service.bo.OrgBo;
import org.treegraph.baas.core.service.bo.RoleInfoBo;
import org.treegraph.baas.core.service.bo.UserInfoBo;
import org.treegraph.baas.core.service.bo.UserOrgBo;
import org.treegraph.baas.core.util.ConfluxUtils;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Functions;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Validated
@Service
@AllArgsConstructor
@Slf4j
public class BuildService {
	
    private PermissionDas permissionDas;
	
    private PasswordService passwordService;
    
    private VaultKeypairBuildService vaultKeypairBuildService;
    
    private JsonRpcService jsonRpcService;

	public RoleInfoBo buildRole(int roleType, String roleName, String consId, List<String> reqPermissions) {
   		LocalDateTime now = LocalDateTime.now();
   		String oper = WebHelper.getUserId();
   		RolePo role = RolePo.builder()
   				.id(UUID.randomUUID().toString())
   				.consortiumId(consId)
   				.roleName(roleName)
   				.roleType(roleType)
   				.deleteFlag(IntTypeEnum.FALSE.getValue())
   				.createTime(now)
   				.createBy(oper)
   				.updateTime(now)
   				.updateBy(oper)
   				.build();
   		
   		List<RolePermissionPo> rolePermissionList = Lists.newArrayList();
		if(CollectionUtils.isNotEmpty(reqPermissions)) {
			Map<String, Long> permissionCodeIdMap = permissionDas.validatePermissions(reqPermissions);
   			reqPermissions.stream().forEach(item -> {
   				RolePermissionPo rolePerm = RolePermissionPo.builder()
   						.roleId(role.getId())
   						.permissionId(permissionCodeIdMap.get(item))
   						.consortiumId(consId)
   						.deleteFlag(IntTypeEnum.FALSE.getValue())
   						.createTime(now)
   						.createBy(oper)
   						.updateTime(now)
   						.updateBy(oper)
   						.build();
   				rolePermissionList.add(rolePerm);
   			});
   		}
		
		return RoleInfoBo.builder()
				.role(role)
				.rolePermissionList(rolePermissionList)
				.build();
	}
	
	public UserInfoBo buildUser(String account, String name, String phone, String email, String pwd, List<Role> roles,
			List<OrgBo> orgs) {
		LocalDateTime now = LocalDateTime.now();
		String operUser = WebHelper.getUserId();
		UserInfoPo user = UserInfoPo.builder()
				.id(UUID.randomUUID().toString())
				.account(account)
				.name(name)
				.phone(phone)
				.email(email)
				.password(passwordService.bcryptEncoder(pwd))
				.status(UserEnum.Status.AVAILABLE.getCode())
				.deleteFlag(IntTypeEnum.FALSE.getValue())
				.createTime(now)
				.createBy(operUser)
				.updateTime(now)
				.updateBy(operUser)
				.build();
		
		List<UserRolePo> userRoles = roles.stream().map(item -> UserRolePo.builder()
				.userId(user.getId())
				.roleId(item.getRoleId())
				.consortiumId(item.getConsId())
				.deleteFlag(IntTypeEnum.FALSE.getValue())
				.createTime(now)
				.createBy(operUser)
				.updateTime(now)
				.updateBy(operUser)
				.build()).collect(Collectors.toList());
		
		List<UserOrganizationPo> userOrgs = orgs.stream().map(item -> UserOrganizationPo.builder()
				.userId(user.getId())
				.consortiumId(item.getConsortiumId())
				.organizationName(item.getOrganizationName())
				.deleteFlag(IntTypeEnum.FALSE.getValue())
				.createTime(now)
				.updateTime(now)
				.build()).collect(Collectors.toList());
		
		return UserInfoBo.builder()
				.user(user)
				.userRoles(userRoles)
				.userOrgs(userOrgs)
				.build();
	}
	
	public List<UserRolePo> buildUserRoles(String userId, List<Role> roles) {
		LocalDateTime now = LocalDateTime.now();
		String operUser = WebHelper.getUserId();
		
		return roles.stream().map(item -> UserRolePo.builder()
				.userId(userId)
				.roleId(item.getRoleId())
				.consortiumId(item.getConsId())
				.deleteFlag(IntTypeEnum.FALSE.getValue())
				.createTime(now)
				.createBy(operUser)
				.updateTime(now)
				.updateBy(operUser)
				.build()).collect(Collectors.toList());
	}
	
	public UserOrgBo buildModifyUserOrg(List<UserOrganizationPo> dbUserOrgs, List<UserOrganizationPo> reqUserOrgs) {
		Set<String> dbOrgs = dbUserOrgs.stream().map(item -> this.buildUserOrgIdentifier(item))
				.collect(Collectors.toSet());
		Set<String> reqOrgs = reqUserOrgs.stream().map(item -> this.buildUserOrgIdentifier(item))
				.collect(Collectors.toSet());
		
		Set<String> intersetOrgs = Sets.newHashSet();
		intersetOrgs.addAll(dbOrgs);
		intersetOrgs.retainAll(reqOrgs);
		reqOrgs.removeAll(intersetOrgs);log.info("reqOrgs======>"+JSON.toJSONString(reqOrgs));
		dbOrgs.removeAll(intersetOrgs);log.info("dbOrgs======>"+JSON.toJSONString(dbOrgs));
		
		List<UserOrganizationPo> toAddUserOrgs = reqOrgs.stream().map(item -> this.buildUserOrg(item))
				.collect(Collectors.toList());
		Map<String, UserOrganizationPo> dbOrgMap = dbUserOrgs.stream()
				.collect(Collectors.toMap(item -> this.buildUserOrgIdentifier(item), Functions.identity()));
		List<Long> toDelUserOrgIds = dbOrgs.stream().map(item -> dbOrgMap.get(item).getId())
				.collect(Collectors.toList());

		return UserOrgBo.builder()
				.toAddUserOrgs(toAddUserOrgs)
				.toDelUserOrgIds(toDelUserOrgIds)
				.build();
	}
	
	private String buildUserOrgIdentifier(UserOrganizationPo usrOrg) {
		return new StringBuilder(usrOrg.getUserId()).append(",").append(usrOrg.getConsortiumId()).append(",")
				.append(usrOrg.getOrganizationName()).toString();
	}
	
	private UserOrganizationPo buildUserOrg(String identifier) {
		String[] params = identifier.split(",");
		System.out.println(JSON.toJSONString(params));
		LocalDateTime now = LocalDateTime.now();
		return UserOrganizationPo.builder()
				.userId(params[0])
				.consortiumId(params[1])
				.organizationName(params[2])
				.deleteFlag(IntTypeEnum.FALSE.getValue())
				.createTime(now)
				.updateTime(now)
				.build();
	}

	public ConsortiumPo buildToUpdateCons(String consId, EstablishStep step) {
		return ConsortiumPo.builder()
				.consortiumId(consId)
				.establishStep(step.getCode())
				.updateTime(LocalDateTime.now())
				.updateBy(WebHelper.getUserId())
				.build();
	}
	
	public NodeCustomizedConfigPo buildNodeCustomizedConfig(NodePo node, String key, String value) {
		LocalDateTime now = LocalDateTime.now();
		String operUser = WebHelper.getUserId();
		return NodeCustomizedConfigPo.builder()
			.consortiumId(node.getConsortiumId())
			.nodeId(node.getId())
			.propertyName(key)
			.propertyValue(value)
			.deleteFlag(IntTypeEnum.FALSE.getValue())
			.createTime(now)
			.createBy(operUser)
			.updateTime(now)
			.updateBy(operUser)
			.build();
	}
	
	public MembershipChangeBo preMembershipChange(String consId, String blockchainId, List<NodePo> existNodes,
			List<NodePo> toAddNodes, List<NodePo> toRemoveNodes, Map<String, NodePo> finalStateNodeMap)
			throws Exception {
		// process null case
		toAddNodes = CollectionUtils.isEmpty(toAddNodes) ? Lists.newArrayList() : toAddNodes;
		toRemoveNodes = CollectionUtils.isEmpty(toRemoveNodes) ? Lists.newArrayList() : toRemoveNodes;
		boolean isAddNode = toAddNodes.size() > 0;
		
		// get classified node keypairs
		List<NodeKeypairBo> existDbNodeKeypairs = existNodes.stream().map(
				item -> vaultKeypairBuildService.getNodeKeypair(consId, ALGO_NODE, item, null))
				.collect(Collectors.toList());
		KeypairPo consKeyPair = vaultKeypairBuildService.getConsortiumKeyPair(consId, ALGO_CONS, null);
		List<NodeKeypairBo> toAddNodeKeypairs = toAddNodes.stream().map(item -> {
				NodeKeypairBo nodeKeypair = vaultKeypairBuildService.getNodeKeypair(consId, ALGO_NODE, item, consKeyPair);
				nodeKeypair.getNode().setMembershipStatus(MembershipStatus.ADD_START.getCode());
				return nodeKeypair;
				}).collect(Collectors.toList());
		List<NodeKeypairBo> toInsertDbNodeKeypairs = toAddNodeKeypairs.stream().filter(item -> {
				String uri = ConfluxUtils.toIpAtPort(item.getNode().getNodeIp(), item.getNode().getNodePort());
				return !finalStateNodeMap.containsKey(uri);
				}).collect(Collectors.toList());
		List<NodeKeypairBo> toRemoveNodeKeypairs = toRemoveNodes.stream().map(
				item -> vaultKeypairBuildService.getNodeKeypair(consId, ALGO_NODE, item, null))
				.collect(Collectors.toList());
		
		// get node keypairs for rpc of add/delete nodes
		List<NodeKeypairBo> membershipNodeKeypairs = existDbNodeKeypairs.stream()
				.filter(item -> isMembershipNode(item.getNode())).collect(Collectors.toList());
		Assert.isTrue(CollectionUtils.isNotEmpty(membershipNodeKeypairs), "membership.node.not.found");
		List<NodeKeypairBo> committeeNodeKeypairs = this.getNodeKeypairForCommittee(membershipNodeKeypairs,
				isAddNode ? null : toRemoveNodeKeypairs);
		List<NodeKeypairBo> validatorNodeKeypairs = this.getNodeKeypairForValidator(membershipNodeKeypairs,
				isAddNode ? toAddNodeKeypairs : null, isAddNode ? null : toRemoveNodeKeypairs);
		
		// record pre-bftEpochNumber、committeeNodeIds for this add/delete nodes request
		NodeKeypairBo proposerNodeKeypair = committeeNodeKeypairs.stream()
				.filter(item -> HeartbeatEnum.Status.GREEN == HeartbeatLinstener.getInstance()
						.getNode(item.getNode().getConsortiumId(), item.getNode().getId()).getStatus())
				.findFirst().orElseThrow(() -> new AlertException("alive.proposer.node.not.found"));
		NodePo proposerNode = proposerNodeKeypair.getNode();
		BigInteger preBftEpochNumber = jsonRpcService.getBftEpochNumber(proposerNode.getConsortiumId(), proposerNode.getId());
		List<String> committeeNodeIds = committeeNodeKeypairs.stream().map(item -> item.getNode().getId())
				.collect(Collectors.toList());
		MembershipChangePo membershipChange = this.buildMembershipChangePo(consId, blockchainId,
				isAddNode ? MembershipEnum.Type.ADD.getCode() : MembershipEnum.Type.REMOVE.getCode(), preBftEpochNumber,
				committeeNodeIds);
		List<NodePo> membershipChangeNodes = isAddNode ? toAddNodes : toRemoveNodes;
		List<MembershipPo> nodeMemberships = membershipChangeNodes.stream().map(item -> this.buildMembership(null,
				item.getConsortiumId(), item.getId(), item.getNodeIp(), item.getNodePort()))
				.collect(Collectors.toList());
		
		return MembershipChangeBo.builder()
				.existDbNodeKeypairs(existDbNodeKeypairs)
				.toAddNodeKeypairs(toAddNodeKeypairs)
				.toInsertDbNodeKeypairs(toInsertDbNodeKeypairs)
				.toRemoveNodeKeypairs(toRemoveNodeKeypairs)
				.membershipNodeKeypairs(membershipNodeKeypairs)
				.committeeNodeKeypairs(committeeNodeKeypairs)
				.validatorNodeKeypairs(validatorNodeKeypairs)
				.membershipChange(membershipChange)
				.nodeMemberships(nodeMemberships)
				.build();
	}

	public boolean isMembershipNode(NodePo node) {
		return node.getMembershipFinalStatus() != null
				&& (node.getMembershipFinalStatus() == MembershipStatus.ADD_SUCCESS.getCode() || 
					node.getMembershipFinalStatus() == MembershipStatus.TO_LAUNCH_FIRST_TIME.getCode());
	}
	
	public MembershipChangePo buildMembershipChangePo(String consId, String blockchainId, int changeType,
			BigInteger preBftEpochNumber, List<String> committeeNodeIds) {
		LocalDateTime now = LocalDateTime.now();
		String operUser = WebHelper.getUserId();
		return  MembershipChangePo.builder()
			.consortiumId(consId)
			.blockchainId(blockchainId)
			.changeType(changeType)
			.status(MembershipEnum.Status.INIT.getCode())
			.committeeNodes(SplitterConstant.COMMA_JOINER.join(committeeNodeIds))
			.preBftEpochNumber(preBftEpochNumber.longValue())
			.createTime(now)
			.updateTime(now)
			.createBy(operUser)
			.updateBy(operUser)
			.build();
	}
	
	public MembershipPo buildMembership(Long membershipChangeId, String consId, String nodeId, String nodeIp,
			int nodePort) {
		return MembershipPo.builder()
			.membershipChangeId(membershipChangeId)
			.consortiumId(consId)
			.nodeId(nodeId)
			.nodeIp(nodeIp)
			.nodePort(nodePort)
			.build();
	}
	
	public List<NodeKeypairBo> getNodeKeypairForCommittee(List<NodeKeypairBo> existNodeKeypairs,
			List<NodeKeypairBo> removeNodeKeypairs) {
    	List<NodeKeypairBo> leftNodeKeypairs = Lists.newArrayList(existNodeKeypairs);
    	if(CollectionUtils.isNotEmpty(removeNodeKeypairs)) {
			Set<String> toRemoveNodeIdSet = removeNodeKeypairs.stream().map(item -> item.getNode().getId())
					.collect(Collectors.toSet());
			leftNodeKeypairs = leftNodeKeypairs.stream()
					.filter(item -> !toRemoveNodeIdSet.contains(item.getNode().getId())).collect(Collectors.toList());
    	}
    	
    	return leftNodeKeypairs;
    }
    
	public List<NodeKeypairBo> getNodeKeypairForValidator(List<NodeKeypairBo> existNodeKeypairs,
			List<NodeKeypairBo> addNodeKeypairs, List<NodeKeypairBo> removeNodeKeypairs) {
    	List<NodeKeypairBo> leftNodeKeypairs = Lists.newArrayList(existNodeKeypairs);
    	if(CollectionUtils.isNotEmpty(removeNodeKeypairs)) {
			Set<String> toRemoveNodeIdSet = removeNodeKeypairs.stream().map(item -> item.getNode().getId())
					.collect(Collectors.toSet());
			leftNodeKeypairs = leftNodeKeypairs.stream()
					.filter(item -> !toRemoveNodeIdSet.contains(item.getNode().getId())).collect(Collectors.toList());
    	}
    	if(CollectionUtils.isNotEmpty(addNodeKeypairs)) {
    		leftNodeKeypairs.addAll(addNodeKeypairs);
    	}
    	
    	return leftNodeKeypairs;
    }
	
	public CertificateDeferPo buildCertificateDefer(KeypairPo keypair) {
		LocalDateTime now = LocalDateTime.now();
		String operUser = WebHelper.getUserId();
		return CertificateDeferPo.builder()
				.publicKey(keypair.getPublicKey())
				.privateKey(keypair.getPrivateKey())
				.csrStr(keypair.getCsrStr())
				.certStr(keypair.getCertStr())
				.algorithm(keypair.getAlgorithm())
				.consortiumId(keypair.getConsortiumId())
				.status(DeferStatus.INITIAL.getCode())
				.deleteFlag(IntTypeEnum.FALSE.getValue())
				.createTime(now)
				.createBy(operUser)
				.updateTime(now)
				.updateBy(operUser)
				.build();
	}
	
	public CertificateDeferPo buildCertificateDeferUpdate(Long id) {
		return CertificateDeferPo.builder()
				.id(id)
				.updateTime(LocalDateTime.now())
				.updateBy(WebHelper.getUserId())
				.build();
	}
	
	public KeypairPo buildConsortiumKeypairPoFromDefer(CertificateDeferPo defer) {
		LocalDateTime now = LocalDateTime.now();
		String operUser = WebHelper.getUserId();
		return KeypairPo.builder()
				.publicKey(defer.getPublicKey())
				.privateKey(defer.getPrivateKey())
				.csrStr(defer.getCsrStr())
				.certStr(defer.getCertStr())
				.algorithm(defer.getAlgorithm())
				.consortiumId(defer.getConsortiumId())
				.keyType(KeyType.CONSORTIUM.getCode())
				.deleteFlag(IntTypeEnum.FALSE.getValue())
				.createTime(now)
				.createBy(operUser)
				.updateTime(now)
				.updateBy(operUser)
				.build();
	}
	
	public NodePo buildToAddNode(String consId, String blockchainId, NodeInfo nodeInfo, LocalDateTime now) {
		String operUser = WebHelper.getUserId();
		return NodePo.builder()
				.id(UUID.randomUUID().toString())
				.consortiumId(consId)
				.blockchainId(blockchainId)
				.organizationName(nodeInfo.getOrg())
				.nodeIp(nodeInfo.getIp())
				.nodePort(Integer.parseInt(nodeInfo.getPort()))
				.status(NodeEnum.Status.TO_LAUNCH.getCode())
				.membershipFinalStatus(MembershipStatus.TO_LAUNCH_FIRST_TIME.getCode())
				.membershipStatus(MembershipStatus.TO_LAUNCH_FIRST_TIME.getCode())
				.deleteFlag(IntTypeEnum.FALSE.getValue())
				.createTime(now)
				.createBy(operUser)
				.updateTime(now)
				.updateBy(operUser)
				.build();
	}
}
