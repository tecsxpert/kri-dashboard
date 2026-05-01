package com.internship.tool.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.Map;

@Configuration
public class RedisConfig {

    // ------------------------------------------------------------------ //
    //  Shared TTL                                                          //
    // ------------------------------------------------------------------ //

    private static final Duration TTL = Duration.ofMinutes(10);

    // ------------------------------------------------------------------ //
    //  Default cache configuration                                         //
    // ------------------------------------------------------------------ //

    @Bean
    public RedisCacheConfiguration defaultCacheConfig() {
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(TTL)
                .disableCachingNullValues()
                .serializeKeysWith(
                        RedisSerializationContext.SerializationPair
                                .fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair
                                .fromSerializer(new GenericJackson2JsonRedisSerializer()));
    }

    // ------------------------------------------------------------------ //
    //  Cache manager with per-cache TTL configuration                      //
    // ------------------------------------------------------------------ //

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory factory,
                                          RedisCacheConfiguration defaultCacheConfig) {

        Map<String, RedisCacheConfiguration> cacheConfigs = Map.of(
                "kriRecords",    defaultCacheConfig,   // GET /all  (page-scoped key)
                "kriRecordById", defaultCacheConfig    // GET /{id}
        );

        return RedisCacheManager.builder(factory)
                .cacheDefaults(defaultCacheConfig)
                .withInitialCacheConfigurations(cacheConfigs)
                .build();
    }
}
