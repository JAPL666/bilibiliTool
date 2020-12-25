package com.warma.bilibili;

import com.warma.bilibili.app.BiLiBiLi;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Scheduler {

    //定时获取抽奖动态并转发
    @Scheduled(cron = "0 0 1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23 * * ? ")
    public void getDynamic(){
        new BiLiBiLi().getDynamic();
    }

    //删除开奖动态
    @Scheduled(cron = "0 0 0 * * ? *")
    public void expiredDynamicDelete(){
        new BiLiBiLi().expiredDynamicDelete();
    }
}
