package com.team1.spreet.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfig extends AsyncConfigurerSupport {

	@Bean
	public ThreadPoolTaskExecutor getAsyncExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(3);     // 기본적으로 실행 대기 중인 Thread 개수
		executor.setMaxPoolSize(10);     // 동시에 동작하는 최대 Thread 개수
		executor.setQueueCapacity(50);   // Queue 사이즈
		executor.setThreadNamePrefix("async-");
		executor.initialize();
		return executor;
	}
}
