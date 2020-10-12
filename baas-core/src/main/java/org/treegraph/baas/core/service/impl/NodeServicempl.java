package org.treegraph.baas.core.service.impl;

import static org.treegraph.baas.core.util.ConfluxUtils.toUri;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.treegraph.baas.api.dto.NodeInfo;
import org.treegraph.baas.api.dto.request.DeleteNodeRequest;
import org.treegraph.baas.api.dto.request.ListNodeRequest;
import org.treegraph.baas.api.dto.request.ModifyNodeRequest;
import org.treegraph.baas.api.dto.request.QueryNodeDetailRequest;
import org.treegraph.baas.api.dto.request.QueryNodeLogRequest;
import org.treegraph.baas.api.dto.response.ListNodeResponse;
import org.treegraph.baas.common.util.WebHelper;
import org.treegraph.baas.core.constant.ConfluxConstant;
import org.treegraph.baas.core.das.BlockchainDas;
import org.treegraph.baas.core.das.ConsortiumDas;
import org.treegraph.baas.core.das.NodeCustomizedConfigDas;
import org.treegraph.baas.core.das.NodeDas;
import org.treegraph.baas.core.domain.BlockChainPo;
import org.treegraph.baas.core.domain.ConsortiumPo;
import org.treegraph.baas.core.domain.NodePo;
import org.treegraph.baas.core.enums.ConsortiumEnum.EstablishStep;
import org.treegraph.baas.core.enums.MembershipEnum;
import org.treegraph.baas.core.enums.NodeEnum;
import org.treegraph.baas.core.enums.NodeEnum.MembershipStatus;
import org.treegraph.baas.core.enums.NodeEnum.Status;
import org.treegraph.baas.core.exception.AlertException;
import org.treegraph.baas.core.heartbeat.impl.HeartbeatLinstener;
import org.treegraph.baas.core.service.NodeService;
import org.treegraph.baas.core.service.bo.MembershipChangeBo;
import org.treegraph.baas.core.util.DateTimeUtils;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class NodeServicempl implements NodeService {

    private NodeDas nodeDas;
    
    private ConsortiumDas consortiumDas;
    
    private BlockchainDas blockchainDas;
    
    private NodeCustomizedConfigDas nodeCustomizedConfigDas;
    
    private BuildService buildService;
    
    private JsonRpcService jsonRpcService;
 
	public ListNodeResponse listNodes(ListNodeRequest req) {
		PageHelper.startPage(req.getCurrent(), req.getPageSize());
    	List<NodePo> nodes = nodeDas.findAll(req.getConsId(), req.getIp());
    	PageInfo<NodePo> pageInfo = new PageInfo<NodePo>(nodes);
    	
		List<NodePo> nodeList = pageInfo.getList();
		List<String> consIds = nodeList.stream().map(NodePo::getConsortiumId).collect(Collectors.toList());
		List<ConsortiumPo> cons = consortiumDas.findByIds(consIds);
		Map<String, String> consIdNameMap = cons.stream()
				.collect(Collectors.toMap(ConsortiumPo::getConsortiumId, ConsortiumPo::getName));
    	
		List<String> blockchainIds = nodeList.stream().map(NodePo::getBlockchainId).collect(Collectors.toList());
		List<BlockChainPo> blockchains = blockchainDas.findByIds(blockchainIds);
		Map<String, String> blockchainIdNameMap = blockchains.stream()
				.collect(Collectors.toMap(BlockChainPo::getId, BlockChainPo::getBlockchainName));
    	
    	List<NodeInfo> nodeInfos = nodeList.stream().map(item -> {
			Status nodeStatus = HeartbeatLinstener.getInstance().getNodeStatus(item.getConsortiumId(), item.getId());
			
			return NodeInfo.builder()
					.id(item.getId())
					.ip(item.getNodeIp())
					.port(item.getNodePort() + "")
					.org(item.getOrganizationName())
					.consId(item.getConsortiumId())
					.consName(consIdNameMap.get(item.getConsortiumId()))
					.blockchainId(item.getBlockchainId())
					.blockchainName(blockchainIdNameMap.get(item.getBlockchainId()))
					.createTime(DateTimeUtils.formatTime(item.getCreateTime(), DateTimeUtils.FORMAT_yyyyMMddHHmmss))
					.status(nodeStatus.getCode())
					.build();
		}).collect(Collectors.toList());
    	
    	return ListNodeResponse.builder()
    			.current(req.getCurrent())
				.pageSize(req.getPageSize())
    			.total(pageInfo.getTotal())
    			.list(nodeInfos)
    			.build();
	}
	
	public NodeInfo getNodeDetail(QueryNodeDetailRequest req) {
		NodePo node = nodeDas.findById(req.getId()).orElseThrow(() -> new AlertException("node.not.found"));
		ConsortiumPo consortium = consortiumDas.findById(node.getConsortiumId())
				.orElseThrow(() -> new AlertException("consortium.not.found"));
		BlockChainPo blockchain = blockchainDas.findById(node.getBlockchainId())
				.orElseThrow(() -> new AlertException("blockchain.not.found"));
		Status nodeStatus = HeartbeatLinstener.getInstance().getNodeStatus(node.getConsortiumId(), node.getId());
		String logMonitorPort = nodeCustomizedConfigDas.findPropertyValueByName(node.getConsortiumId(), node.getId(),
				ConfluxConstant.PROPERTY_LOG_MONITOR_WEB_PORT);
		return NodeInfo.builder()
				.consId(node.getConsortiumId())
				.consName(consortium.getName())
				.blockchainId(node.getBlockchainId())
				.blockchainName(blockchain.getBlockchainName())
				.ip(node.getNodeIp())
				.port(node.getNodePort()+"")
				.org(node.getOrganizationName())
				.status(nodeStatus.getCode())
				.logMonitorUri(toUri(node.getNodeIp(), Integer.parseInt(logMonitorPort)))
				.build();
	}

	public String getNodeLog(QueryNodeLogRequest req) {
		return null;
	}

	public boolean modifyNode(ModifyNodeRequest req) {
		NodePo node = nodeDas.findById(req.getId()).orElseThrow(() -> new AlertException("node.not.found"));
		Assert.isTrue(!NodeEnum.Status.isRuning(node.getStatus()),
				"节点在运行中或部署中，不可修改[" + req.getIp() + ":" + req.getPort() + "]");
		
		NodePo nodePo = NodePo.builder().id(req.getId())
				.nodeIp(req.getIp())
				.nodePort(req.getPort() != null ? Integer.parseInt(req.getPort()) : null)
				.organizationName(req.getOrg())
				.updateTime(LocalDateTime.now())
				.updateBy(WebHelper.getUserId())
				.build();
		
		return nodeDas.modifyNode(nodePo);
	}
	
	public boolean deleteNode(DeleteNodeRequest req) throws Exception {
		//check
		NodePo node = nodeDas.findById(req.getId()).orElseThrow(() -> new AlertException("node.not.found"));
		String consId = node.getConsortiumId();
		String blockchainId = node.getBlockchainId();
		
		// before download deploy scripts
		boolean deleteNodeResult = Boolean.FALSE;
		int establishStep = consortiumDas.findById(consId).get().getEstablishStep();
		if (req.getForce() || establishStep < EstablishStep.DOWNLOADED_SCRIPTS.getCode()) {
			ConsortiumPo cons = buildService.buildToUpdateCons(consId, EstablishStep.TO_DEPLOY);
			deleteNodeResult = nodeDas.deleteNodes(node, cons);
		}
		
		// after download deploy scripts, need check chain is launched already
		if(!req.getForce() && establishStep == EstablishStep.DOWNLOADED_SCRIPTS.getCode()) {
			List<NodePo> existNodes = nodeDas.findByConsIdAndChainId(consId, blockchainId);
			List<NodePo> toRemoveNodes = Lists.newArrayList(node);
			
			MembershipChangeBo changeBo = buildService.preMembershipChange(consId, blockchainId,
					existNodes, null, toRemoveNodes, null);
			List<String> toRemoveNodeIds = Lists.newArrayList(node.getId());
			nodeDas.addNodesMembershipChange(toRemoveNodeIds, MembershipStatus.REMOVE_START,
					changeBo.getMembershipChange(), changeBo.getNodeMemberships());
			
			MembershipEnum.Status membershipChangeStatus = jsonRpcService.sendMembershipChangeTransactionPlus(
					changeBo.getMembershipChange().getId(), changeBo.getCommitteeNodeKeypairs(),
					changeBo.getValidatorNodeKeypairs());
			MembershipStatus nodeMembershipStatus = membershipChangeStatus.isFail() ? MembershipStatus.REMOVE_FAILURE
					: MembershipStatus.REMOVE_CHECK_THREAD_LAUNCH;
			nodeDas.modifyNodesMembershipStatus(toRemoveNodeIds, nodeMembershipStatus,
					changeBo.getMembershipChange().getId(), membershipChangeStatus);
		}
		
		return deleteNodeResult;
	}
}
