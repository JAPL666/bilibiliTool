package com.warma.bilibili;

import java.util.HashMap;

public class MyTest {
    public static void main(String[] args) {
        HashMap<String, String> list = BiLiBiLiApi.getDynamicIdList("401742377");
        for(String dyid:list.keySet()){
            System.out.println("UID："+list.get(dyid)+"  动态ID："+dyid);
        }
    }

}
