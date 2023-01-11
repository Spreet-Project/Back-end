package com.team1.spreet.config;

import com.team1.spreet.entity.EmailConfirm;
import org.redisson.api.MapOptions;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class RedisConfig {
    private RedissonClient redissonClient;

    public RedisConfig(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Bean
    public RMapCache<String, EmailConfirm> emailConfirmRMapCache() {
        final RMapCache<String, EmailConfirm> emailConfirmRMapCache
                = redissonClient.getMapCache("email_confirm", MapOptions.<String, EmailConfirm>defaults()
                .writeMode(MapOptions.WriteMode.WRITE_BEHIND));

        return emailConfirmRMapCache;
    }
}
