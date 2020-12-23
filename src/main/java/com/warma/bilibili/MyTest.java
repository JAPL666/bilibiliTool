package com.warma.bilibili;

import com.warma.bilibili.entity.ResultEntity;
import com.warma.bilibili.utils.BiLiBiLiApi;
import com.warma.bilibili.utils.QRCode;

import java.util.HashMap;

public class MyTest {
    public static void main(String[] args) {
        BiLiBiLiApi biLiBiLiApi = new BiLiBiLiApi();
//        String[] uids=new String[]{"401742377"};
//        HashMap<String, String> allList=new HashMap<>();
//

//        for (String uid:uids){
//            HashMap<String, String> list = biLiBiLiApi.getDynamicIdList(uid);
//            allList.putAll(list);
//        }
//
//        for(String dyid:allList.keySet()){
//            System.out.println("UID："+allList.get(dyid)+"  动态ID："+dyid);
//        }

        //BILIBILI扫码登录
 //       ResultEntity login = biLiBiLiApi.login();
        //检测是否关注
//        System.out.println(biLiBiLiApi.is_followed(login,"15547141")?"已经关注！":"没有关注！");

        //-----------------------------------------------------------------------------------------------------------------//
        //获取已过期的抽奖动态id
//        HashMap<String, String> expiredDynamicIdList = biLiBiLiApi.getExpiredDynamicIdList("281120836");
//        for (String dynamicId:expiredDynamicIdList.keySet()){
//            //检测是否中奖
//            if(biLiBiLiApi.isWinning(dynamicId,"281120836")){
//                //中奖
//                System.out.println("恭喜中奖了！");
//                System.out.println("动态id："+dynamicId+"  uid："+expiredDynamicIdList.get(dynamicId));
//            }else{
//                System.out.println(dynamicId);
//                //没中奖
//                //ResultEntity resultEntity = new ResultEntity();
//                //删除没中奖的动态
//                biLiBiLiApi.rm_dynamic(login,dynamicId);
//            }
//        }
        //-----------------------------------------------------------------------------------------------------------------//

//        String base64 = QRCode.createQRCodeImageBase64("https://baidu.com", 180, 180);
//        System.out.println(base64);

        biLiBiLiApi.getExpiredDynamicIdList("401742377");

    }
}
