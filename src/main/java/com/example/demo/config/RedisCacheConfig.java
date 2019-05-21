package com.example.demo.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.util.HashMap;
import java.util.Map;


@Configuration
@EnableConfigurationProperties({CacheProperties.class,CustomCacheProperties.class})
public class RedisCacheConfig {

    @Autowired
    private CacheProperties cacheProperties;
    @Autowired
    private CustomCacheProperties customCacheProperties;

    @Bean
    public KeyGenerator simpleKeyGenerator() {
        return (o, method, objects) -> {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(o.getClass().getSimpleName());
            stringBuilder.append(".");
            stringBuilder.append(method.getName());
            stringBuilder.append("[");
            for (Object obj : objects) {
                stringBuilder.append(obj.toString());
            }
            stringBuilder.append("]");

            return stringBuilder.toString();
        };
    }

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        return new RedisCacheManager(
                RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory),
                this.getRedisCacheConfigurationWithTtl(this.cacheProperties.getRedis()), // 默认策略，未配置的 key 会使用这个
                this.getRedisCacheConfigurationMap() // 指定 key 策略
        );
    }

    private Map<String, RedisCacheConfiguration> getRedisCacheConfigurationMap() {
        Map<String, RedisCacheConfiguration> redisCacheConfigurationMap = new HashMap<>();
        this.customCacheProperties.getCustomCache().forEach((name,config)->{
            redisCacheConfigurationMap.put(name,this.getRedisCacheConfigurationWithTtl(config));
        });
        return redisCacheConfigurationMap;
    }

    private RedisCacheConfiguration getRedisCacheConfigurationWithTtl(CacheProperties.Redis redisProperties) {
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = getJackson2JsonRedisSerializer();
        RedisSerializationContext.SerializationPair<Object> pair = RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializer);
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig();
        redisCacheConfiguration = redisCacheConfiguration.serializeValuesWith(pair).computePrefixWith(cacheName-> cacheName.concat(":"))
        .entryTtl(redisProperties.getTimeToLive());

        return redisCacheConfiguration;
    }

    /**
     * 配置json序列化
     */
    @Bean
    @Primary
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = getJackson2JsonRedisSerializer();
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }


    private Jackson2JsonRedisSerializer getJackson2JsonRedisSerializer(){
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
        return jackson2JsonRedisSerializer;
    }



}