package org.treegraph.baas.core.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.treegraph.baas.api.dto.NodeInfo;
import org.treegraph.baas.api.dto.request.ListNodeRequest;
import org.treegraph.baas.api.dto.request.ModifyNodeRequest;
import org.treegraph.baas.api.dto.request.QueryNodeDetailRequest;
import org.treegraph.baas.api.dto.request.QueryNodeLogRequest;
import org.treegraph.baas.api.dto.response.ListNodeResponse;
import org.treegraph.baas.common.util.WebHelper;
import org.treegraph.baas.core.das.BlockchainDas;
import org.treegraph.baas.core.das.ConsortiumDas;
import org.treegraph.baas.core.das.NodeDas;
import org.treegraph.baas.core.domain.BlockChainPo;
import org.treegraph.baas.core.domain.ConsortiumPo;
import org.treegraph.baas.core.domain.NodePo;
import org.treegraph.baas.core.enums.NodeEnum;
import org.treegraph.baas.core.exception.AlertException;
import org.treegraph.baas.core.service.NodeService;
import org.treegraph.baas.core.util.DateTimeUtils;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class NodeServicempl implements NodeService {

    private NodeDas nodeDas;
    
    private ConsortiumDas consortiumDas;
    
    private BlockchainDas blockchainDas;
    
    private HeartbeatService heartbeatService;
 
	public ListNodeResponse listNodes(ListNodeRequest req) {
		
		PageHelper.startPage(req.getCurrent(), req.getPageSize());
    	List<NodePo> nodes = nodeDas.findAll(req.getConsId(), req.getIp());
    	PageInfo<NodePo> pageInfo = new PageInfo<NodePo>(nodes);
    	
    	List<NodePo> nodeList = pageInfo.getList();
    	List<String> consIds = nodeList.stream().map(NodePo::getConsortiumId).collect(Collectors.toList());
    	List<ConsortiumPo> cons = consortiumDas.findByIds(consIds);
    	Map<String, String> consIdNameMap = cons.stream().collect(Collectors.toMap(ConsortiumPo::getConsortiumId, ConsortiumPo::getName));
    	
    	List<String> blockchainIds = nodeList.stream().map(NodePo::getBlockchainId).collect(Collectors.toList());
    	List<BlockChainPo> blockchains = blockchainDas.findByIds(blockchainIds);
    	Map<String, String> blockchainIdNameMap = blockchains.stream().collect(Collectors.toMap(BlockChainPo::getId, BlockChainPo::getBlockchainName));
    	
    	
    	List<NodeInfo> nodeInfos = nodeList.stream().map(item -> NodeInfo.builder()
    			.id(item.getId())
    			.ip(item.getNodeIp())
    			.port(item.getNodePort()+"")
    			.org(item.getOrganizationName())
    			.consId(item.getConsortiumId())
    			.consName(consIdNameMap.get(item.getConsortiumId()))
    			.blockchainId(item.getBlockchainId())
    			.blockchainName(blockchainIdNameMap.get(item.getBlockchainId()))
    			.createTime(DateTimeUtils.formatTime(item.getCreateTime(), DateTimeUtils.FORMAT_yyyyMMddHHmmss))
    			.status(getNodeStatus(item.getStatus(), item.getNodeIp(), item.getNodePort()))
    			.build()).collect(Collectors.toList());
    	
    	return ListNodeResponse.builder()
    			.current(req.getCurrent())
				.pageSize(req.getPageSize())
    			.total(pageInfo.getTotal())
    			.list(nodeInfos)
    			.build();
	}
	
	public NodeInfo getNodeDetail(QueryNodeDetailRequest req) {
		NodePo nodePo = nodeDas.findById(req.getId()).orElseThrow(() -> new AlertException("node.not.found"));
		return NodeInfo.builder()
				.ip(nodePo.getNodeIp())
				.port(nodePo.getNodePort()+"")
				.org(nodePo.getOrganizationName())
				.status(getNodeStatus(nodePo.getStatus(), nodePo.getNodeIp(), nodePo.getNodePort()))
				.build();
	}

	public String getNodeLog(QueryNodeLogRequest req) {
		
		return null;
	}

	public boolean modifyNode(ModifyNodeRequest req) {
		NodePo node = nodeDas.findById(req.getId()).orElseThrow(() -> new AlertException("node.not.found"));
		Assert.isTrue(!NodeEnum.Status.isRuningOrDeploying(node.getStatus()),
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
	
	private int getNodeStatus(int dbStatus, String nodeIp, int nodePort) {
		if (dbStatus == NodeEnum.Status.TO_DEPLOY.getCode() || dbStatus == NodeEnum.Status.STOP.getCode()) {
			return dbStatus;
		}

		boolean heartbeat = heartbeatService.sendNodeHeartbeat(nodeIp, nodePort, HttpMethod.POST);
		if (heartbeat) {
			return NodeEnum.Status.RUNING.getCode();
		} else {
			return NodeEnum.Status.EXCEPTION.getCode();
		}
	}
}
