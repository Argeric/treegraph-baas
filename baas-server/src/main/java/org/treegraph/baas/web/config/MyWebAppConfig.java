package org.treegraph.baas.web.config;

import javax.annotation.Resource;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.treegraph.baas.web.interceptor.LoginAuthInterceptor;

@Configuration
public class MyWebAppConfig /*extends WebMvcConfigurerAdapter*/ implements WebMvcConfigurer {
	
    @Resource
    private LoginAuthInterceptor loginAuthInterceptor;
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
    	
        String[] clientTokenAuthUrls = new String[]{
                "/user/**", "/treegraph/user/**",
                "/role/**", "/treegraph/role/**",
                "/consortium/**", "/treegraph/consortium/**",
                "/blockchain/**", "/treegraph/blockchain/**",
                "/node/**", "/treegraph/node/**"
        };
        
        registry.addInterceptor(loginAuthInterceptor).addPathPatterns(clientTokenAuthUrls);
    }
}