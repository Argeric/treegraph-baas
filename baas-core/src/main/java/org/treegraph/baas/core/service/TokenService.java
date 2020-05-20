package org.treegraph.baas.core.service;

import org.treegraph.baas.core.domain.InviteUserPo;
import org.treegraph.baas.core.domain.UserInfoPo;

public interface TokenService {

	UserInfoPo checkToken(String token);
	
	InviteUserPo checkInvitToken(String token);
	
	void authPermission(String userId, String uri);
}
