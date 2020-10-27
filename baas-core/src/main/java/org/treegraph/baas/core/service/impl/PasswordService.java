package org.treegraph.baas.core.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.treegraph.baas.core.util.crypto.bcrypt.BCryptPasswordEncoder;

@Service
public class PasswordService {

//	@Value("${password.rasPrivateKey}")
//	private String passwordRsaPrivateKey;

	@Value("${password.default}")
	private String defaultPassword;

//	/**
//	 * 解密传输过来的密码
//	 *
//	 * @param password password
//	 * @return plain
//	 */
//	public String decryptByRsa(String password) {
//		return RSAUtils.decryptByPrivateKey(password, passwordRsaPrivateKey);
//	}

	/**
	 * 加密
	 * 
	 * @param password password
	 * @return crypt text
	 */
	public String bcryptEncoder(String password) {
		return BCryptPasswordEncoder.create().encode(password);
	}

	/**
	 * 匹配
	 * 
	 * @param password password
	 * @param encodePassword encodePassword
	 * @return match result
	 */
	public boolean matches(String password, String encodePassword) {
		return BCryptPasswordEncoder.create().matches(password, encodePassword);
	}

	public String getDefaultPwd() {
		return bcryptEncoder(defaultPassword);
	}
}
