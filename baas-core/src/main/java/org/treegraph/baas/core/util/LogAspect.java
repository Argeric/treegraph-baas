package org.treegraph.baas.core.util;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
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
	
	private final LogService logService;

	@Pointcut("execution(* org.treegraph.baas.web.rest..*.*(..)) || execution(* org.treegraph.baas.core.service.impl..*.*(..)) || execution(* org.treegraph.baas.core.das.impl..*.*(..))")
    public void logPointCut() {
    }

    @Around(value = "logPointCut() && !@annotation(LogSkip)")
    public Object logOperate(ProceedingJoinPoint joinPoint) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        String methodName = method.getDeclaringClass().getSimpleName() + "." + method.getName();

        String userId = null; 
        try{
        	userId = WebHelper.getUserId(); 
        } catch(Exception e){
			log.error("[userId=null]not login, cannot get userId");
        }
        
		Object[] argArrays = null;
		Object[] originalArrays = joinPoint.getArgs();
		if (originalArrays != null && originalArrays.length > 0) {
			List<Object> argList = Lists.newArrayList(originalArrays).stream()
					.filter(this::isShowArg).collect(Collectors.toList());
			argArrays = CollectionUtils.isNotEmpty(argList) ? argList.toArray() : null;
		}

        log.debug("[userId={}]{} req:{}", userId, methodName, JSON.toJSONString(argArrays));
		Object result;
        try {
            result = joinPoint.proceed();
        } catch (Throwable e) {
        	addOperLog(methodName, JSON.toJSONString(argArrays), StringUtils.substring(e.getMessage(), 0, 100));
			log.error("[userId={}]{} resp:null,elapsed:{},err:", userId, methodName, stopWatch.getTime(), e);
            throw e;
        }
        addOperLog(methodName, JSON.toJSONString(argArrays), JSON.toJSONString(result));
        log.debug("[userId={}]{} resp:{},elapsed:{}", userId, methodName, JSON.toJSONString(result), stopWatch.getTime());
        
        return result;
    }

	private boolean isShowArg(Object item) {
		return !(item instanceof HttpServletRequest);
	}
	
	private void addOperLog(String methodName, String req, String resp) {
		String[] paras = StringUtils.split(methodName, ".");
		if (!paras[0].endsWith("Controller") || WebHelper.getUserId() == null) {
			return;
		}
		logService.addOperLog(WebHelper.getUserId(), WebHelper.getUserName(), WebHelper.getUserIp(), null, paras[1],
				"req=" + req + ",resp=" + resp);
	}
}
