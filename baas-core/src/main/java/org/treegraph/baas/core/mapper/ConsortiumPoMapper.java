package org.treegraph.baas.core.mapper;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Param;
import org.treegraph.baas.core.domain.ConsortiumPo;
import org.treegraph.baas.core.domain.ConsortiumPoExample;

public interface ConsortiumPoMapper {
    long countByExample(ConsortiumPoExample example);

    int deleteByExample(ConsortiumPoExample example);

    int deleteByPrimaryKey(String consortiumId);

    int insert(ConsortiumPo record);

    int insertSelective(ConsortiumPo record);

    List<ConsortiumPo> selectByExampleWithBLOBs(ConsortiumPoExample example);

    List<ConsortiumPo> selectByExample(ConsortiumPoExample example);

    Optional<ConsortiumPo> selectByPrimaryKey(String consortiumId);

    int updateByExampleSelective(@Param("record") ConsortiumPo record, @Param("example") ConsortiumPoExample example);

    int updateByExampleWithBLOBs(@Param("record") ConsortiumPo record, @Param("example") ConsortiumPoExample example);

    int updateByExample(@Param("record") ConsortiumPo record, @Param("example") ConsortiumPoExample example);

    int updateByPrimaryKeySelective(ConsortiumPo record);

    int updateByPrimaryKeyWithBLOBs(ConsortiumPo record);

    int updateByPrimaryKey(ConsortiumPo record);
}