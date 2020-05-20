package org.treegraph.baas.core.das.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.treegraph.baas.core.das.InviteUserDas;
import org.treegraph.baas.core.domain.InviteUserPo;
import org.treegraph.baas.core.domain.InviteUserRolePo;
import org.treegraph.baas.core.domain.InviteUserRolePoExample;
import org.treegraph.baas.core.mapper.InviteUserPoMapper;
import org.treegraph.baas.core.mapper.InviteUserRolePoMapper;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class InviteUserDasImpl implements InviteUserDas{
	
    private InviteUserPoMapper inviteUserPoMapper;
    
    private InviteUserRolePoMapper inviteUserRolePoMapper;
    
    public Optional<InviteUserPo> findById(Long inviteId) {
        return inviteUserPoMapper.selectByPrimaryKey(inviteId);
    }
    
    public List<InviteUserRolePo> findByInviteId(Long inviteId){
    	InviteUserRolePoExample example = new InviteUserRolePoExample();
    	example.createCriteria().andInviteIdEqualTo(inviteId);
    	return inviteUserRolePoMapper.selectByExample(example);
    }
}
