package com.aws.rest.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AwsDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(AwsDemoApplication.class, args);
		System.out.println("Application is up!!");
	}

}
