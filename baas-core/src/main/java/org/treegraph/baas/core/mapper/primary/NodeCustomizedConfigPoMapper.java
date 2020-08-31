package org.treegraph.baas.core.mapper.primary;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.treegraph.baas.core.domain.NodeCustomizedConfigPo;
import org.treegraph.baas.core.domain.NodeCustomizedConfigPoExample;
import org.treegraph.baas.core.persistence.GlobalLevel;

public interface NodeCustomizedConfigPoMapper {
    long countByExample(NodeCustomizedConfigPoExample example);

    int deleteByExample(NodeCustomizedConfigPoExample example);

    int deleteByPrimaryKey(Long id);

    int insert(NodeCustomizedConfigPo record);

    int insertSelective(NodeCustomizedConfigPo record);

    @GlobalLevel
    List<NodeCustomizedConfigPo> selectByExample(NodeCustomizedConfigPoExample example);

    NodeCustomizedConfigPo selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") NodeCustomizedConfigPo record, @Param("example") NodeCustomizedConfigPoExample example);

    int updateByExample(@Param("record") NodeCustomizedConfigPo record, @Param("example") NodeCustomizedConfigPoExample example);

    int updateByPrimaryKeySelective(NodeCustomizedConfigPo record);

    int updateByPrimaryKey(NodeCustomizedConfigPo record);
    
    int bulkInsert(List<NodeCustomizedConfigPo> list);
}