package org.treegraph.baas.core.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.treegraph.baas.core.persistence.DataScopePlugin;

import com.alibaba.druid.pool.DruidDataSource;

/**
 * 操作baas中数据库表，主要数据源
 */
@Configuration
@MapperScan(basePackages = PrimaryDataSourceConfiguration.PACKAGE, sqlSessionFactoryRef = "primarySqlSessionFactory")
public class PrimaryDataSourceConfiguration {
    // 精确到 primary 目录，以便跟其他数据源隔离
    static final String PACKAGE = "org.treegraph.baas.core.mapper.primary";
    private static final String MAPPER_LOCATION = "classpath*:mapper/primary/*.xml";

    @Value("${spring.datasource.druid.data-sources.primary.url}")
    private String url;

    @Value("${spring.datasource.druid.data-sources.primary.username}")
    private String username;

    @Value("${spring.datasource.druid.data-sources.primary.password}")
    private String password;

    @Value("${spring.datasource.druid.data-sources.primary.driver-class-name}")
    private String driverClass;

    @Bean(name = "primaryDataSource")
    @Primary
    public DataSource primaryDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setName("primary");
        dataSource.setDriverClassName(driverClass);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }

    @Bean(name = "primaryTransactionManager")
    @Primary
    public DataSourceTransactionManager primaryTransactionManager() {
        return new DataSourceTransactionManager(primaryDataSource());
    }

    @Bean(name = "primarySqlSessionFactory")
    @Primary
    public SqlSessionFactory primarySqlSessionFactory(@Qualifier("primaryDataSource") DataSource primaryDataSource, @Qualifier("dataScopePlugin") DataScopePlugin dataScopePlugin/*, @Qualifier("pageInterceptor") PageInterceptor pageInterceptor*/)
            throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(primaryDataSource);
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources(PrimaryDataSourceConfiguration.MAPPER_LOCATION));
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setMapUnderscoreToCamelCase(true);
        sessionFactory.setConfiguration(configuration);
        sessionFactory.setPlugins(/*pageInterceptor, */dataScopePlugin);
        return sessionFactory.getObject();
    }

}
