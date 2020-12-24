package org.treegraph.baas.core.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.treegraph.baas.common.enums.IntTypeEnum;
import org.treegraph.baas.common.util.WebHelper;
import org.treegraph.baas.core.constant.ConsortiumConstant;
import org.treegraph.baas.core.das.KeypairDas;
import org.treegraph.baas.core.das.NodeKeyDas;
import org.treegraph.baas.core.das.bo.NodeKeypairBo;
import org.treegraph.baas.core.domain.KeypairPo;
import org.treegraph.baas.core.domain.NodeKeyPo;
import org.treegraph.baas.core.domain.NodePo;
import org.treegraph.baas.core.enums.CryptoEnum;
import org.treegraph.baas.core.enums.CryptoEnum.KeyType;
import org.treegraph.baas.core.service.bo.GRpcKeypairCertBo;
import org.treegraph.baas.core.service.bo.KeypairCertBo;
import org.treegraph.baas.core.util.LogSkip;
import org.treegraph.baas.core.util.crypto.x509.KeypairCertUtil;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.treegraph.baas.core.util.crypto.x509.KeyUtil.privateKeyFromBase64;
import static org.treegraph.baas.core.util.crypto.x509.KeyUtil.privateKeyToPem;

@Validated
@Service
@AllArgsConstructor
@Slf4j
public class KeypairBuildService {

  private final KeypairDas keypairDas;

  private final NodeKeyDas nodeKeyDas;

  private final KeypairCertUtil keypairCertUtil;

  public void geneRootKeypair() throws Exception{
    KeypairPo root = keypairDas.getCA(ConsortiumConstant.DEFAULT_CONSORTIUM_ID, KeyType.ROOT_CA);
    if(root != null) return;

    KeypairCertBo keypairCert = keypairCertUtil.buildRoot();
    LocalDateTime now = LocalDateTime.now();
    String operator = WebHelper.getUserId();
    KeypairPo keypairPo = KeypairPo.builder()
        .consortiumId(ConsortiumConstant.DEFAULT_CONSORTIUM_ID)
        .keyType(KeyType.ROOT_CA.getCode())
        .algorithm(KeyType.ROOT_CA.getAlgo().getCode())
        .publicKey(keypairCert.getPublicKey())
        .privateKey(keypairCert.getPrivateKey())
        .csrStr(null)
        .certStr(keypairCert.getCertStr())
        .deleteFlag(IntTypeEnum.FALSE.getValue())
        .createTime(now)
        .createBy(operator)
        .updateTime(now)
        .updateBy(operator)
        .build();

    boolean successFlag = keypairDas.save(keypairPo);
    Assert.isTrue(successFlag, "root.ca.build.err");
  }


  public KeypairPo getConsortiumKeyPair(String consortiumId) throws Exception {
    KeypairPo keypairPo = keypairDas.getCA(consortiumId, KeyType.CONSORTIUM);
    if (keypairPo != null) {
      return keypairPo;
    }
    return geneNewConsortiumKeyPair(consortiumId, null);
  }

  @LogSkip
  public KeypairPo geneNewConsortiumKeyPair(String consId, Date lastCrtExpire) throws Exception {
    KeypairCertBo keypairCert = keypairCertUtil.buildConsCA(consId, lastCrtExpire);
    LocalDateTime now = LocalDateTime.now();
    String operator = WebHelper.getUserId();
    KeypairPo keypairPo = KeypairPo.builder()
        .consortiumId(consId)
        .keyType(CryptoEnum.KeyType.CONSORTIUM.getCode())
        .algorithm(keypairCert.getKeyType().getAlgo().getCode())
        .publicKey(keypairCert.getPublicKey())
        .privateKey(keypairCert.getPrivateKey())
        .csrStr(keypairCert.getCsrStr())
        .certStr(keypairCert.getCertStr())
        .deleteFlag(IntTypeEnum.FALSE.getValue())
        .createTime(now)
        .createBy(operator)
        .updateTime(now)
        .updateBy(operator)
        .build();

    boolean successFlag = keypairDas.save(keypairPo);
    Assert.isTrue(successFlag, "cons.ca.build.err");
    return keypairPo;
  }

  @LogSkip
  public NodeKeypairBo getNodeKeypairBo(String consortiumId, NodePo node) {
    List<NodeKeyPo> nodeKeys = nodeKeyDas.findAll(consortiumId, node.getId());
    if (CollectionUtils.isNotEmpty(nodeKeys)) {
      List<Long> keypairIds = nodeKeys.stream().map(NodeKeyPo::getKeypairId)
          .collect(Collectors.toList());
      List<KeypairPo> nodeKeypairList = keypairDas.findByIds(keypairIds);
      Optional<KeypairPo> keypairOp = nodeKeypairList.stream()
          .filter(item -> item.getAlgorithm().equalsIgnoreCase(KeyType.NODE.getAlgo().getCode()))
          .findFirst();
      if (keypairOp.isPresent()) {
        return NodeKeypairBo.builder().keypair(keypairOp.get()).node(node).build();
      }
    }

    KeypairCertBo keypairCert = keypairCertUtil.buildNodeKeypair();
    LocalDateTime now = LocalDateTime.now();
    String operator = WebHelper.getUserId();
    KeypairPo keypair = KeypairPo.builder()
        .publicKey(keypairCert.getPublicKey())
        .privateKey(keypairCert.getPrivateKey())
        .algorithm(keypairCert.getKeyType().getAlgo().getCode())
        .consortiumId(consortiumId)
        .keyType(CryptoEnum.KeyType.NODE.getCode())
        .deleteFlag(IntTypeEnum.FALSE.getValue())
        .createTime(now)
        .createBy(operator)
        .updateTime(now)
        .updateBy(operator)
        .build();

    NodeKeyPo nodeKey = NodeKeyPo.builder()
        .nodeId(node.getId())
        .keypairId(null)
        .consortiumId(consortiumId)
        .deleteFlag(IntTypeEnum.FALSE.getValue())
        .createTime(now)
        .createBy(operator)
        .updateTime(now)
        .updateBy(operator)
        .build();
    return NodeKeypairBo.builder().keypair(keypair).nodeKey(nodeKey).node(node).build();
  }

  @LogSkip
  public GRpcKeypairCertBo geneNewRpcKeypair(String consortiumId, Date lastCrtExpire,
      List<String> sanList) throws Exception {
    KeypairCertBo keypairCert = keypairCertUtil.buildRpcCert(consortiumId, lastCrtExpire, sanList);
    String rpcCert = keypairCert.getCertStr();
    String rpcPrivate = privateKeyToPem(
        privateKeyFromBase64(KeyType.RPC.getAlgo().getCode(), keypairCert.getPrivateKey()));
    return GRpcKeypairCertBo.builder().consId(consortiumId).cert(rpcCert).privateKey(rpcPrivate)
        .build();
  }
}
