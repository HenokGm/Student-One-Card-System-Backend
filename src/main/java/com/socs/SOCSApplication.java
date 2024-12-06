package com.socs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing(auditorAwareRef = "auditorAware")
public class SOCSApplication {

	public static void main(String[] args) {
		SpringApplication.run(SOCSApplication.class, args);
	}

}
