package org.treegraph.baas.core.mapper;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Param;
import org.treegraph.baas.core.domain.PermissionPo;
import org.treegraph.baas.core.domain.PermissionPoExample;

public interface PermissionPoMapper {
    long countByExample(PermissionPoExample example);

    int deleteByExample(PermissionPoExample example);

    int deleteByPrimaryKey(Long id);

    int insert(PermissionPo record);

    int insertSelective(PermissionPo record);

    List<PermissionPo> selectByExample(PermissionPoExample example);

    Optional<PermissionPo> selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") PermissionPo record, @Param("example") PermissionPoExample example);

    int updateByExample(@Param("record") PermissionPo record, @Param("example") PermissionPoExample example);

    int updateByPrimaryKeySelective(PermissionPo record);

    int updateByPrimaryKey(PermissionPo record);
}