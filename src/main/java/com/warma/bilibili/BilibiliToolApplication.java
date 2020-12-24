package com.warma.bilibili;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@MapperScan
//@EnableCaching
@SpringBootApplication
public class BilibiliToolApplication {

    public static void main(String[] args) {
        SpringApplication.run(BilibiliToolApplication.class, args);
    }

}
