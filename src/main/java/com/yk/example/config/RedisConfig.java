package com.yk.example.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;

@Configuration
public class RedisConfig {

    @Value("${spring.redis.host}")
    private String hostName;

    @Value("${spring.redis.port}")
    private int port;

    @Value("${spring.redis.database}")
    private int database;


    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        JedisConnectionFactory factory = new JedisConnectionFactory();
        factory.setDatabase(database);
        factory.setHostName(hostName);
        factory.setPort(port);
        return factory;
    }



    @Bean
    public StringRedisTemplate stringRedisTemplate() {
          return new StringRedisTemplate(jedisConnectionFactory());
//        return new StringRedisTemplate(redisConnectionFactory());
    }

    @Bean
    public CacheManager cacheManager() {
        StringRedisTemplate stringRedisTemplate = stringRedisTemplate();
        stringRedisTemplate.setDefaultSerializer(new GenericJackson2JsonRedisSerializer());
        return new RedisCacheManager(stringRedisTemplate);
    }
}