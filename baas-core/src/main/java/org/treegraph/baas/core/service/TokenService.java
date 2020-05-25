package org.treegraph.baas.core.service;

import org.treegraph.baas.core.domain.UserInvitePo;
import org.treegraph.baas.core.domain.UserInfoPo;

public interface TokenService {

	UserInfoPo checkToken(String token);
	
	UserInvitePo checkInvitToken(String token);
	
	void authPermission(String userId, String uri);
}
