package org.treegraph.baas.core.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.treegraph.baas.core.domain.RolePo;
import org.treegraph.baas.core.domain.RolePoExample;

public interface RolePoMapper {
    long countByExample(RolePoExample example);

    int deleteByExample(RolePoExample example);

    int deleteByPrimaryKey(Long id);

    int insert(RolePo record);

    int insertSelective(RolePo record);

    List<RolePo> selectByExample(RolePoExample example);

    RolePo selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") RolePo record, @Param("example") RolePoExample example);

    int updateByExample(@Param("record") RolePo record, @Param("example") RolePoExample example);

    int updateByPrimaryKeySelective(RolePo record);

    int updateByPrimaryKey(RolePo record);
}