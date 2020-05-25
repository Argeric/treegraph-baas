package org.treegraph.baas.core.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.treegraph.baas.api.dto.OperLogInfo;
import org.treegraph.baas.api.dto.request.ListOperLogRequest;
import org.treegraph.baas.api.dto.response.ListOperLogResponse;
import org.treegraph.baas.core.das.UserInfoDas;
import org.treegraph.baas.core.das.UserOperLogDas;
import org.treegraph.baas.core.domain.UserInfoPo;
import org.treegraph.baas.core.domain.UserLoginLogPo;
import org.treegraph.baas.core.domain.UserOperLogPo;
import org.treegraph.baas.core.enums.LogEnum.Login;
import org.treegraph.baas.core.service.LogService;
import org.treegraph.baas.core.util.AddressUtils;
import org.treegraph.baas.core.util.DateTimeUtils;
import org.treegraph.baas.core.util.LogSkip;
import org.treegraph.baas.core.util.WebUtils;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class LogServiceImpl implements LogService{

	private UserInfoDas userInfoPoDas;

	private UserOperLogDas userOperLogDas;

	/**
	 * 添加登录日志
	 */
	@LogSkip
	public void addLoginLog(String userId, Login login, HttpServletRequest request) {
		UserLoginLogPo loginLog = new UserLoginLogPo();
		String ip = Objects.isNull(request) ? "" : WebUtils.getIpAddress(request);
		loginLog.setUserId(userId);
		loginLog.setLoginIp(ip);
		loginLog.setIpAddress(AddressUtils.getAddressByIp(ip));
		loginLog.setType(login.getType());
		loginLog.setDevice(Objects.isNull(request) ? "" : WebUtils.getDeviceeInfo(request));
		loginLog.setLoginTime(LocalDateTime.now());
		userOperLogDas.addLoginLog(loginLog);
	}
	
	/**
	 * 添加操作日志
	 */
	@LogSkip
	public void addOperLog(String userId, String userName, String ip, String operCode, String operInfo) {
		LocalDateTime now = LocalDateTime.now();
		UserOperLogPo userOperLog =  UserOperLogPo.builder()
				.userId(userId)
				.userName(userName)
				.userIp(ip)
				.operCode(operCode)
				.operInfo(operInfo)
				.operTime(now)
				.createTime(now)
				.build();
		userOperLogDas.addOperLog(userOperLog);
	}

	/**
	 * 查询操作日志列表
	 */
	@LogSkip
	public ListOperLogResponse listOperLog(ListOperLogRequest req) {
		String userId = null;
		if (req.getAccount() != null) {
			UserInfoPo user = userInfoPoDas.findByAccount(req.getAccount()).orElse(null);
			userId = user != null ? user.getId() : StringUtils.EMPTY;
		}
		PageHelper.startPage(req.getCurrent(), req.getPageSize());
		List<UserOperLogPo> operLogList = userOperLogDas.findAllOperLog(userId, req.getName(), req.getType(),
				req.getIp());
		PageInfo<UserOperLogPo> pageInfo = new PageInfo<>(operLogList);

		List<UserOperLogPo> list = pageInfo.getList();
		List<String> userIds = list.stream().map(UserOperLogPo::getUserId).collect(Collectors.toList());
		List<UserInfoPo> userInfos = userInfoPoDas.findByIds(userIds);
		Map<String, String> userInfoMap = userInfos.stream()
				.collect(Collectors.toMap(UserInfoPo::getId, UserInfoPo::getAccount));
		List<OperLogInfo> logs = list.stream()
				.map(item -> OperLogInfo.builder()
						.id(item.getId() + "")
						.name(item.getUserName())
						.account(userInfoMap.get(item.getUserId()))
						.type(item.getOperCode())
						.createTime(DateTimeUtils.formatTime(item.getCreateTime(), DateTimeUtils.FORMAT_yyyyMMddHHmmss))
						.log(item.getOperInfo())
						.ip(item.getUserIp())
						.build())
				.collect(Collectors.toList());

		return ListOperLogResponse.builder()
				.current(req.getCurrent())
				.pageSize(req.getPageSize())
				.total(pageInfo.getTotal())
				.list(logs)
				.build();
	}
}
