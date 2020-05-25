package org.treegraph.baas.core.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.treegraph.baas.core.domain.UserRoleInvitePo;
import org.treegraph.baas.core.domain.UserRoleInvitePoExample;

public interface UserRoleInvitePoMapper {
    long countByExample(UserRoleInvitePoExample example);

    int deleteByExample(UserRoleInvitePoExample example);

    int deleteByPrimaryKey(Long id);

    int insert(UserRoleInvitePo record);

    int insertSelective(UserRoleInvitePo record);

    List<UserRoleInvitePo> selectByExample(UserRoleInvitePoExample example);

    UserRoleInvitePo selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") UserRoleInvitePo record, @Param("example") UserRoleInvitePoExample example);

    int updateByExample(@Param("record") UserRoleInvitePo record, @Param("example") UserRoleInvitePoExample example);

    int updateByPrimaryKeySelective(UserRoleInvitePo record);

    int updateByPrimaryKey(UserRoleInvitePo record);
    
    int bulkInsert(List<UserRoleInvitePo> list);
}