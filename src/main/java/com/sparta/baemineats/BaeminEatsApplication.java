package com.sparta.baemineats;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableWebSecurity
public class BaeminEatsApplication {

    public static void main(String[] args) {
        SpringApplication.run(BaeminEatsApplication.class, args);
    }

}
