package org.treegraph.baas.web.config;

import javax.annotation.Resource;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.treegraph.baas.web.interceptor.LoginAuthInterceptor;
import org.treegraph.baas.web.interceptor.WebAuthInterceptor;

@Configuration
public class MyWebAppConfig /*extends WebMvcConfigurerAdapter*/ implements WebMvcConfigurer {
	
    @Resource
    private LoginAuthInterceptor loginAuthInterceptor;
    
    @Resource
    private WebAuthInterceptor apiAuthInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
    	
        String[] clientTokenAuthUrls = new String[]{
                "/operator/**", "/repaytoolkit/operator/**",
                "/debtor/**", "/repaytoolkit/debtor/**",
                "/contract/**", "/repaytoolkit/contract/**",
                "/repayOrder/**", "/repaytoolkit/repayOrder/**"
        };
        String[] apiUrls = new String[]{"/api/**", "/repaytoolkit/api/**"};

        registry.addInterceptor(loginAuthInterceptor).addPathPatterns(clientTokenAuthUrls);
        registry.addInterceptor(apiAuthInterceptor).addPathPatterns(apiUrls);
    }
}