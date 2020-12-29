package com.warma.bilibili.app;

import com.warma.bilibili.entity.*;
import com.warma.bilibili.service.impl.BiLiBiLiServiceImpl;
import com.warma.bilibili.utils.BiLiBiLiApi;
import com.warma.bilibili.utils.Mail;
import com.warma.bilibili.utils.Warma;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
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
        List<String> allComment = biLiBiLi.service.findAllComment();

        BiLiBiLiApi biLiBiLiApi = new BiLiBiLiApi();
        List<BiLiBiLiInfoEntity> userInfo = biLiBiLi.service.findUserInfo();

        if(userInfo!=null){
            //遍历多个用户
            for (BiLiBiLiInfoEntity biLiBiLiInfoEntity : userInfo) {

                //循环次数
                for (int i = 0; i < 1000; i++) {

                    //随机生成UID
                    int randomUid = Warma.Random(2, 9000000);
                    //通过UID获取用户信息
                    BiLiBiLiInfoEntity info = biLiBiLiApi.getInfo(randomUid);
                    if(info.getCode()==-412){
                        //请求繁忙，请求被拦截
                        System.out.println("请求被拦截>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                        break;
                    }else if(info.getCode() == 0){

                        //等级大于0才执行
                        if(info.getLevel()>0){

                            //获取用户转发或者自己发的抽奖动态
                            ArrayList<BiLiBiLiEntity> dynamicIdList = biLiBiLiApi.getDynamicIdList(String.valueOf(info.getUid()));
                            if(dynamicIdList.size()!=0){

                                for (BiLiBiLiEntity biLiBiLiEntity : dynamicIdList) {

                                    DynamicidAndUid dynamicidAndUid = new DynamicidAndUid();
                                    //抽奖动态ID
                                    dynamicidAndUid.setDynamicId(biLiBiLiEntity.getDynamicId());
                                    //发动态的人的UID
                                    dynamicidAndUid.setUid(biLiBiLiEntity.getHost_uid());
                                    //动态ID和UID插入数据库
                                    int result = biLiBiLi.service.insertDynamicidAndUid(dynamicidAndUid);
                                    if(result>0){
                                        System.out.print("动态ID："+biLiBiLiEntity.getDynamicId());
                                        System.out.println("  UID："+biLiBiLiEntity.getHost_uid());


                                        String comment;
                                        if(allComment.size()>0){
                                            int random = Warma.Random(0, allComment.size());
                                            //从数据库中随机获取评论
                                            comment = allComment.get(random);
                                        }else{
                                            comment="礼物我收下了！！！！";
                                        }

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
                                        biLiBiLiApi.dynamic_repost(biLiBiLiInfoEntity,entity,comment);

                                    }

                                }
                            }

                        }
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
    //删除过期动态并检测是否中奖
    public void expiredDynamicDelete(){
        BiLiBiLiApi biLiBiLiApi = new BiLiBiLiApi();
        List<BiLiBiLiInfoEntity> userInfo = biLiBiLi.service.findUserInfo();
        for (BiLiBiLiInfoEntity biLiBiLiInfoEntity : userInfo) {
            //我的uid
            String myUid=String.valueOf(biLiBiLiInfoEntity.getUid());

            //获取自己转发的过期抽奖动态
            ArrayList<BiLiBiLiEntity> list = biLiBiLiApi.getExpiredDynamicIdList(myUid);
            for (BiLiBiLiEntity biLiBiLiEntity : list) {
                //动态id
                String dynamicId = biLiBiLiEntity.getDynamicId();

                if(biLiBiLiApi.isWinning(dynamicId,myUid)){
                    //中奖了
                    System.out.println("中奖了:"+dynamicId);
                    Mail.sendEmail("2453885428@qq.com","哔哩哔哩动态抽奖中奖了！！","动态ID："+dynamicId+"\nUID:"+biLiBiLiEntity.getHost_uid());
                }else{
                    //删除过期动态
                    biLiBiLiApi.rm_dynamic(biLiBiLiInfoEntity,dynamicId);
                    System.out.println("已经删除："+dynamicId+"  UID:"+biLiBiLiEntity.getHost_uid());
                }

                try {
                    Thread.sleep(2000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    //取关没有抽奖动态的用户 慎重使用 会取关自己关注的up
    public void modify(){
        BiLiBiLiApi biLiBiLiApi = new BiLiBiLiApi();
        List<BiLiBiLiInfoEntity> userInfo = biLiBiLi.service.findUserInfo();
        for (BiLiBiLiInfoEntity biLiBiLiInfoEntity : userInfo) {
            String cookies = biLiBiLiInfoEntity.getCookies();
            String url="https://api.vc.bilibili.com/feed/v1/feed/get_attention_list?uid="+biLiBiLiInfoEntity.getUid();
            HashMap<String, String> map = new HashMap<>();
            map.put("Cookie",cookies);
            ResultEntity resultEntity = Warma.get(url, map);

            String result = resultEntity.result;
            if(!result.contains("success")){
                continue;
            }
            JSONObject json = new JSONObject(result);

            JSONArray jsonArray = json.getJSONObject("data").getJSONArray("list");
            for (Object uid : jsonArray) {
                //获取用户转发的可用抽奖数量
                ArrayList<BiLiBiLiEntity> dynamicIdList = biLiBiLiApi.getDynamicIdList(uid.toString());
                //如果转发的可用动态小于零则取关
                if(0<dynamicIdList.size()){
                    //取关
                    biLiBiLiApi.modify(biLiBiLiInfoEntity,String.valueOf(uid.toString()),2);
                }
            }
        }
    }
}
