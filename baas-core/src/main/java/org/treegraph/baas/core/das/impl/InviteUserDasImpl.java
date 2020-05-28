package org.treegraph.baas.core.das.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.treegraph.baas.core.das.InviteUserDas;
import org.treegraph.baas.core.domain.UserInvitePo;
import org.treegraph.baas.core.domain.UserRoleInvitePo;
import org.treegraph.baas.core.domain.UserRoleInvitePoExample;
import org.treegraph.baas.core.mapper.primary.UserInvitePoMapper;
import org.treegraph.baas.core.mapper.primary.UserRoleInvitePoMapper;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class InviteUserDasImpl implements InviteUserDas{
	
    private UserInvitePoMapper userInvitePoMapper;
    
    private UserRoleInvitePoMapper userRoleInvitePoMapper;
    
    public Optional<UserInvitePo> findById(Long inviteId) {
        return userInvitePoMapper.selectByPrimaryKey(inviteId);
    }
    
    public List<UserRoleInvitePo> findByInviteId(Long inviteId){
    	UserRoleInvitePoExample example = new UserRoleInvitePoExample();
    	example.createCriteria().andInviteIdEqualTo(inviteId);
    	return userRoleInvitePoMapper.selectByExample(example);
    }
}
