package org.treegraph.baas.core.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissionConfiguration {
	
    @Value("${spring.redis.host}")
    private String redisHost;
    @Value("${spring.redis.port}")
    private String redisPort;
    @Value("${spring.redis.database}")
    private String redisDatabase;
    @Value("${spring.redis.password}")
    private String redisPassword;
    
    @Autowired
    private RedisProperties redisProperties;

    @Bean
    public RedissonClient redission() {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://"+redisHost + ":" + redisPort)
                .setDatabase(Integer.parseInt(redisDatabase)).setPassword(redisPassword);
        RedissonClient redisson = Redisson.create(config);
        return redisson;
    }
}