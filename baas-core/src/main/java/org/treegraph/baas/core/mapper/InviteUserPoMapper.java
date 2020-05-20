package org.treegraph.baas.core.mapper;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Param;
import org.treegraph.baas.core.domain.InviteUserPo;
import org.treegraph.baas.core.domain.InviteUserPoExample;

public interface InviteUserPoMapper {
    long countByExample(InviteUserPoExample example);

    int deleteByExample(InviteUserPoExample example);

    int deleteByPrimaryKey(Long id);

    int insert(InviteUserPo record);

    int insertSelective(InviteUserPo record);

    List<InviteUserPo> selectByExample(InviteUserPoExample example);

    Optional<InviteUserPo> selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") InviteUserPo record, @Param("example") InviteUserPoExample example);

    int updateByExample(@Param("record") InviteUserPo record, @Param("example") InviteUserPoExample example);

    int updateByPrimaryKeySelective(InviteUserPo record);

    int updateByPrimaryKey(InviteUserPo record);
}