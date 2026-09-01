package com.shortly;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class UrlShortnerApplication {
    public static void main(String[] args) {
        System.out.println("DEBUG - SYSTEM ENV URL: " + System.getenv("SPRING_DATASOURCE_URL"));
        SpringApplication.run(UrlShortnerApplication.class, args);
    }
}
