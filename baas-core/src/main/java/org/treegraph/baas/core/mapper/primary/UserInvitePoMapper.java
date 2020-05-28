package org.treegraph.baas.core.mapper.primary;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Param;
import org.treegraph.baas.core.domain.UserInvitePo;
import org.treegraph.baas.core.domain.UserInvitePoExample;

public interface UserInvitePoMapper {
    long countByExample(UserInvitePoExample example);

    int deleteByExample(UserInvitePoExample example);

    int deleteByPrimaryKey(Long id);

    int insert(UserInvitePo record);

    int insertSelective(UserInvitePo record);

    List<UserInvitePo> selectByExample(UserInvitePoExample example);

    Optional<UserInvitePo> selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") UserInvitePo record, @Param("example") UserInvitePoExample example);

    int updateByExample(@Param("record") UserInvitePo record, @Param("example") UserInvitePoExample example);

    int updateByPrimaryKeySelective(UserInvitePo record);

    int updateByPrimaryKey(UserInvitePo record);
}