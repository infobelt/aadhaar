package com.infobelt.aadhaar.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * A basic example application
 */
@SpringBootApplication
@EnableWebMvc
public class AadhaarExample {

    public static void main(String[] args) {
        SpringApplication.run(AadhaarExample.class, args);
    }

}
