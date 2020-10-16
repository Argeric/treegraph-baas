package org.treegraph.baas.core.das.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.treegraph.baas.common.enums.IntTypeEnum;
import org.treegraph.baas.common.util.WebHelper;
import org.treegraph.baas.core.constant.ConfluxConstant;
import org.treegraph.baas.core.das.MembershipChangeDas;
import org.treegraph.baas.core.das.NodeCustomizedConfigDas;
import org.treegraph.baas.core.das.NodeDas;
import org.treegraph.baas.core.das.bo.NodeKeypairBo;
import org.treegraph.baas.core.domain.ConsortiumPo;
import org.treegraph.baas.core.domain.KeypairPo;
import org.treegraph.baas.core.domain.MembershipChangePo;
import org.treegraph.baas.core.domain.MembershipPo;
import org.treegraph.baas.core.domain.NodeCustomizedConfigPo;
import org.treegraph.baas.core.domain.NodeKeyPo;
import org.treegraph.baas.core.domain.NodePo;
import org.treegraph.baas.core.domain.NodePoExample;
import org.treegraph.baas.core.domain.NodePoExample.Criteria;
import org.treegraph.baas.core.enums.MembershipEnum;
import org.treegraph.baas.core.enums.NodeEnum;
import org.treegraph.baas.core.enums.NodeEnum.MembershipStatus;
import org.treegraph.baas.core.heartbeat.bo.Heartbeat;
import org.treegraph.baas.core.heartbeat.impl.HeartbeatLinstener;
import org.treegraph.baas.core.mapper.primary.ConsortiumPoMapper;
import org.treegraph.baas.core.mapper.primary.KeypairPoMapper;
import org.treegraph.baas.core.mapper.primary.NodeKeyPoMapper;
import org.treegraph.baas.core.mapper.primary.NodePoMapper;
import org.treegraph.baas.core.util.LogSkip;

