package com.warma.bilibili;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class BilibiliToolApplication {

    public static void main(String[] args) {
        SpringApplication.run(BilibiliToolApplication.class, args);
    }

}
