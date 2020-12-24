package com.warma.bilibili.app;

import com.warma.bilibili.entity.BiLiBiLiInfoEntity;
import com.warma.bilibili.service.impl.BiLiBiLiServiceImpl;
import com.warma.bilibili.utils.BiLiBiLiApi;
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
        BiLiBiLiApi biLiBiLiApi = new BiLiBiLiApi();
        List<BiLiBiLiInfoEntity> userInfo = getDynamic.service.findUserInfo();
        if(userInfo!=null){
            for (BiLiBiLiInfoEntity biLiBiLiInfoEntity : userInfo) {
                System.out.println(biLiBiLiInfoEntity.getCookies());
                System.out.println(biLiBiLiInfoEntity.getName());

                boolean followed = biLiBiLiApi.is_followed(biLiBiLiInfoEntity, "53456");
                if(followed){
                    System.out.println("已经关注了");
                }else{
                    System.out.println("没有关注");
                }

            }
        }
    }
}
