package com.warma.bilibili;

import com.warma.bilibili.utils.BiLiBiLiApi;
import com.warma.bilibili.utils.QRCode;

public class MyTest {
    public static void main(String[] args) {
        BiLiBiLiApi biLiBiLiApi = new BiLiBiLiApi();


        //BILIBILI扫码登录
        //ResultEntity login = biLiBiLiApi.login();

        //获取自己转发的过期抽奖动态
//        ArrayList<BiLiBiLiEntity> list = biLiBiLiApi.getExpiredDynamicIdList("281120836");
//        for (BiLiBiLiEntity biLiBiLiEntity : list) {
//            System.out.println(biLiBiLiEntity.getDynamicId());
//            System.out.println(biLiBiLiEntity.getHost_uid());
//        }

        QRCode.createConsoleQRCode("http://warma.fun");

    }
}
