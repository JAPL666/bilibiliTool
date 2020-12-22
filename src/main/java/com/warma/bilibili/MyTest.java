package com.warma.bilibili;

import com.warma.bilibili.entity.ResultEntity;
import com.warma.bilibili.utils.BiLiBiLiApi;

import java.util.HashMap;

public class MyTest {
    public static void main(String[] args) {

        String[] uids=new String[]{"401742377"};
        HashMap<String, String> allList=new HashMap<>();

        BiLiBiLiApi biLiBiLiApi = new BiLiBiLiApi();
        for (String uid:uids){
            HashMap<String, String> list = biLiBiLiApi.getDynamicIdList(uid);
            allList.putAll(list);
        }

        for(String dyid:allList.keySet()){
            System.out.println("UID："+allList.get(dyid)+"  动态ID："+dyid);
        }

//        ResultEntity login = biLiBiLiApi.login();
//        System.out.println(biLiBiLiApi.is_followed(login,"15547141")?"已经关注！":"没有关注！");

    }
}
