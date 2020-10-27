package org.treegraph.baas.core.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.treegraph.baas.core.enums.ErrorCodeEnum;
import org.treegraph.baas.core.exception.BusinessException;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtUtils {

	public static final String HEADER_TOKEN = "X-Token";
	
	private static final String SECRET = "#ljqGro@!ot9876%32017";

	private static final String TOKEN_PREFIX = "Bearer";

	private static final String PARAM_UID = "uid";
	
	private static final String PARAM_ACCOUNT = "account";

	private static final String PARAM_NAME = "name";
	
	private static final String PARAM_FROM_UID = "fromUid";
	
	private static final String PARAM_TO_UID = "toUid";

	private static final String PARAM_INVITE_ID = "invite_id";

	/**
	 * 生成token
	 */
	public static String generateToken(String uid, String account, String name, long ttlSecs) {

		HashMap<String, Object> map = new HashMap<>();
		// you can put any data in the map
		map.put(PARAM_UID, uid);
		map.put(PARAM_ACCOUNT, account);
		map.put(PARAM_NAME, name);

		JwtBuilder jwtBuilder = Jwts.builder().setClaims(map).signWith(SignatureAlgorithm.HS512, SECRET);

		// 过期时间
		if (ttlSecs >= 0) {
			long nowMillis = System.currentTimeMillis();
			long expMillis = nowMillis + ttlSecs * 1000;
			Date exp = new Date(expMillis);
			jwtBuilder.setExpiration(exp);
		}

		return jwtBuilder.compact();
	}

	/**
	 * 验证token
	 */
	public static Map<String, Object> validateToken(String token) {
		try {
			// parse the token.
			return Jwts.parser().setSigningKey(SECRET)
					.parseClaimsJws(token.replace(TOKEN_PREFIX, "")).getBody();
		} catch (ExpiredJwtException e) {
			throw new BusinessException(ErrorCodeEnum.AUTH_TOKEN_ERROR, "签名已经过期");
		} catch (Exception e) {
			throw new BusinessException(ErrorCodeEnum.AUTH_TOKEN_ERROR, "签名验证失败");
		}
	}
	
	public static String getUid(Map<String, Object> map) {
		return (String) map.get(PARAM_UID);
	}

	public static String getAccount(Map<String, Object> map) {
		return (String) map.get(PARAM_ACCOUNT);
	}

	public static String getName(Map<String, Object> map) {
		return (String) map.get(PARAM_NAME);
	}
	
	/**
	 * 生成invite token
	 */
	public static String generateInviteToken(long ttlSecs) {

		HashMap<String, Object> map = new HashMap<>();
		// you can put any data in the map
		map.put(PARAM_UID, PARAM_FROM_UID);
		map.put(PARAM_ACCOUNT, PARAM_TO_UID);
		map.put(PARAM_NAME, PARAM_INVITE_ID);

		JwtBuilder jwtBuilder = Jwts.builder().setClaims(map).signWith(SignatureAlgorithm.HS512, SECRET);

		// 过期时间
		if (ttlSecs >= 0) {
			long nowMillis = System.currentTimeMillis();
			long expMillis = nowMillis + ttlSecs * 1000;
			Date exp = new Date(expMillis);
			jwtBuilder.setExpiration(exp);
		}

		return jwtBuilder.compact();
	}
	
	/**
	 * 验证invite token
	 */
	public static Map<String, Object> validateInviteToken(String inviteToken) {
		try {
			// parse the token.
			return Jwts.parser().setSigningKey(SECRET)
					.parseClaimsJws(inviteToken.replace(TOKEN_PREFIX, "")).getBody();
		} catch (ExpiredJwtException e) {
			throw new BusinessException(ErrorCodeEnum.AUTH_TOKEN_ERROR, "签名已经过期");
		} catch (Exception e) {
			throw new BusinessException(ErrorCodeEnum.AUTH_TOKEN_ERROR, "签名验证失败");
		}
	}
	
	public static String getInviter(Map<String, Object> map) {
		return (String) map.get(PARAM_FROM_UID);
	}

	public static String getAcceptor(Map<String, Object> map) {
		return (String) map.get(PARAM_TO_UID);
	}

	public static String getInviteId(Map<String, Object> map) {
		return (String) map.get(PARAM_INVITE_ID);
	}
}
