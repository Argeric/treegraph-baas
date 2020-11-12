package org.treegraph.baas.core.das.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.treegraph.baas.common.enums.IntTypeEnum;
import org.treegraph.baas.core.das.CertificateDeferDas;
import org.treegraph.baas.core.domain.CertificateDeferPo;
import org.treegraph.baas.core.domain.CertificateDeferPoExample;
import org.treegraph.baas.core.domain.CertificateDeferPoExample.Criteria;
import org.treegraph.baas.core.domain.KeypairPo;
import org.treegraph.baas.core.domain.KeypairPoExample;
import org.treegraph.baas.core.mapper.primary.CertificateDeferPoMapper;
import org.treegraph.baas.core.mapper.primary.KeypairPoMapper;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CertificateDeferDasImpl implements CertificateDeferDas {

  private CertificateDeferPoMapper certificateDeferPoMapper;

  private KeypairPoMapper keypairPoMapper;

  public List<CertificateDeferPo> findByConsortiumId(String consId, List<Integer> statusList) {
    CertificateDeferPoExample example = new CertificateDeferPoExample();
    Criteria criteria = example.createCriteria();

    criteria.andConsortiumIdEqualTo(consId);
    if (CollectionUtils.isNotEmpty(statusList)) {
      criteria.andStatusIn(statusList);
    }

    return certificateDeferPoMapper.selectByExample(example);
  }

  public void createCertificateDefer(CertificateDeferPo defer) {
    certificateDeferPoMapper.insert(defer);
  }

  public void modifyCertificateDefer(CertificateDeferPo defer) {
    certificateDeferPoMapper.updateByPrimaryKeySelective(defer);
  }

  public void modifyCertificateDefer(CertificateDeferPo defer, List<Long> oldKeypairIds,
      KeypairPo newKeypair) {
    boolean isSuccess = Boolean.TRUE;
    isSuccess = isSuccess && certificateDeferPoMapper.updateByPrimaryKeySelective(defer) > 0;

    if (CollectionUtils.isNotEmpty(oldKeypairIds)) {
      KeypairPo record = KeypairPo.builder().deleteFlag(IntTypeEnum.TRUE.getValue()).build();
      KeypairPoExample example = new KeypairPoExample();
      example.createCriteria().andIdIn(oldKeypairIds);
      isSuccess =
          isSuccess && keypairPoMapper.updateByExampleSelective(record, example) == oldKeypairIds
              .size();
    }

    isSuccess = isSuccess && keypairPoMapper.insert(newKeypair) > 0;
  }
}
