package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * NutriTrackApplication
 *
 * Entry point for the Spring Boot application.
 * This class bootstraps the application, initializes the embedded server,
 * and starts the web environment.
 *
 * Running this class will launch the application at:
 * http://localhost:8080/login
 */
@SpringBootApplication
public class NutriTrackApplication {
	public static void main(String[] args) {
		DatabaseManager.initializeDatabase();
		SpringApplication.run(NutriTrackApplication.class, args);
	}
}