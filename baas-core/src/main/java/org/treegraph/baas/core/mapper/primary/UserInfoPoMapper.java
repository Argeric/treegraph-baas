package org.treegraph.baas.core.mapper.primary;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Param;
import org.treegraph.baas.core.domain.UserInfoPo;
import org.treegraph.baas.core.domain.UserInfoPoExample;

public interface UserInfoPoMapper {
    long countByExample(UserInfoPoExample example);

    int deleteByExample(UserInfoPoExample example);

    int deleteByPrimaryKey(String id);

    int insert(UserInfoPo record);

    int insertSelective(UserInfoPo record);

    List<UserInfoPo> selectByExample(UserInfoPoExample example);

    Optional<UserInfoPo> selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") UserInfoPo record, @Param("example") UserInfoPoExample example);

    int updateByExample(@Param("record") UserInfoPo record, @Param("example") UserInfoPoExample example);

    int updateByPrimaryKeySelective(UserInfoPo record);

    int updateByPrimaryKey(UserInfoPo record);
    
    int bulkInsert(List<UserInfoPo> list);
}