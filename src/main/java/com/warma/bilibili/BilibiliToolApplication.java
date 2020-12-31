package com.warma.bilibili;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan
@EnableScheduling
public class BilibiliToolApplication {

    public static void main(String[] args) {
        SpringApplication.run(BilibiliToolApplication.class, args);

        //new BiLiBiLi().getDynamic();
        //System.out.println(new BiLiBiLiApi().getInfo(2).toString());
        //new BiLiBiLi().getDynamic();
    }
}
