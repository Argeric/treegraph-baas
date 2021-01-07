package org.treegraph.baas.core.das.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.treegraph.baas.common.enums.IntTypeEnum;
import org.treegraph.baas.core.das.NodeCustomizedConfigDas;
import org.treegraph.baas.core.domain.NodeCustomizedConfigPo;
import org.treegraph.baas.core.domain.NodeCustomizedConfigPoExample;
import org.treegraph.baas.core.domain.NodeCustomizedConfigPoExample.Criteria;
import org.treegraph.baas.core.mapper.primary.NodeCustomizedConfigPoMapper;

import lombok.AllArgsConstructor;
import org.treegraph.baas.core.util.LogSkip;

@Service
@AllArgsConstructor
public class NodeCustomizedConfigDasImpl implements NodeCustomizedConfigDas {

  private NodeCustomizedConfigPoMapper nodeCustomizedConfigPoMapper;

  public List<NodeCustomizedConfigPo> findAll(String consId, List<String> nodeIds) {
    NodeCustomizedConfigPoExample example = new NodeCustomizedConfigPoExample();
    Criteria criteria = example.createCriteria();
    if (StringUtils.isNotBlank(consId)) {
      criteria.andConsortiumIdEqualTo(consId);
    }
    if (CollectionUtils.isNotEmpty(nodeIds)) {
      criteria.andNodeIdIn(nodeIds);
    }
    criteria.andDeleteFlagEqualTo(IntTypeEnum.FALSE.getValue());
    example.setOrderByClause("create_time desc");
    return nodeCustomizedConfigPoMapper.selectByExample(example);
  }

  public NodeCustomizedConfigPo findConfigByPropertyName(String consId, String nodeId,
      String propertyName) {
    NodeCustomizedConfigPoExample example = new NodeCustomizedConfigPoExample();
    Criteria criteria = example.createCriteria();
    if (StringUtils.isNotBlank(consId)) {
      criteria.andConsortiumIdEqualTo(consId);
    }
    if (StringUtils.isNoneBlank(nodeId)) {
      criteria.andNodeIdEqualTo(nodeId);
    } else {
      criteria.andNodeIdEqualTo(IntTypeEnum.FALSE.getValue() + "");
    }
    criteria.andPropertyNameEqualTo(propertyName);
    criteria.andDeleteFlagEqualTo(IntTypeEnum.FALSE.getValue());
    example.setOrderByClause("create_time desc");
    List<NodeCustomizedConfigPo> nodeCustomizedConfigList = nodeCustomizedConfigPoMapper
        .selectByExample(example);
    return CollectionUtils.isNotEmpty(nodeCustomizedConfigList)
        ? nodeCustomizedConfigList.get(0) : null;
  }

  public List<NodeCustomizedConfigPo> findConfigByPropertyName(List<String> nodeIds,
      String propertyName) {
    NodeCustomizedConfigPoExample example = new NodeCustomizedConfigPoExample();
    Criteria criteria = example.createCriteria();
    if (CollectionUtils.isNotEmpty(nodeIds)) {
      criteria.andNodeIdIn(nodeIds);
    } else {
      criteria.andNodeIdEqualTo(IntTypeEnum.FALSE.getValue() + "");
    }
    criteria.andPropertyNameEqualTo(propertyName);
    criteria.andDeleteFlagEqualTo(IntTypeEnum.FALSE.getValue());
    example.setOrderByClause("create_time desc");
    return nodeCustomizedConfigPoMapper.selectByExample(example);
  }

  @LogSkip
  public String findPropertyValueByName(String consId, String nodeId, String propertyName) {
    NodeCustomizedConfigPoExample example = new NodeCustomizedConfigPoExample();
    Criteria criteria = example.createCriteria();
    if (StringUtils.isNotBlank(consId)) {
      criteria.andConsortiumIdEqualTo(consId);
    }
    if (StringUtils.isNoneBlank(nodeId)) {
      criteria.andNodeIdEqualTo(nodeId);
    } else {
      criteria.andNodeIdEqualTo(IntTypeEnum.FALSE.getValue() + "");
    }
    criteria.andPropertyNameEqualTo(propertyName);
    criteria.andDeleteFlagEqualTo(IntTypeEnum.FALSE.getValue());
    example.setOrderByClause("create_time desc");
    List<NodeCustomizedConfigPo> nodeCustomizedConfigList = nodeCustomizedConfigPoMapper
        .selectByExample(example);
    return CollectionUtils.isNotEmpty(nodeCustomizedConfigList)
        ? nodeCustomizedConfigList.get(0).getPropertyValue()
        : null;
  }

  public boolean add(NodeCustomizedConfigPo nodeCustomizedConfig) {
    return nodeCustomizedConfigPoMapper.insertSelective(nodeCustomizedConfig) > 0;
  }

  public boolean batchAdd(List<NodeCustomizedConfigPo> customizedConfigList) {
    return nodeCustomizedConfigPoMapper.bulkInsert(customizedConfigList) == customizedConfigList
        .size();
  }

  public boolean modify(NodeCustomizedConfigPo nodeCustomizedConfig) {
    return nodeCustomizedConfigPoMapper.updateByPrimaryKeySelective(nodeCustomizedConfig) > 0;
  }
}
