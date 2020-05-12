package org.treegraph.baas.core.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.treegraph.baas.core.domain.RolePermissionPo;
import org.treegraph.baas.core.domain.RolePermissionPoExample;

public interface RolePermissionPoMapper {
    long countByExample(RolePermissionPoExample example);

    int deleteByExample(RolePermissionPoExample example);

    int deleteByPrimaryKey(Long id);

    int insert(RolePermissionPo record);

    int insertSelective(RolePermissionPo record);

    List<RolePermissionPo> selectByExample(RolePermissionPoExample example);

    RolePermissionPo selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") RolePermissionPo record, @Param("example") RolePermissionPoExample example);

    int updateByExample(@Param("record") RolePermissionPo record, @Param("example") RolePermissionPoExample example);

    int updateByPrimaryKeySelective(RolePermissionPo record);

    int updateByPrimaryKey(RolePermissionPo record);
}