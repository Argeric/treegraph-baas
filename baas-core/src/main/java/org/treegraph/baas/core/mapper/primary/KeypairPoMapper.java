package org.treegraph.baas.core.mapper.primary;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Param;
import org.treegraph.baas.core.domain.KeypairPo;
import org.treegraph.baas.core.domain.KeypairPoExample;

public interface KeypairPoMapper {
    long countByExample(KeypairPoExample example);

    int deleteByExample(KeypairPoExample example);

    int deleteByPrimaryKey(Long id);

    int insert(KeypairPo record);

    int insertSelective(KeypairPo record);

    List<KeypairPo> selectByExample(KeypairPoExample example);

    Optional<KeypairPo> selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") KeypairPo record, @Param("example") KeypairPoExample example);

    int updateByExample(@Param("record") KeypairPo record, @Param("example") KeypairPoExample example);

    int updateByPrimaryKeySelective(KeypairPo record);

    int updateByPrimaryKey(KeypairPo record);
    
    int bulkInsert(List<KeypairPo> list);
}