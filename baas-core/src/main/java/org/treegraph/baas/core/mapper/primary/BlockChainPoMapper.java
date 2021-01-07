package org.treegraph.baas.core.mapper.primary;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Param;
import org.treegraph.baas.core.domain.BlockChainPo;
import org.treegraph.baas.core.domain.BlockChainPoExample;

public interface BlockChainPoMapper {

  long countByExample(BlockChainPoExample example);

  int deleteByExample(BlockChainPoExample example);

  int deleteByPrimaryKey(String id);

  int insert(BlockChainPo record);

  int insertSelective(BlockChainPo record);

  List<BlockChainPo> selectByExample(BlockChainPoExample example);

  Optional<BlockChainPo> selectByPrimaryKey(String id);

  int updateByExampleSelective(@Param("record") BlockChainPo record,
      @Param("example") BlockChainPoExample example);

  int updateByExample(@Param("record") BlockChainPo record,
      @Param("example") BlockChainPoExample example);

  int updateByPrimaryKeySelective(BlockChainPo record);

  int updateByPrimaryKey(BlockChainPo record);
}