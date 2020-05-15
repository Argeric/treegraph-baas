package org.treegraph.baas.core.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.treegraph.baas.api.dto.BlockchainInfo;
import org.treegraph.baas.api.dto.CertInfo;
import org.treegraph.baas.api.dto.ListRequest;
import org.treegraph.baas.api.dto.NodeInfo;
import org.treegraph.baas.api.dto.request.DownloadCertRequest;
import org.treegraph.baas.api.dto.request.ListCertRequest;
import org.treegraph.baas.api.dto.request.QueryBlockchainDetailRequest;
import org.treegraph.baas.api.dto.request.SwitchBlockchainStatusRequest;
import org.treegraph.baas.api.dto.response.ListBlockchainResponse;
import org.treegraph.baas.api.dto.response.ListCertResponse;
import org.treegraph.baas.api.dto.response.QueryBlockchainDetailResponse;
import org.treegraph.baas.common.util.WebHelper;
import org.treegraph.baas.core.das.BlockchainDas;
import org.treegraph.baas.core.das.CertDas;
import org.treegraph.baas.core.das.ConsortiumDas;
import org.treegraph.baas.core.das.NodeDas;
import org.treegraph.baas.core.das.UserInfoDas;
import org.treegraph.baas.core.domain.BlockChainPo;
import org.treegraph.baas.core.domain.CertificateRequestPo;
import org.treegraph.baas.core.domain.ConsortiumPo;
import org.treegraph.baas.core.domain.NodePo;
import org.treegraph.baas.core.domain.UserInfoPo;
import org.treegraph.baas.core.enums.BlockchainEnum;
import org.treegraph.baas.core.exception.AlertException;
import org.treegraph.baas.core.service.BlockchainService;
import org.treegraph.baas.core.util.DateTimeUtils;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import lombok.AllArgsConstructor;

@Validated
@Service
@AllArgsConstructor
public class BlockchainServicempl implements BlockchainService {

	private BlockchainDas blockchainDas;
	
	private ConsortiumDas consortiumDas;
	
	private NodeDas nodeDas;
	
	private UserInfoDas userInfoDas;
	
	private CertDas certDas;

	public ListBlockchainResponse listBlockchain(ListRequest req) {
		
		PageHelper.startPage(req.getCurrent(), req.getPageSize());
		List<BlockChainPo> chains = blockchainDas.findAll();
		PageInfo<BlockChainPo> pageInfo = new PageInfo<BlockChainPo>(chains);
		
		List<BlockChainPo> blockchains = pageInfo.getList();
		List<String> consIdList = blockchains.stream().map(BlockChainPo::getConsortiumId).collect(Collectors.toList());
		List<ConsortiumPo> consList = consortiumDas.findByIds(consIdList);
		Map<String, ConsortiumPo> consMap = consList.stream().collect(Collectors.toMap(ConsortiumPo::getId, Function.identity()));
		
		List<String> creatorIds = blockchains.stream().map(BlockChainPo::getCreateBy).collect(Collectors.toList());
		List<UserInfoPo> users = userInfoDas.findByIds(creatorIds);
		Map<String, UserInfoPo> userMap = users.stream().collect(Collectors.toMap(UserInfoPo::getId, Function.identity()));
		
		List<String> chainIdList = blockchains.stream().map(BlockChainPo::getId).collect(Collectors.toList());
		List<NodePo> nodes = nodeDas.findByChainIds(chainIdList);
		Map<String, Long> chainCountMap = nodes.stream().collect(Collectors.groupingBy(NodePo::getBlockchainId, Collectors.counting()));
		
		List<BlockchainInfo> list = blockchains.stream().map(item -> BlockchainInfo.builder()
				.id(item.getId())
				.name(item.getBlockchainName())
				.consId(item.getConsortiumId())
				.consName(consMap.get(item.getConsortiumId()).getName())
				.consAvatar(new String(consMap.get(item.getConsortiumId()).getAvatar()))
				.createUser(userMap.get(item.getCreateBy()).getName())
				.createTime(DateTimeUtils.formatTime(item.getCreateTime(), DateTimeUtils.FORMAT_yyyyMMddHHmmss))
				.status(BlockchainEnum.Status.getByCode(item.getStatus()).getDesc())
				.nodeCount(chainCountMap.get(item.getId())+"")
				.tps(null)
				.contractCount(null)
				.txCount(null)
				.build()
				).collect(Collectors.toList());

		return ListBlockchainResponse.builder()
				.current(req.getCurrent())
				.pageSize(req.getPageSize())
				.list(list)
				.total(pageInfo.getTotal())
				.build();
	}

	public QueryBlockchainDetailResponse getBlockchainDetail(QueryBlockchainDetailRequest req) {
		
		BlockChainPo chain = blockchainDas.findById(req.getId()).orElseThrow(() -> new AlertException("blockchain.not.found"));
		ConsortiumPo cons = consortiumDas.findById(chain.getConsortiumId()).orElseThrow(() -> new AlertException("consortium.not.found"));
		List<NodePo> nodes = nodeDas.findByConsIdAndChainId(cons.getId(), chain.getId());
		List<NodeInfo> nodeInfos = nodes.stream().map(item -> NodeInfo.builder()
				.ip(item.getNodeIp())
				.port(item.getNodePort()+"")
				.org(item.getOrganizationName())
				.build()).collect(Collectors.toList());
		
		return QueryBlockchainDetailResponse.builder()
				.id(chain.getId())
				.name(chain.getBlockchainName())
				.alertEmail(chain.getAlarmEmail())
				.nodes(nodeInfos)
				.consId(chain.getConsortiumId())
				.consName(cons.getName())
				.consAvatar(new String(cons.getAvatar()))
				.consContact(cons.getContactName())
				.consEmail(cons.getContactEmail())
				.consPhone(cons.getContactPhone())
				.consensus(BlockchainEnum.Consensus.getByCode(chain.getConsensusAlgorithm()).getName())
				.algorithm(BlockchainEnum.ALGORITHM.getByCode(chain.getEncryptAlgorithm()).getName())
				.tps(null)
				.txCount(null)
				.build();
	}

	public ListCertResponse listCert(ListCertRequest req) {
		
		List<CertificateRequestPo> certReqList = certDas.findByBlockchainId(req.getBlockchainId());
		List<String> applyeeIds = certReqList.stream().map(CertificateRequestPo::getApplyUserId).collect(Collectors.toList());
		List<UserInfoPo> users = userInfoDas.findByIds(applyeeIds);
		Map<String, UserInfoPo> userMap = users.stream().collect(Collectors.toMap(UserInfoPo::getId, Function.identity()));
		
		List<CertInfo> list = certReqList.stream().map(item -> CertInfo.builder()
				.id(req.getBlockchainId())
				.name(userMap.get(item.getApplyUserId()).getName())
				.account(userMap.get(item.getApplyUserId()).getAccount())
				.createTime(DateTimeUtils.formatTime(item.getCreateTime(), DateTimeUtils.FORMAT_yyyyMMddHHmmss))
				.build()).collect(Collectors.toList());
		
		return ListCertResponse.builder()
				.list(list)
				.build();
	}

	public boolean downloadCert(DownloadCertRequest downloadCertRequest) {

		return false;
	}
	
	public boolean switchBlockchainStatus(SwitchBlockchainStatusRequest req, BlockchainEnum.Status status) {
		BlockChainPo blockChainPo = BlockChainPo.builder()
				.id(req.getBlockchainId())
				.status(status.getCode())
				.updateTime(LocalDateTime.now())
				.updateBy(WebHelper.getUserId())
				.build();
		return blockchainDas.modify(blockChainPo);
	}
}
