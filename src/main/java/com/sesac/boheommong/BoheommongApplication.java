package com.sesac.boheommong;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class BoheommongApplication {

	public static void main(String[] args) {
		SpringApplication.run(BoheommongApplication.class, args);
	}

}
