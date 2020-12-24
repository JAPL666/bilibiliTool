package com.warma.bilibili.app;

import com.warma.bilibili.entity.BiLiBiLiEntity;
import com.warma.bilibili.entity.BiLiBiLiInfoEntity;
import com.warma.bilibili.entity.DynamicidAndUid;
import com.warma.bilibili.service.impl.BiLiBiLiServiceImpl;
import com.warma.bilibili.utils.BiLiBiLiApi;
import com.warma.bilibili.utils.Warma;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
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
            //遍历多个用户
            for (BiLiBiLiInfoEntity biLiBiLiInfoEntity : userInfo) {

                //循环次数
                for (int i = 0; i < 100; i++) {

                    //随机生成UID
                    int randomUid = Warma.Random(2, 9000000);
                    //通过UID获取用户信息
                    BiLiBiLiInfoEntity info = biLiBiLiApi.getInfo(randomUid);
                    if(info.getCode()==-412){
                        //请求繁忙，请求被拦截
                        System.out.println("请求被拦截>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                    }else if(info.getCode() == 0){

                        //等级大于0才执行
                        if(info.getLevel()>0){

                            ArrayList<BiLiBiLiEntity> dynamicIdList = biLiBiLiApi.getDynamicIdList(String.valueOf(info.getUid()));
                            if(dynamicIdList.size()!=0){

                                for (BiLiBiLiEntity biLiBiLiEntity : dynamicIdList) {
                                    System.out.println("动态ID："+biLiBiLiEntity.getDynamicId());
                                    //获取关注状态
                                    boolean followed = biLiBiLiApi.is_followed(biLiBiLiInfoEntity, String.valueOf(info.getUid()));
                                    //如果没有关注
                                    if(!followed){
                                        //关注
                                        biLiBiLiApi.modify(biLiBiLiInfoEntity,String.valueOf(biLiBiLiEntity.getHost_uid()),1);
                                    }
                                    BiLiBiLiEntity entity = new BiLiBiLiEntity();
                                    //要转发动态的ID
                                    entity.setDynamicId(biLiBiLiEntity.getDynamicId());
                                    //自己的UID
                                    entity.setMyuid(String.valueOf(biLiBiLiInfoEntity.getUid()));
                                    //转发抽奖动态
                                    biLiBiLiApi.dynamic_repost(biLiBiLiInfoEntity,entity,"我收下了、、、、、");

                                    DynamicidAndUid dynamicidAndUid = new DynamicidAndUid();
                                    //抽奖动态ID
                                    dynamicidAndUid.setDynamicId(biLiBiLiEntity.getDynamicId());
                                    //发动态的人的UID
                                    dynamicidAndUid.setUid(biLiBiLiEntity.getHost_uid());
                                    //动态ID和UID插入数据库
                                    biLiBiLi.service.insertDynamicidAndUid(dynamicidAndUid);

                                }
                                System.out.println("\n");
                            }

                        }
                    }

                    try {
                        Thread.sleep(200);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
