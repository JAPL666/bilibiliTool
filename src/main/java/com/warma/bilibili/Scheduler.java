package com.warma.bilibili;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Scheduler {

    @Scheduled(cron = "")
    public void getdyIdList(){

    }
}
