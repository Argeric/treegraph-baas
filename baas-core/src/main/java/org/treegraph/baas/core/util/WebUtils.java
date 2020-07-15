package org.treegraph.baas.core.util;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;

import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;

public class WebUtils extends org.springframework.web.util.WebUtils {

	/**
	 * 获取IP
	 */
	public static String getIpAddress(HttpServletRequest request) {
		List<String> ipList = Lists.newArrayList();
		String ip = request.getHeader("x-forwarded-for");

		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		if (ip != null && ip.indexOf(":") >= 0) { // 判断是否为IPV6地址
			ip = "127.0.0.1";
		}
		//log.info("ip is :{}", ip);
		Matcher matcher = Pattern
				.compile("(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)" + "(\\.(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)){3}")
				.matcher(ip);
		while (matcher.find()) {
			String tmp = matcher.group();
			ipList.add(tmp);
		}

		return CollectionUtils.isEmpty(ipList) ? StringUtils.EMPTY : ipList.get(0);
	}

	/**
	 * 判断该请求是否是AJAX请求
	 */
	public static boolean isAjax(HttpServletRequest request) {

		String ajaxRequest = request.getHeader("X-Requested-With");

		return "XMLHttpRequest".equalsIgnoreCase(ajaxRequest);
	}

	/**
	 * 获取cookies值
	 */
	public static String getCookiesValue(HttpServletRequest request, String cookiesName) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null && cookies.length > 0) {
			return null;
		}
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals(cookiesName)) {
				return cookie.getValue();
			}
		}
		return null;
	}

	/**
	 * 获取请求host地址
	 */
	public static String getRequestHost(HttpServletRequest request) {
		String url = request.getRequestURL().toString();
		String uri = request.getRequestURI();
		return url.replace(uri, "");
	}

	/**
	 * 获取登录设备信息
	 *
	 * @param request
	 * @return
	 */
	public static String getDeviceeInfo(HttpServletRequest request) {
		String userAgent = request.getHeader("User-Agent");
		if (StringUtils.isBlank(userAgent)) {
			return "无法获取userAgent";
		}
		UserAgent ua = UserAgent.parseUserAgentString(userAgent);
		if (ua != null) {
			Browser browser = ua.getBrowser();
			OperatingSystem operatingSystem = ua.getOperatingSystem();
			if (browser == Browser.UNKNOWN || operatingSystem == OperatingSystem.UNKNOWN) {
				return userAgent;
			}
			return browser.getName() + "," + operatingSystem.getName();
		} else {
			return userAgent;
		}
	}
}
