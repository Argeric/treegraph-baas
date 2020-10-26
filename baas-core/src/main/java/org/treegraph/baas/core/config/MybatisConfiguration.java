package org.treegraph.baas.core.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.treegraph.baas.core.persistence.DataScopePlugin;

@Configuration
public class MybatisConfiguration {

    @Bean(name = "dataScopePlugin")
    @ConfigurationProperties(prefix = "data-scope")
    public DataScopePlugin dataScopePlugin() {
        return new DataScopePlugin();
    }
}
