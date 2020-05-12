package org.treegraph.baas.core.service;

import javax.servlet.http.HttpServletRequest;

import org.treegraph.baas.api.dto.response.LoginResponse;

public interface LoginService {

	LoginResponse login(String accountNo, String password, HttpServletRequest request);
}
