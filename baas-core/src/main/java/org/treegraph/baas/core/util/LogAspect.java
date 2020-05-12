package org.treegraph.baas.core.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.time.StopWatch;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
@Order(0)
public class LogAspect {

    /**
     * @Description: 日志切点
     */
    @Pointcut("execution(* org.treegraph.baas.web.controller..*.*(..)) || execution(* org.treegraph.baas.core.service.impl..*.*(..)) || execution(* org.treegraph.baas.core.dao.impl..*.*(..))")
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
        	//userId = RequestUtils.getUserId(); 
        } catch(Exception e){
        }

        log.info("[userId={}]{} req:{}", userId, methodName, JSON.toJSONString(joinPoint.getArgs()));
        try {
            result = joinPoint.proceed();
        } catch (Throwable e) {
            log.error("[userId={}]{} resp:{},elapsed:{},err:", userId, methodName, JSON.toJSONString(result), stopWatch.getTime(), e);
            throw e;
        }
        log.info("[userId={}]{} resp:{},elapsed:{}", userId, methodName, JSON.toJSONString(result), stopWatch.getTime());
        
        return result;
    }
    
    public static void main(String[] params) {
    	Object o = new Date();
    	Object[] args = {"account", "pass", o};
		List<Object> toLogArgs = null;
		if (!CollectionUtils.isEmpty(args)) {
			ArrayList<Object> argList = Lists.newArrayList(args);
			System.out.println(argList);
			toLogArgs = Lists.newArrayList(args).stream()
					.filter(arg -> !arg.getClass().isAssignableFrom(Date.class))
					.collect(Collectors.toList());
		}
		System.out.println(toLogArgs);
	}
}
