package com.fly.jsonredis.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 使用redis的连接池
 */
@Configuration
public class RedisConfig {

    @Value("${spring.redis.database}")
    private int database; // 添加数据库分区配置

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Value("${spring.redis.password}")
    private String password;

    @Value("${spring.redis.timeout}")
    private int timeout;

    @Value("${spring.redis.jedis.pool.max-active}")
    private int maxActive;

    @Value("${spring.redis.jedis.pool.max-idle}")
    private int maxIdle;

    @Value("${spring.redis.jedis.pool.min-idle}")
    private int minIdle;

    @Value("${spring.redis.jedis.pool.max-wait}")
    private long maxWait;

    @Bean
    public JedisPool jedisPool() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(maxActive); // 最大连接数
        poolConfig.setMaxIdle(maxIdle);   // 最大空闲连接数
        poolConfig.setMinIdle(minIdle);   // 最小空闲连接数
        poolConfig.setMaxWaitMillis(maxWait); // 最大等待时间

        //没有配置密码的情况 默认使用 分区  DB0
        if (password == null || password.isEmpty()) {
            return new JedisPool(poolConfig, host, port, timeout);
        } else {
            return new JedisPool(poolConfig, host, port, timeout, password);
        }

    }


    


}
