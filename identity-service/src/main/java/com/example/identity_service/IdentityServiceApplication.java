package com.example.identity_service;

import com.example.identity_service.configuration.EnvLoader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class IdentityServiceApplication {
	public static void main(String[] args) {
		EnvLoader.loadEnv(); // Nạp file .env vào System properties
		SpringApplication.run(IdentityServiceApplication.class, args);
	}

}
