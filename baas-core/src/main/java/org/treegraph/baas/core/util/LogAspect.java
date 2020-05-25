package org.treegraph.baas.core.util;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.treegraph.baas.common.util.WebHelper;
import org.treegraph.baas.core.service.LogService;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
@Order(0)
@AllArgsConstructor
public class LogAspect {
	
	private LogService logService;

    /**
     * @Description: 日志切点
     */
	@Pointcut("execution(* org.treegraph.baas.web.rest..*.*(..)) || execution(* org.treegraph.baas.core.service.impl..*.*(..)) || execution(* org.treegraph.baas.core.das.impl..*.*(..))")
    public void logPointCut() {
    }

    @Around(value = "logPointCut() && !@annotation(LogSkip)")
    public Object logOperate(ProceedingJoinPoint joinPoint) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        Object result = null;
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        String methodName = method.getDeclaringClass().getSimpleName() + "." + method.getName();

        String userId = null; 
        try{
        	userId = WebHelper.getUserId(); 
        } catch(Exception e){
        }
        
		Object[] argArrays = null;
		Object[] originalArrays = joinPoint.getArgs();
		if (!CollectionUtils.isEmpty(originalArrays)) {
			List<Object> argList = Lists.newArrayList(originalArrays).stream()
					.filter(item -> isShowArg(item)).collect(Collectors.toList());
			argArrays = CollectionUtils.isNotEmpty(argList) ? argList.toArray() : null;
		}

        log.info("[userId={}]{} req:{}", userId, methodName, JSON.toJSONString(argArrays));
        try {
            result = joinPoint.proceed();
        } catch (Throwable e) {
        	addOperLog(methodName, JSON.toJSONString(argArrays), StringUtils.substring(e.getMessage(), 0, 100));
            log.error("[userId={}]{} resp:{},elapsed:{},err:", userId, methodName, JSON.toJSONString(result), stopWatch.getTime(), e);
            throw e;
        }
        addOperLog(methodName, JSON.toJSONString(argArrays), JSON.toJSONString(result));
        log.info("[userId={}]{} resp:{},elapsed:{}", userId, methodName, JSON.toJSONString(result), stopWatch.getTime());
        
        return result;
    }

	private boolean isShowArg(Object item) {
		if (item instanceof HttpServletRequest) {
			return false;
		}
		// add other subject
		return true;
	}
	
	private void addOperLog(String methodName, String req, String resp) {
		String[] paras = StringUtils.split(methodName, ".");
		if (!paras[0].endsWith("Controller") || WebHelper.getUserId() == null) {
			return;
		}
		StringBuilder info = new StringBuilder("req=").append(req).append(",resp=").append(resp);
		logService.addOperLog(WebHelper.getUserId(), WebHelper.getUserName(), WebHelper.getUserIp(),
				paras[1], info.toString());
	}
}
