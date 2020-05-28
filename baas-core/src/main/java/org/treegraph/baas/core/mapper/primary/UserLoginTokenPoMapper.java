package org.treegraph.baas.core.mapper.primary;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.treegraph.baas.core.domain.UserLoginTokenPo;
import org.treegraph.baas.core.domain.UserLoginTokenPoExample;

public interface UserLoginTokenPoMapper {
    long countByExample(UserLoginTokenPoExample example);

    int deleteByExample(UserLoginTokenPoExample example);

    int deleteByPrimaryKey(Long id);

    int insert(UserLoginTokenPo record);

    int insertSelective(UserLoginTokenPo record);

    List<UserLoginTokenPo> selectByExample(UserLoginTokenPoExample example);

    UserLoginTokenPo selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") UserLoginTokenPo record, @Param("example") UserLoginTokenPoExample example);

    int updateByExample(@Param("record") UserLoginTokenPo record, @Param("example") UserLoginTokenPoExample example);

    int updateByPrimaryKeySelective(UserLoginTokenPo record);

    int updateByPrimaryKey(UserLoginTokenPo record);
}