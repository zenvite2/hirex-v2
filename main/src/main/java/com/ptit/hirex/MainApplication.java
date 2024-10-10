package com.ptit.hirex;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@EnableJpaRepositories(basePackages = {"com.ptit.data"} )
@EntityScan(basePackages = "com.ptit.data")
@EnableRedisRepositories(basePackages = "com.ptit.data.repository")
@SpringBootApplication
@ComponentScan(basePackages = {"com.ptit"})
public class MainApplication {
	public static void main(String[] args) {
		SpringApplication.run(MainApplication.class, args);
	}
}
