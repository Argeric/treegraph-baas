package org.treegraph.baas.core.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.treegraph.baas.core.domain.ConsortiumPo;
import org.treegraph.baas.core.domain.ConsortiumPoExample;

public interface ConsortiumPoMapper {
    long countByExample(ConsortiumPoExample example);

    int deleteByExample(ConsortiumPoExample example);

    int deleteByPrimaryKey(String id);

    int insert(ConsortiumPo record);

    int insertSelective(ConsortiumPo record);

    List<ConsortiumPo> selectByExampleWithBLOBs(ConsortiumPoExample example);

    List<ConsortiumPo> selectByExample(ConsortiumPoExample example);

    ConsortiumPo selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") ConsortiumPo record, @Param("example") ConsortiumPoExample example);

    int updateByExampleWithBLOBs(@Param("record") ConsortiumPo record, @Param("example") ConsortiumPoExample example);

    int updateByExample(@Param("record") ConsortiumPo record, @Param("example") ConsortiumPoExample example);

    int updateByPrimaryKeySelective(ConsortiumPo record);

    int updateByPrimaryKeyWithBLOBs(ConsortiumPo record);

    int updateByPrimaryKey(ConsortiumPo record);
}