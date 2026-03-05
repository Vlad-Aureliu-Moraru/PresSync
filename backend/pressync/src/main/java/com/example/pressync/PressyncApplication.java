package com.example.pressync;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PressyncApplication {

	public static void main(String[] args) {
		SpringApplication.run(PressyncApplication.class, args);
			System.out.println("Pressync application started");
	}

}
