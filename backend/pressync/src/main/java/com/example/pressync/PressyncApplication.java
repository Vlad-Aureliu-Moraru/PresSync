package com.example.pressync;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableAsync
@EnableCaching
public class PressyncApplication {
    private static final Logger log = LoggerFactory.getLogger(PressyncApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(PressyncApplication.class, args);
        log.info("Pressync application started");
    }
}
