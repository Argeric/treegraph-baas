package org.treegraph.baas.core.mapper.primary;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Param;
import org.treegraph.baas.core.das.bo.NodeKeypairInfoBo;
import org.treegraph.baas.core.domain.KeypairPo;
import org.treegraph.baas.core.domain.KeypairPoExample;
import org.treegraph.baas.core.persistence.GlobalLevel;
import org.treegraph.baas.core.util.LogSkip;

@GlobalLevel
public interface KeypairPoMapper {

  long countByExample(KeypairPoExample example);

  int deleteByExample(KeypairPoExample example);

  int deleteByPrimaryKey(Long id);

  int insert(KeypairPo record);

  int insertSelective(KeypairPo record);

  @LogSkip
  List<KeypairPo> selectByExample(KeypairPoExample example);

  Optional<KeypairPo> selectByPrimaryKey(Long id);

  int updateByExampleSelective(@Param("record") KeypairPo record,
      @Param("example") KeypairPoExample example);

  int updateByExample(@Param("record") KeypairPo record,
      @Param("example") KeypairPoExample example);

  int updateByPrimaryKeySelective(KeypairPo record);

  int updateByPrimaryKey(KeypairPo record);

  int bulkInsert(List<KeypairPo> list);

  List<NodeKeypairInfoBo> selectAllByNodeIds(@Param("consortiumId") String consortiumId,
      @Param("keyType") int keyType,
      @Param("algo") String algo, @Param("nodeIds") List<String> nodeIds);
}