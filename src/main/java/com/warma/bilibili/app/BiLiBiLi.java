package com.warma.bilibili.app;

import com.warma.bilibili.entity.BiLiBiLiInfoEntity;
import com.warma.bilibili.service.impl.BiLiBiLiServiceImpl;
import com.warma.bilibili.utils.BiLiBiLiApi;
import com.warma.bilibili.utils.Warma;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

@Component
public class BiLiBiLi {

    @Resource(name="biLiBiLiServiceImpl")
    BiLiBiLiServiceImpl service;

    public static BiLiBiLi biLiBiLi;

    @PostConstruct
    public void init(){
        biLiBiLi =this;
    }
    public void getDynamic(){
        BiLiBiLiApi biLiBiLiApi = new BiLiBiLiApi();
        List<BiLiBiLiInfoEntity> userInfo = biLiBiLi.service.findUserInfo();
        if(userInfo!=null){
            for (BiLiBiLiInfoEntity biLiBiLiInfoEntity : userInfo) {

                for (int i = 0; i < 1000; i++) {

                    int randomUid = Warma.Random(2, 900000000);
                    BiLiBiLiInfoEntity info = biLiBiLiApi.getInfo(randomUid);
                    if(info.getCode()==-412){
                        System.out.println("请求被拦截>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                    }else if(info.getCode() == 0){
                        System.out.println("名字："+info.getName());
                        System.out.println("UID："+info.getUid());
                        System.out.println("等级："+info.getLevel());
                        System.out.println("性别："+info.getSex());
                        System.out.println("头像："+info.getFace());
                        System.out.println("\n");
                    }

                    try {
                        Thread.sleep(2000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
