package org.treegraph.baas.core.mapper;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Param;
import org.treegraph.baas.core.domain.UserLoginLogPo;
import org.treegraph.baas.core.domain.UserLoginLogPoExample;

public interface UserLoginLogPoMapper {
    long countByExample(UserLoginLogPoExample example);

    int deleteByExample(UserLoginLogPoExample example);

    int deleteByPrimaryKey(Long id);

    int insert(UserLoginLogPo record);

    int insertSelective(UserLoginLogPo record);

    List<UserLoginLogPo> selectByExample(UserLoginLogPoExample example);

    Optional<UserLoginLogPo> selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") UserLoginLogPo record, @Param("example") UserLoginLogPoExample example);

    int updateByExample(@Param("record") UserLoginLogPo record, @Param("example") UserLoginLogPoExample example);

    int updateByPrimaryKeySelective(UserLoginLogPo record);

    int updateByPrimaryKey(UserLoginLogPo record);
}