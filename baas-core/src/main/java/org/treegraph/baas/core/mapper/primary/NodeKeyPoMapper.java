package org.treegraph.baas.core.mapper.primary;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.treegraph.baas.core.domain.NodeKeyPo;
import org.treegraph.baas.core.domain.NodeKeyPoExample;
import org.treegraph.baas.core.persistence.GlobalLevel;

public interface NodeKeyPoMapper {

  long countByExample(NodeKeyPoExample example);

  int deleteByExample(NodeKeyPoExample example);

  int deleteByPrimaryKey(Long id);

  int insert(NodeKeyPo record);

  int insertSelective(NodeKeyPo record);

  @GlobalLevel
  List<NodeKeyPo> selectByExample(NodeKeyPoExample example);

  NodeKeyPo selectByPrimaryKey(Long id);

  int updateByExampleSelective(@Param("record") NodeKeyPo record,
      @Param("example") NodeKeyPoExample example);

  int updateByExample(@Param("record") NodeKeyPo record,
      @Param("example") NodeKeyPoExample example);

  int updateByPrimaryKeySelective(NodeKeyPo record);

  int updateByPrimaryKey(NodeKeyPo record);
}