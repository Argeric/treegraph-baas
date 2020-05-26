package org.treegraph.baas.core.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.treegraph.baas.core.domain.UserTokenPo;
import org.treegraph.baas.core.domain.UserTokenPoExample;

public interface UserTokenPoMapper {
    long countByExample(UserTokenPoExample example);

    int deleteByExample(UserTokenPoExample example);

    int deleteByPrimaryKey(Long id);

    int insert(UserTokenPo record);

    int insertSelective(UserTokenPo record);

    List<UserTokenPo> selectByExample(UserTokenPoExample example);

    UserTokenPo selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") UserTokenPo record, @Param("example") UserTokenPoExample example);

    int updateByExample(@Param("record") UserTokenPo record, @Param("example") UserTokenPoExample example);

    int updateByPrimaryKeySelective(UserTokenPo record);

    int updateByPrimaryKey(UserTokenPo record);
}