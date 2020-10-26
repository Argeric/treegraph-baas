package org.treegraph.baas.core.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.treegraph.baas.api.dto.AuthInfo;
import org.treegraph.baas.api.dto.response.LoginResponse;

public interface LoginService {

	LoginResponse login(String accountNo, String password, HttpServletRequest request);
	
	List<AuthInfo> getAuthInfo(String userId);
}
