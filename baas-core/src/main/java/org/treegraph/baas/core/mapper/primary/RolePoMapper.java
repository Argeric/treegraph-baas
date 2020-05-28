package org.treegraph.baas.core.mapper.primary;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Param;
import org.treegraph.baas.core.domain.RolePo;
import org.treegraph.baas.core.domain.RolePoExample;

public interface RolePoMapper {
    long countByExample(RolePoExample example);

    int deleteByExample(RolePoExample example);

    int deleteByPrimaryKey(String id);

    int insert(RolePo record);

    int insertSelective(RolePo record);

    List<RolePo> selectByExample(RolePoExample example);

    Optional<RolePo> selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") RolePo record, @Param("example") RolePoExample example);

    int updateByExample(@Param("record") RolePo record, @Param("example") RolePoExample example);

    int updateByPrimaryKeySelective(RolePo record);

    int updateByPrimaryKey(RolePo record);
}