package org.treegraph.baas.core.mapper;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Param;
import org.treegraph.baas.core.domain.UserOperLogPo;
import org.treegraph.baas.core.domain.UserOperLogPoExample;

public interface UserOperLogPoMapper {
    long countByExample(UserOperLogPoExample example);

    int deleteByExample(UserOperLogPoExample example);

    int deleteByPrimaryKey(Long id);

    int insert(UserOperLogPo record);

    int insertSelective(UserOperLogPo record);

    List<UserOperLogPo> selectByExample(UserOperLogPoExample example);

    Optional<UserOperLogPo> selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") UserOperLogPo record, @Param("example") UserOperLogPoExample example);

    int updateByExample(@Param("record") UserOperLogPo record, @Param("example") UserOperLogPoExample example);

    int updateByPrimaryKeySelective(UserOperLogPo record);

    int updateByPrimaryKey(UserOperLogPo record);
}