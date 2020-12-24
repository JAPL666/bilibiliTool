package com.warma.bilibili.app;

import com.warma.bilibili.entity.BiLiBiLiInfoEntity;
import com.warma.bilibili.service.impl.BiLiBiLiServiceImpl;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

@Component
public class GetDynamic {

    @Resource(name="biLiBiLiServiceImpl")
    BiLiBiLiServiceImpl service;

    public static GetDynamic getDynamic;

    @PostConstruct
    public void init(){
        getDynamic=this;
    }
    public void start(){
        List<BiLiBiLiInfoEntity> userInfo = getDynamic.service.findUserInfo();
        if(userInfo!=null){
            for (BiLiBiLiInfoEntity biLiBiLiInfoEntity : userInfo) {
                System.out.println(biLiBiLiInfoEntity.getCookies());
                System.out.println(biLiBiLiInfoEntity.getName());
            }
        }
    }
}
