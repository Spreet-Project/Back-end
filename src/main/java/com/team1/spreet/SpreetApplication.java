package com.team1.spreet;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@Slf4j
public class SpreetApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpreetApplication.class, args);
	}
}
