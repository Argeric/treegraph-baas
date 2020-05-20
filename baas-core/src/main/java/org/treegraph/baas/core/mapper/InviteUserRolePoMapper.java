package org.treegraph.baas.core.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.treegraph.baas.core.domain.InviteUserRolePo;
import org.treegraph.baas.core.domain.InviteUserRolePoExample;

public interface InviteUserRolePoMapper {
    long countByExample(InviteUserRolePoExample example);

    int deleteByExample(InviteUserRolePoExample example);

    int deleteByPrimaryKey(Long id);

    int insert(InviteUserRolePo record);

    int insertSelective(InviteUserRolePo record);

    List<InviteUserRolePo> selectByExample(InviteUserRolePoExample example);

    InviteUserRolePo selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") InviteUserRolePo record, @Param("example") InviteUserRolePoExample example);

    int updateByExample(@Param("record") InviteUserRolePo record, @Param("example") InviteUserRolePoExample example);

    int updateByPrimaryKeySelective(InviteUserRolePo record);

    int updateByPrimaryKey(InviteUserRolePo record);
    
    int bulkInsert(List<InviteUserRolePo> list);
}