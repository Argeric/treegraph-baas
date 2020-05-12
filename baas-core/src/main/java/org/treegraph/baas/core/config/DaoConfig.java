package org.treegraph.baas.core.config;

import javax.sql.DataSource;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;

@MapperScan(basePackages = "org.treegraph.baas.core.mapper")
@EnableTransactionManagement(proxyTargetClass = true)
@Configuration
public class DaoConfig {

    /**
     * 解决springboot多数据源中必须 选定一个primary
     *
     * {@code Qualifier} 才会生效
     * 
     * @return
     */
    @Primary
    @Bean
    public DataSource dataSource() {
        return DruidDataSourceBuilder.create().build();
    }

    @ConditionalOnBean(DataSource.class)
    @Bean
    public PlatformTransactionManager txManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

}
