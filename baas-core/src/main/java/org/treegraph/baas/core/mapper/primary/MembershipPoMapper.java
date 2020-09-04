package org.treegraph.baas.core.mapper.primary;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.treegraph.baas.core.domain.MembershipPo;
import org.treegraph.baas.core.domain.MembershipPoExample;

public interface MembershipPoMapper {
    long countByExample(MembershipPoExample example);

    int deleteByExample(MembershipPoExample example);

    int deleteByPrimaryKey(String id);

    int insert(MembershipPo record);

    int insertSelective(MembershipPo record);

    List<MembershipPo> selectByExample(MembershipPoExample example);

    MembershipPo selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") MembershipPo record, @Param("example") MembershipPoExample example);

    int updateByExample(@Param("record") MembershipPo record, @Param("example") MembershipPoExample example);

    int updateByPrimaryKeySelective(MembershipPo record);

    int updateByPrimaryKey(MembershipPo record);
    
    int bulkInsert(List<MembershipPo> list);
}