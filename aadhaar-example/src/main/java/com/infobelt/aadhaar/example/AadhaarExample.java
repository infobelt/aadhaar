package com.infobelt.aadhaar.example;

import com.github.vanroy.springboot.autoconfigure.data.jest.ElasticsearchJestAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * A basic example application
 */
@EnableWebMvc
@SpringBootApplication(exclude = ElasticsearchJestAutoConfiguration.class)
public class AadhaarExample {

    public static void main(String[] args) {
        SpringApplication.run(AadhaarExample.class, args);
    }

}
