package org.treegraph.baas.core.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.treegraph.baas.core.persistence.DataScopePlugin;

@Configuration
public class MybatisConfiguration {
	
//    @Bean(name = "pageInterceptor")
//    public PageInterceptor pageInterceptor() {
//        PageInterceptor pageInterceptor = new PageInterceptor();
//        Properties properties = new Properties();
//        properties.setProperty("helperDialect", "mysql");
//        properties.setProperty("reasonable", "true");
//        
//        properties.setProperty("rowboundsWithCount", "true");
//        properties.setProperty("offsetAsPageNum", "true");
//        properties.setProperty("supportMethodsArguments", "false");
//        
//        pageInterceptor.setProperties(properties);
//        return pageInterceptor;
//    }
    
    @Bean(name = "dataScopePlugin")
    @ConfigurationProperties(prefix = "data-scope")
    public DataScopePlugin dataScopePlugin() {
        return new DataScopePlugin();
    }
}
