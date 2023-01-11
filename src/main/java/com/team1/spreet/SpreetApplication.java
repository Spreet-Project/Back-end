package com.team1.spreet;

import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@Slf4j
public class SpreetApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpreetApplication.class, args);
	}

	@Bean
	public RedissonClient redissonClient(@Value("${spring.redis.host}") String host, @Value("${spring.redis.port}") String port) {
		Config config = new Config();
		config.useSingleServer().setAddress("redis://" + host + ":" + port);
		log.info("redis://" + host + ":" + port);
		return Redisson.create(config);
	}
}
