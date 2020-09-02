package org.treegraph.baas.core.mapper.primary;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.treegraph.baas.core.domain.MembershipChangePo;
import org.treegraph.baas.core.domain.MembershipChangePoExample;

public interface MembershipChangePoMapper {
    long countByExample(MembershipChangePoExample example);

    int deleteByExample(MembershipChangePoExample example);

    int deleteByPrimaryKey(Long id);

    int insert(MembershipChangePo record);

    int insertSelective(MembershipChangePo record);

    List<MembershipChangePo> selectByExample(MembershipChangePoExample example);

    MembershipChangePo selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") MembershipChangePo record, @Param("example") MembershipChangePoExample example);

    int updateByExample(@Param("record") MembershipChangePo record, @Param("example") MembershipChangePoExample example);

    int updateByPrimaryKeySelective(MembershipChangePo record);

    int updateByPrimaryKey(MembershipChangePo record);
}