package com.team1.spreet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SpreetApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpreetApplication.class, args);
	}

}
