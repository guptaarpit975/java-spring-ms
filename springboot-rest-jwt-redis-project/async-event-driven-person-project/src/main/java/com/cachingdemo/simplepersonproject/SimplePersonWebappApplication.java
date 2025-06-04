package com.cachingdemo.simplepersonproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class SimplePersonWebappApplication {
	public static void main(String[] args) {
		SpringApplication.run(SimplePersonWebappApplication.class, args);
	}

}
