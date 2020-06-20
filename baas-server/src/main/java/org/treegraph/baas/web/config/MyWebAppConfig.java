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
        String[] needTokenAuthUrls = new String[]{
        		"/stat/**", "/treegraph/stat/**",
        		"/sys/**", "/treegraph/sys/**",
                "/user/**", "/treegraph/user/**",
                "/role/**", "/treegraph/role/**",
                "/cons/**", "/treegraph/cons/**",
                "/blockchain/**", "/treegraph/blockchain/**",
                "/node/**", "/treegraph/node/**"
        };
        String[] noneedTokenAuthUrls = new String[]{
        		"/cons/script/download", "/treegraph/cons/script/download",
        		"/sys/login", "/treegraph/sys/login",
        		"/user/pwd/modify", "/treegraph/user/pwd/modify",
                "/user/invite/accept", "/treegraph/user/invite/accept"
        };
		registry.addInterceptor(loginAuthInterceptor).addPathPatterns(needTokenAuthUrls)
				.excludePathPatterns(noneedTokenAuthUrls);
    }
}