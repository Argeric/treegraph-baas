package org.treegraph.baas.web.interceptor;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.treegraph.baas.api.dto.BaasResult;
import org.treegraph.baas.common.util.WebHelper;
import org.treegraph.baas.core.domain.UserInfoPo;
import org.treegraph.baas.core.service.TokenService;
import org.treegraph.baas.core.util.JwtUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class LoginAuthInterceptor extends HandlerInterceptorAdapter {
	
    @Resource
    private TokenService tokenService;
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
    	
        String token = request.getHeader(JwtUtils.HEADER_TOKEN);

        UserInfoPo user = null;
        try {
        	user = tokenService.checkToken(token);
        } catch(Exception e) {
        	outputAuthFailResult(response, e.getMessage());
        	return false;
        }
    	
		WebHelper.init(user.getId(), user.getAccount(), user.getName());
		
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        if (ex != null) {
            log.error("[userId={}]LoginAuthInterceptor uri:{},err:", WebHelper.getUserId(), request.getRequestURI(), ex);
        }
        WebHelper.destroy();
    }

    private void outputAuthFailResult(HttpServletResponse response, String msg) throws IOException {
        BaasResult<Void> result = BaasResult.fail(msg);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.getWriter().print(new ObjectMapper().writeValueAsString(result));
    }
}