package com.warma.bilibili;

import com.warma.bilibili.app.BiLiBiLi;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Scheduler {

    //定时获取抽奖动态并转发
    @Scheduled(cron = "0 0 * * * ? ")
    public void getDynamic(){
        BiLiBiLi.getDynamic();
    }

    //删除开奖动态
    @Scheduled(cron = "0 0 0 * * ? *")
    public void expiredDynamicDelete(){
        BiLiBiLi.expiredDynamicDelete();
    }
}
