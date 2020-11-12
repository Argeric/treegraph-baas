package org.treegraph.baas.core.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.security.cert.X509Certificate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.treegraph.baas.api.dto.BlockchainInfo;
import org.treegraph.baas.api.dto.CertInfo;
import org.treegraph.baas.api.dto.ListRequest;
import org.treegraph.baas.api.dto.NodeInfo;
import org.treegraph.baas.api.dto.request.DownloadCertRequest;
import org.treegraph.baas.api.dto.request.ListCertRequest;
import org.treegraph.baas.api.dto.request.QueryBlockchainDetailRequest;
import org.treegraph.baas.api.dto.request.QueryBlockchainRequest;
import org.treegraph.baas.api.dto.request.SwitchBlockchainStatusRequest;
import org.treegraph.baas.api.dto.response.ListBlockchainResponse;
import org.treegraph.baas.api.dto.response.ListCertResponse;
import org.treegraph.baas.api.dto.response.QueryBlockchainDetailResponse;
import org.treegraph.baas.common.util.WebHelper;
import org.treegraph.baas.core.das.BlockchainDas;
import org.treegraph.baas.core.das.CertDas;
import org.treegraph.baas.core.das.ConsortiumDas;
import org.treegraph.baas.core.das.KeypairDas;
import org.treegraph.baas.core.das.NodeDas;
import org.treegraph.baas.core.das.UserInfoDas;
import org.treegraph.baas.core.domain.BlockChainPo;
import org.treegraph.baas.core.domain.CertificateRequestPo;
import org.treegraph.baas.core.domain.ConsortiumPo;
import org.treegraph.baas.core.domain.KeypairPo;
import org.treegraph.baas.core.domain.NodePo;
import org.treegraph.baas.core.domain.UserInfoPo;
import org.treegraph.baas.core.enums.BlockchainEnum;
import org.treegraph.baas.core.enums.CryptoEnum;
import org.treegraph.baas.core.exception.AlertException;
import org.treegraph.baas.core.service.BlockchainService;
import org.treegraph.baas.core.util.DateTimeUtils;
import org.treegraph.baas.core.util.DateUtils;
import org.treegraph.baas.core.util.crypto.x509.CertUtil;

@Validated
@Service
@AllArgsConstructor
@Slf4j
public class BlockchainServiceImpl implements BlockchainService {

  private final BlockchainDas blockchainDas;

  private final ConsortiumDas consortiumDas;

  private final NodeDas nodeDas;

  private final UserInfoDas userInfoDas;

  private final CertDas certDas;

  private final KeypairDas keypairDas;

  public ListBlockchainResponse listBlockchain(ListRequest req) {
    PageHelper.startPage(req.getCurrent(), req.getPageSize());
    List<BlockChainPo> chains = blockchainDas.findAll(null, null);
    PageInfo<BlockChainPo> pageInfo = new PageInfo<>(chains);

    List<BlockChainPo> blockchains = pageInfo.getList();
    List<String> consIdList = blockchains.stream().map(BlockChainPo::getConsortiumId)
        .collect(Collectors.toList());
    List<ConsortiumPo> consList = consortiumDas.findByIds(consIdList);
    Map<String, ConsortiumPo> consMap = consList.stream()
        .collect(Collectors.toMap(ConsortiumPo::getConsortiumId, Function.identity()));

    List<String> creatorIds = blockchains.stream().map(BlockChainPo::getCreateBy)
        .collect(Collectors.toList());
    List<UserInfoPo> users = userInfoDas.findByIds(creatorIds);
    Map<String, UserInfoPo> userMap = users.stream()
        .collect(Collectors.toMap(UserInfoPo::getId, Function.identity()));

    List<String> chainIdList = blockchains.stream().map(BlockChainPo::getId)
        .collect(Collectors.toList());
    List<NodePo> nodes = nodeDas.findByChainIds(chainIdList);
    Map<String, Long> chainCountMap = nodes.stream()
        .collect(Collectors.groupingBy(NodePo::getBlockchainId, Collectors.counting()));

    List<KeypairPo> consKeypairs = keypairDas
        .findAll(consIdList, CryptoEnum.KeyType.CONSORTIUM.getCode(),
            CryptoEnum.Algo.RSA.getCode());
    Map<String, List<String>> expireDateMap = Maps.newHashMap();
    consKeypairs.forEach(item -> {
      try {
        X509Certificate cert = CertUtil.certFromPem(item.getCertStr());
        List<String> expireDates = Lists.newArrayList();
        expireDates.add(DateUtils.format(cert.getNotBefore(), DateUtils.SYSTEM_DATE_FORMAT));
        expireDates.add(DateUtils.format(cert.getNotAfter(), DateUtils.SYSTEM_DATE_FORMAT));
        expireDateMap.put(item.getConsortiumId(), expireDates);
      } catch (Exception e) {
        log.error("get expireDates err", e);
      }
    });

    List<BlockchainInfo> list = blockchains.stream().map(item -> {
      BlockchainEnum.Status status = BlockchainEnum.Status.getByCode(item.getStatus());
      List<String> expireDates = expireDateMap.get(item.getConsortiumId());
      return BlockchainInfo.builder()
          .id(item.getId())
          .name(item.getBlockchainName())
          .consId(item.getConsortiumId())
          .consName(consMap.get(item.getConsortiumId()).getName())
          .consAvatar(consMap.get(item.getConsortiumId()).getAvatar() != null
              ? new String(consMap.get(item.getConsortiumId()).getAvatar())
              : null)
          .createUser(userMap.get(item.getCreateBy()).getName())
          .createTime(
              DateTimeUtils.formatTime(item.getCreateTime(), DateTimeUtils.FORMAT_yyyyMMddHHmmss))
          .status(status != null ? status.getCode() : null)
          .nodeCount(
              chainCountMap.get(item.getId()) == null ? "0" : chainCountMap.get(item.getId()) + "")
          .tps(null)
          .contractCount(null)
          .txCount(null)
          .alertEmail(item.getAlarmEmail())
          .certNotBefore(expireDates != null ? expireDates.get(0) : "")
          .certNotAfter(expireDates != null ? expireDates.get(1) : "")
          .build();
    }).collect(Collectors.toList());

    return ListBlockchainResponse.builder()
        .current(req.getCurrent())
        .pageSize(req.getPageSize())
        .list(list)
        .total(pageInfo.getTotal())
        .build();
  }

