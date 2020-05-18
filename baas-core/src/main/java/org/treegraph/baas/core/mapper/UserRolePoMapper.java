package org.treegraph.baas.core.mapper;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Param;
import org.treegraph.baas.core.domain.UserRolePo;
import org.treegraph.baas.core.domain.UserRolePoExample;

public interface UserRolePoMapper {
    long countByExample(UserRolePoExample example);

    int deleteByExample(UserRolePoExample example);

    int deleteByPrimaryKey(Long id);

    int insert(UserRolePo record);

    int insertSelective(UserRolePo record);

    List<UserRolePo> selectByExample(UserRolePoExample example);

    Optional<UserRolePo> selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") UserRolePo record, @Param("example") UserRolePoExample example);

    int updateByExample(@Param("record") UserRolePo record, @Param("example") UserRolePoExample example);

    int updateByPrimaryKeySelective(UserRolePo record);

    int updateByPrimaryKey(UserRolePo record);
    
    int bulkInsert(List<UserRolePo> list);
}