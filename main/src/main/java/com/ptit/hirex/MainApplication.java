package com.ptit.hirex;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@ComponentScan(basePackages = {"com.ptit"})
@EnableJpaRepositories(basePackages = {"com.ptit"} )
@EnableRedisRepositories(basePackages = "com.ptit.data.repository")
@EntityScan(basePackages = "com.ptit")
@SpringBootApplication
public class MainApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.load();
//		System.setProperty("DB_URL", dotenv.get("DB_URL"));

		SpringApplication.run(MainApplication.class, args);
	}

}
