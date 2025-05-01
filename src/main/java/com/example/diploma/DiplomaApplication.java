package com.example.diploma;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaAuditing
@EnableJpaRepositories("com.example.diploma.repository.jpa")
public class DiplomaApplication {

	public static void main(String[] args) {
		SpringApplication.run(DiplomaApplication.class, args);
	}

}
