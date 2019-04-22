package com.infobelt.aadhaar.example;

import com.github.vanroy.springboot.autoconfigure.data.jest.ElasticsearchJestAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * A basic example application
 */
@EnableSwagger2
@EnableWebMvc
@SpringBootApplication(exclude = ElasticsearchJestAutoConfiguration.class)
public class AadhaarExample {

    public static void main(String[] args) {
        SpringApplication.run(AadhaarExample.class, args);
    }

}
