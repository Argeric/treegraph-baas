package org.treegraph.baas.core.mapper;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Param;
import org.treegraph.baas.core.domain.NodePo;
import org.treegraph.baas.core.domain.NodePoExample;

public interface NodePoMapper {
    long countByExample(NodePoExample example);

    int deleteByExample(NodePoExample example);

    int deleteByPrimaryKey(String id);

    int insert(NodePo record);

    int insertSelective(NodePo record);

    List<NodePo> selectByExample(NodePoExample example);

    Optional<NodePo> selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") NodePo record, @Param("example") NodePoExample example);

    int updateByExample(@Param("record") NodePo record, @Param("example") NodePoExample example);

    int updateByPrimaryKeySelective(NodePo record);

    int updateByPrimaryKey(NodePo record);
    
    int bulkInsert(List<NodePo> list);
}