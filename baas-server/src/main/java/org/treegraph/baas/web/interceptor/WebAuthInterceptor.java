package org.treegraph.baas.web.interceptor;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class WebAuthInterceptor extends HandlerInterceptorAdapter {
	
    private static final String REQUEST_TIME_PARAM_NAME = "reqTime";
    
    private static final String API_TOKEN_PARAM_NAME = "apiToken";
    
    private static final String REQUEST_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    @Value("${api.auth.timeSpan}")
    private Long timeSpan;
    
    @Value("${api.auth.securityKey}")
    private String securityKey;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//    	
//        if (!isLegalReqTime(request)) {
//            outputAuthFailResult(response, "api接口reqTime不合法");
//            return false;
//        }
//
//        if (!isLegalApiToken(request)) {
//            outputAuthFailResult(response, "api接口apiToken不合法");
//            return false;
//        }
//        
//        if(SwitchConfig.switchRepayToolkit == IntTypeEnum.FALSE.getValue()){
//        	outputAuthFailResult(response, "系统维护中，请使用其他还款方式！");
//            return false;
//		}
//        
//        log.info("API接口请求合法性校验通过, requestUri:{}", request.getRequestURI());
//        
        return true;
    }

    private boolean isLegalReqTime(HttpServletRequest request) throws ParseException {
    	
//        String requestTimeStr =request.getParameter(REQUEST_TIME_PARAM_NAME);
//        if (Strings.isNullOrEmpty(requestTimeStr)) {
//            log.error("API接口请求不合法,requestTime缺失,requestUri:{}", request.getRequestURI());
//            return false;
//        }
//        
//        Date requestTime = parseStrToDate(requestTimeStr);
//        Long reqTimeStamp = requestTime.getTime();
//        Date nowTime = new Date();
//        Long nowTimeStamp = nowTime.getTime();
//        String nowTimeStr = DateTools.format(nowTime, DateTools.SYSTEM_TIMESTAMP_FORMAT);
//        if (Math.abs(nowTimeStamp - reqTimeStamp) > timeSpan) {
//            log.error("API接口请求不合法, 超过允许处理的时间范围,requestUri:{},当前系统时间:{},请求的reqTime时间:{},允许最大时间差:{}",
//                    request.getRequestURI(), nowTimeStr, requestTimeStr, timeSpan);
//            return false;
//        }
//        
        return true;
    }

    private boolean isLegalApiToken(HttpServletRequest request) {
//    	
//        String apiToken = request.getParameter(API_TOKEN_PARAM_NAME);
//        if (Strings.isNullOrEmpty(apiToken)) {
//            log.warn("请求不合法,apiToken缺失,requestUri:{}", request.getRequestURI());
//            return false;
//        }
//
//        Map<String, String[]> allParams = request.getParameterMap();
//        Map<String, String> validParams = Maps.newHashMap();
//        for (Map.Entry<String, String[]> param : allParams.entrySet()) {
//            if (param.getKey().equals(API_TOKEN_PARAM_NAME)) {
//                continue;
//            }
//            StringBuilder sb = new StringBuilder();
//            for (String val : param.getValue()) {
//                sb.append(val);
//            }
//            validParams.put(param.getKey(), sb.toString());
//        }
//        
//        String expectedApiToken = ApiAuthUtil.calcToken(securityKey, validParams);
//        if (!apiToken.toLowerCase().equals(expectedApiToken)) {
//            log.warn("请求不合法,apiToken错误,requestUri:{}", request.getRequestURI());
//            return false;
//        }
        
        return true;
    }

    private Date parseStrToDate(String dateStr) throws ParseException {
    	
        SimpleDateFormat dateFormat = new SimpleDateFormat(REQUEST_TIME_FORMAT);
        Date reqTime = dateFormat.parse(dateStr);
        
        return reqTime;
    }

    private void outputAuthFailResult(HttpServletResponse response, String msg) throws IOException {
//        RepaymentResult<Void> result = RepaymentResult.fail(msg);
//        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
//        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
//        response.getWriter().print(new ObjectMapper().writeValueAsString(result));
    }
}
