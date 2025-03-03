package com.example.breken30daysback;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Breken30daysbackApplication {

    public static void main(String[] args) {
        SpringApplication.run(Breken30daysbackApplication.class, args);
    }

}
