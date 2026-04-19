package com.budaos.modules.core.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

/**
 * Redis Cache 统一配置。
 * <p>
 * 使用带完整类型信息（activateDefaultTyping）的 ObjectMapper，
 * 序列化格式为 ["com.budaos.xxx.ClassName", {...}]，
 * 避免使用 GenericJackson2JsonRedisSerializer 默认的 @class 字段嵌入方式，
 * 同时解决包名从 com.creatorblue.* 重构为 com.budaos.* 后旧缓存反序列化失败的问题。
 * </p>
 * <p>
 * <b>注意：</b>升级包名后需手动清除 Redis 中的旧缓存 key（flushdb 或按 key pattern 删除），
 * 否则旧数据仍会反序列化失败，本配置仅对新写入的数据生效。
 * </p>
 */
@Configuration
@EnableCaching
@ConditionalOnClass(RedisConnectionFactory.class)
public class RedisCacheConfig {

    /**
     * 构建用于 Redis 缓存序列化的 ObjectMapper。
     * 启用默认类型信息（NON_FINAL），使得序列化后 JSON 中包含新包名，
     * 可被正确反序列化。
     */
    private ObjectMapper buildCacheObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        // 使用数组包装类型信息：["com.budaos.xxx.ClassName", {...}]
        mapper.activateDefaultTyping(
                LaissezFaireSubTypeValidator.instance,
                ObjectMapper.DefaultTyping.NON_FINAL
        );
        return mapper;
    }

    /**
     * 自定义 CacheManager，替换 Spring Boot 自动配置的默认实现。
     * <ul>
     *   <li>key 使用 StringRedisSerializer（可读性好）</li>
     *   <li>value 使用 Jackson2JsonRedisSerializer（带类型信息，支持正确反序列化）</li>
     *   <li>默认缓存 TTL：1 小时（可按需调整）</li>
     * </ul>
     */
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        Jackson2JsonRedisSerializer<Object> valueSerializer =
                new Jackson2JsonRedisSerializer<>(Object.class);
        valueSerializer.setObjectMapper(buildCacheObjectMapper());

        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofHours(1))
                .serializeKeysWith(
                        RedisSerializationContext.SerializationPair
                                .fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair
                                .fromSerializer(valueSerializer))
                // 不缓存 null 值，避免空指针污染缓存
                .disableCachingNullValues();

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(config)
                .build();
    }
}