  public List<BlockchainInfo> queryBlockchain(QueryBlockchainRequest req) {
    List<BlockChainPo> blockchains = blockchainDas.findAll(req.getId(), req.getConsId());
    return blockchains.stream().map(item -> {
      BlockchainEnum.Status status = BlockchainEnum.Status.getByCode(item.getStatus());
      return BlockchainInfo.builder()
          .id(item.getId())
          .name(item.getBlockchainName())
          .status(status != null ? status.getCode() : null)
          .alertEmail(item.getAlarmEmail())
          .build();
    }).collect(Collectors.toList());
  }

  public QueryBlockchainDetailResponse getBlockchainDetail(QueryBlockchainDetailRequest req) {
    BlockChainPo chain = blockchainDas.findById(req.getId())
        .orElseThrow(() -> new AlertException("blockchain.not.found"));
    ConsortiumPo cons = consortiumDas.findById(chain.getConsortiumId())
        .orElseThrow(() -> new AlertException("consortium.not.found"));
    List<NodePo> nodes = nodeDas.findByConsIdAndChainId(cons.getConsortiumId(), chain.getId());
    List<NodeInfo> nodeInfoList = nodes.stream().map(item -> NodeInfo.builder()
        .ip(item.getNodeIp())
        .port(item.getNodePort() + "")
        .org(item.getOrganizationName())
        .build()).collect(Collectors.toList());

    BlockchainEnum.Consensus consensus = BlockchainEnum.Consensus
        .getByCode(chain.getConsensusAlgorithm());
    BlockchainEnum.Algorithm algorithm = BlockchainEnum.Algorithm
        .getByCode(chain.getEncryptAlgorithm());
    return QueryBlockchainDetailResponse.builder()
        .id(chain.getId())
        .name(chain.getBlockchainName())
        .alertEmail(chain.getAlarmEmail())
        .nodes(nodeInfoList)
        .consId(chain.getConsortiumId())
        .consName(cons.getName())
        .consAvatar(cons.getAvatar() != null ? new String(cons.getAvatar()) : null)
        .consContact(cons.getContactName())
        .consEmail(cons.getContactEmail())
        .consPhone(cons.getContactPhone())
        .consensus(consensus != null ? consensus.getName() : null)
        .algorithm(algorithm != null ? algorithm.getName() : null)
        .tps(null)
        .txCount(null)
        .build();
  }

  public ListCertResponse listCert(ListCertRequest req) {
    List<CertificateRequestPo> certReqList = certDas.findByBlockchainId(req.getBlockchainId());
    List<String> applyUserIdList = certReqList.stream().map(CertificateRequestPo::getApplyUserId)
        .collect(Collectors.toList());
    List<UserInfoPo> users = userInfoDas.findByIds(applyUserIdList);
    Map<String, UserInfoPo> userMap = users.stream()
        .collect(Collectors.toMap(UserInfoPo::getId, Function.identity()));

    List<CertInfo> list = certReqList.stream().map(item -> CertInfo.builder()
        .id(req.getBlockchainId())
        .name(userMap.get(item.getApplyUserId()).getName())
        .account(userMap.get(item.getApplyUserId()).getAccount())
        .ipList(item.getSan() != null ? JSONObject.parseArray(item.getSan(), String.class) : null)
        .createTime(
            DateTimeUtils.formatTime(item.getCreateTime(), DateTimeUtils.FORMAT_yyyyMMddHHmmss))
        .build()).collect(Collectors.toList());

    return ListCertResponse.builder()
        .list(list)
        .build();
  }

  public void downloadCert(DownloadCertRequest downloadCertRequest) {
  }

  public boolean switchBlockchainStatus(SwitchBlockchainStatusRequest req,
      BlockchainEnum.Status status) {
    BlockChainPo blockChainPo = BlockChainPo.builder()
        .id(req.getBlockchainId())
        .status(status.getCode())
        .updateTime(LocalDateTime.now())
        .updateBy(WebHelper.getUserId())
        .build();
    return blockchainDas.modify(blockChainPo);
  }
}
