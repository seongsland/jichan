package com.jichan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class JichanApplication {

    public static void main(String[] args) {
        SpringApplication.run(JichanApplication.class, args);
        System.out.println("JichanApplication START!");
    }

}
