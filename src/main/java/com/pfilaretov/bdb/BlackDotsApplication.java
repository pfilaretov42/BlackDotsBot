package com.pfilaretov.bdb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.ApiContextInitializer;

/**
 * Main spring boot application class
 */
@SpringBootApplication
public class BlackDotsApplication {

    public static void main(String[] args) {
        ApiContextInitializer.init();
        SpringApplication.run(BlackDotsApplication.class, args);
    }
}