import com.google.common.collect.Lists;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class NodeDasImpl implements NodeDas {

	private NodePoMapper nodePoMapper;
	
	private KeypairPoMapper keypairPoMapper;
	
	private NodeKeyPoMapper nodeKeyPoMapper;
	
	private ConsortiumPoMapper consortiumPoMapper;
	
	private NodeCustomizedConfigDas nodeCustomizedConfigDas;

	private MembershipChangeDas membershipDas;
	
	public Optional<NodePo> findById(String id) {
		return nodePoMapper.selectByPrimaryKey(id);
	}
	
	public List<NodePo> findByIds(List<String> ids){
		NodePoExample example = new NodePoExample();
		Criteria criteria = example.createCriteria();
		if (CollectionUtils.isNotEmpty(ids)) {
			criteria.andIdIn(ids);
		}
		criteria.andDeleteFlagEqualTo(IntTypeEnum.FALSE.getValue());
		example.setOrderByClause("create_time desc");
		return nodePoMapper.selectByExample(example);
	}
	
	public List<NodePo> findAll(String consId, String ip) {
		NodePoExample example = new NodePoExample();
		Criteria criteria = example.createCriteria();
		if (StringUtils.isNotBlank(consId)) {
			criteria.andConsortiumIdEqualTo(consId);
		}
		if (StringUtils.isNotBlank(ip)) {
			criteria.andNodeIpEqualTo(ip);
		}
		criteria.andDeleteFlagEqualTo(IntTypeEnum.FALSE.getValue());
		example.setOrderByClause("create_time desc");
		return nodePoMapper.selectByExample(example);
	}

	public List<NodePo> findByConsIdAndChainId(String consId, String chainId) {
		NodePoExample example = new NodePoExample();
		Criteria criteria = example.createCriteria();
		criteria.andConsortiumIdEqualTo(consId)
				.andBlockchainIdEqualTo(chainId)
				.andDeleteFlagEqualTo(IntTypeEnum.FALSE.getValue());
		example.setOrderByClause("create_time desc");
		return nodePoMapper.selectByExample(example);
	}

	public List<NodePo> findByChainIds(List<String> chainIds) {
		if(CollectionUtils.isEmpty(chainIds)) {
			return Lists.newArrayList();
		}
		NodePoExample example = new NodePoExample();
		example.createCriteria().andBlockchainIdIn(chainIds).andDeleteFlagEqualTo(IntTypeEnum.FALSE.getValue());
		example.setOrderByClause("create_time desc");
		return nodePoMapper.selectByExample(example);
	}
	
	public NodePo findByIpPort(String ip, Integer port) {
		NodePoExample example = new NodePoExample();
		example.createCriteria().andNodeIpEqualTo(ip).andNodePortEqualTo(port);
		List<NodePo> list = nodePoMapper.selectByExample(example);
		return CollectionUtils.isNotEmpty(list) ? list.get(0) : null;
	}
	
	public long count() {
		NodePoExample example = new NodePoExample();
		example.createCriteria().andDeleteFlagEqualTo(IntTypeEnum.FALSE.getValue());
		return nodePoMapper.countByExample(example);
	}

	public boolean addNodes(List<NodePo> nodes, ConsortiumPo cons, List<NodeCustomizedConfigPo> stepFactorConfigList,
			List<NodeCustomizedConfigPo> configList) {
		boolean succFlag =  Boolean.TRUE;
		// add nodes
		succFlag = succFlag && nodePoMapper.bulkInsert(nodes) == nodes.size();
		// update consortium step
		if(cons != null) {
			succFlag = succFlag && consortiumPoMapper.updateByPrimaryKeySelective(cons) > 0;
		}
		// add or update stepFactorConfig
		if(CollectionUtils.isNotEmpty(stepFactorConfigList)) {
			for(NodeCustomizedConfigPo stepFactorConfig:stepFactorConfigList) {
				NodeCustomizedConfigPo config = nodeCustomizedConfigDas.findConfigByPropertyName(
						stepFactorConfig.getConsortiumId(), null, stepFactorConfig.getPropertyName());
				if(config != null) {
					stepFactorConfig.setId(config.getId());
					succFlag = succFlag && nodeCustomizedConfigDas.modify(stepFactorConfig);
				} else {
					succFlag = succFlag && nodeCustomizedConfigDas.add(stepFactorConfig);
				}
			}
		}
		// add config
		if(CollectionUtils.isNotEmpty(configList)) {
			succFlag = succFlag && nodeCustomizedConfigDas.batchAdd(configList);
		}
		// add heartbeat listener
		addHeartbeatListener(nodes, configList);
		
		return succFlag;
	}
	
	public boolean addNodeKeypairs(List<NodeKeypairBo> nodeKeypairBoList, NodeCustomizedConfigPo stepFactorConfig,
			List<NodeCustomizedConfigPo> customizedConfigList) {
		boolean succFlag =  Boolean.TRUE;
		// add nodes
		List<NodePo> nodes = nodeKeypairBoList.stream().map(NodeKeypairBo::getNode).collect(Collectors.toList());
		succFlag = succFlag && nodePoMapper.bulkInsert(nodes) == nodes.size();
		// add keypairs and nodekeys
		for(NodeKeypairBo nodeKeypairBo : nodeKeypairBoList){
			if(nodeKeypairBo.getKeypair().getId() == null){
				KeypairPo keypair = nodeKeypairBo.getKeypair();
				NodeKeyPo nodeKey = nodeKeypairBo.getNodeKey();
				succFlag = succFlag && keypairPoMapper.insert(keypair) > 0;
				nodeKey.setKeypairId(keypair.getId());
				succFlag = succFlag && nodeKeyPoMapper.insert(nodeKey) > 0;
			}
		}
		// add or update stepFactorCustomizedConfig
		if(stepFactorConfig != null) {
			NodeCustomizedConfigPo customizedConfig = nodeCustomizedConfigDas.findConfigByPropertyName(
					stepFactorConfig.getConsortiumId(), null, stepFactorConfig.getPropertyName());
			if(customizedConfig != null) {
				stepFactorConfig.setId(customizedConfig.getId());
				nodeCustomizedConfigDas.modify(stepFactorConfig);
			} else {
				nodeCustomizedConfigDas.add(stepFactorConfig);
			}
		}
		// add customized configration
		if(CollectionUtils.isNotEmpty(customizedConfigList)) {
			succFlag = succFlag && nodeCustomizedConfigDas.batchAdd(customizedConfigList);
		}
		// add heartbeat listener
		addHeartbeatListener(nodes, customizedConfigList);
		
		return succFlag;
	}
	
	public boolean addNodeKeypairs(List<NodeKeypairBo> nodeKeypairBoList,
			List<NodeCustomizedConfigPo> stepFactorConfigList, List<NodeCustomizedConfigPo> customizedConfigList,
			MembershipChangePo membershipChange,
			List<MembershipPo> memberships) {
		boolean succFlag =  Boolean.TRUE;
		// add nodes
		List<NodePo> nodes = nodeKeypairBoList.stream().map(NodeKeypairBo::getNode).collect(Collectors.toList());
		succFlag = succFlag && nodePoMapper.bulkInsert(nodes) == nodes.size();
		// add keypairs and nodekeys
		for(NodeKeypairBo nodeKeypairBo : nodeKeypairBoList){
			if(nodeKeypairBo.getKeypair().getId() == null){
				KeypairPo keypair = nodeKeypairBo.getKeypair();
				NodeKeyPo nodeKey = nodeKeypairBo.getNodeKey();
				succFlag = succFlag && keypairPoMapper.insert(keypair) > 0;
				nodeKey.setKeypairId(keypair.getId());
				succFlag = succFlag && nodeKeyPoMapper.insert(nodeKey) > 0;
			}
		}
		// add or update stepFactorCustomizedConfig
		if(CollectionUtils.isNotEmpty(stepFactorConfigList)) {
			for(NodeCustomizedConfigPo stepFactorConfig:stepFactorConfigList) {
				NodeCustomizedConfigPo config = nodeCustomizedConfigDas.findConfigByPropertyName(
						stepFactorConfig.getConsortiumId(), null, stepFactorConfig.getPropertyName());
				if(config != null) {
					stepFactorConfig.setId(config.getId());
					succFlag = succFlag && nodeCustomizedConfigDas.modify(stepFactorConfig);
				} else {
					succFlag = succFlag && nodeCustomizedConfigDas.add(stepFactorConfig);
				}
			}
		}
		// add customized configration
		if(CollectionUtils.isNotEmpty(customizedConfigList)) {
			succFlag = succFlag && nodeCustomizedConfigDas.batchAdd(customizedConfigList);
		}
		// add membership change and memberships
		succFlag = succFlag && membershipDas.addMembershipChange(membershipChange, memberships);
		// add heartbeat listener
		addHeartbeatListener(nodes, customizedConfigList);
		
		return succFlag;
	}
	
	public boolean addNodesMembershipChange(List<String> nodeIds, MembershipStatus membershipStatus,
			MembershipChangePo membershipChange, List<MembershipPo> memberships) {
		boolean succFlag =  Boolean.TRUE;
		// update node membership status
		NodePo record = NodePo.builder().membershipStatus(membershipStatus.getCode()).build();
		NodePoExample example = new NodePoExample();
		Criteria criteria = example.createCriteria();
		criteria.andIdIn(nodeIds);
		succFlag = succFlag &&  nodePoMapper.updateByExampleSelective(record, example) == nodeIds.size();
		// add membershipChange and memberships
		succFlag = succFlag && membershipDas.addMembershipChange(membershipChange, memberships);
		
		return succFlag;
	}

	public boolean modifyNode(NodePo node) {
		return nodePoMapper.updateByPrimaryKeySelective(node) > 0;
	}
	
	@LogSkip
	public boolean modifyNodesStatus(List<String> nodeIds, NodeEnum.Status status) {
		NodePo record = NodePo.builder().status(status.getCode()).build();
		NodePoExample example = new NodePoExample();
		Criteria criteria = example.createCriteria();
		criteria.andIdIn(nodeIds);
		return nodePoMapper.updateByExampleSelective(record, example) == nodeIds.size();
	}
	
	public boolean modifyNodesMembershipStatus(List<String> nodeIds, MembershipStatus membershipStatus,
			Long membershipChangeId, MembershipEnum.Status membershipChangeStatus) {
		boolean succFlag = Boolean.TRUE;
		// update node membership status
		NodePo record = NodePo.builder().membershipStatus(membershipStatus.getCode()).build();
		if (membershipStatus == MembershipStatus.ADD_SUCCESS 
				|| membershipStatus == MembershipStatus.ADD_FAILURE
				|| membershipStatus == MembershipStatus.REMOVE_SUCCESS
				|| membershipStatus == MembershipStatus.REMOVE_FAILURE) {
			record.setMembershipFinalStatus(record.getMembershipStatus());/*set membership final state*/
		}
		NodePoExample example = new NodePoExample();
		Criteria criteria = example.createCriteria();
		criteria.andIdIn(nodeIds);
		succFlag = succFlag &&  nodePoMapper.updateByExampleSelective(record, example) == nodeIds.size();
		// update membership change status
		List<Long> membershipChangeIds = Lists.newArrayList(membershipChangeId);
		succFlag = succFlag &&  membershipDas.modifyMembershipChangeStatus(membershipChangeIds, membershipChangeStatus);
		return succFlag;
	}
	
	public boolean deleteNodes(NodePo node, ConsortiumPo cons) {
		boolean succFlag =  Boolean.TRUE;
		NodePo updateNode = NodePo.builder()
				.id(node.getId())
				.deleteFlag(IntTypeEnum.TRUE.getValue())
				.updateTime(LocalDateTime.now())
				.updateBy(WebHelper.getUserId())
				.build();
		succFlag = succFlag && nodePoMapper.updateByPrimaryKeySelective(updateNode) > 0;
		//succFlag = succFlag && nodePoMapper.deleteByPrimaryKey(node.getId()) > 0;
		if(cons != null) {
			succFlag = succFlag && consortiumPoMapper.updateByPrimaryKeySelective(cons) > 0;
		}
		return succFlag;
	}
	
	public Integer deleteByIpPort(String ip, Integer port) {
		NodePoExample example = new NodePoExample();
		example.createCriteria().andNodeIpEqualTo(ip).andNodePortEqualTo(port);
		return nodePoMapper.deleteByExample(example);
	}

	private void addHeartbeatListener(List<NodePo> nodes, List<NodeCustomizedConfigPo> customizedConfigList) {
		HeartbeatLinstener heartbeatListener = HeartbeatLinstener.getInstance();
		Map<String, Integer> rpcHttpPortMap = customizedConfigList.stream()
				.filter(item -> item.getPropertyName().equalsIgnoreCase(ConfluxConstant.PROPERTY_JSONRPC_PORT))
				.collect(Collectors.toMap(NodeCustomizedConfigPo::getNodeId, item -> Integer.parseInt(item.getPropertyValue())));
		nodes.stream().forEach(node -> {
			if(!heartbeatListener.isNodeValid(node.getConsortiumId(), node.getId())){
				Heartbeat nodeInfo = Heartbeat.builder()
						.id(node.getId())
						.ip(node.getNodeIp())
						.port(rpcHttpPortMap.get(node.getId())).build();
				heartbeatListener.registerNode(node.getConsortiumId(), node.getId(), nodeInfo);
			}
		});
	}
}
