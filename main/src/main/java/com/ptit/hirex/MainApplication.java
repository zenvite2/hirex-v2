package com.ptit.hirex;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@ComponentScan(basePackages = {"com.ptit"})
@EnableMongoRepositories(basePackages = "com.ptit.data")
@EnableMongoAuditing
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class MainApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.load();
		// Uncomment the next line if you want to use environment variables from .env file
		// System.setProperty("DB_URL", dotenv.get("DB_URL"));

		SpringApplication.run(MainApplication.class, args);
	}
}
