package com.warma.bilibili;

import com.warma.bilibili.entity.ResultEntity;
import com.warma.bilibili.utils.QRCode;
import com.warma.bilibili.utils.Warma;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class BiLiBiLiApi {
    public static void main(String[] args) {

    }
    //扫码登录
    public static ResultEntity login(){
        String loginUrl="https://passport.bilibili.com/qrcode/getLoginUrl";
        ResultEntity loginResult = Warma.get(loginUrl, new HashMap<>());

        assert loginResult != null;
        JSONObject json = new JSONObject(loginResult.getResult());
        JSONObject data = json.getJSONObject("data");
        String url = data.getString("url");
        String oauthKey = data.getString("oauthKey");

        System.out.println(url);
        String path="C:\\Users\\86176\\Desktop\\bilibiliLogin.png";
        QRCode.createQRCodeImage(url,180,180,path);

        while(true){
            String loginInfoUrl="https://passport.bilibili.com/qrcode/getLoginInfo";
            ResultEntity loginInfoResult = Warma.post(loginInfoUrl, "oauthKey=" + oauthKey, new HashMap<>());

            assert loginInfoResult != null;
            String res = loginInfoResult.result;
            String cookies = loginInfoResult.cookies;

            JSONObject jsonObject=new JSONObject(loginInfoResult.result);
            boolean status = jsonObject.getBoolean("status");

            if(status){
                System.out.println("扫码登录成功！");
                System.out.println(res);
                System.out.println(cookies);

                return loginInfoResult;
            }else{
                System.out.println("请扫描二维码！");
            }
            try {
                Thread.sleep(2000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    //获取有效的抽奖动态id
    public static HashMap<String,String> getDynamicIdList(String host_uid){
        HashMap<String,String> map=new HashMap<>();

        String offset_dynamic_id="0";

        boolean bool=true;
        while (bool){
            String url="https://api.vc.bilibili.com/dynamic_svr/v1/dynamic_svr/space_history?visitor_uid=29204204&host_uid="+host_uid+"&offset_dynamic_id="+offset_dynamic_id+"&platform=web";
            ResultEntity result = Warma.get(url, new HashMap<>());

            assert result != null;
            String res = result.result;
            JSONObject json = new JSONObject(res);

            int has_more = json.getJSONObject("data").getInt("has_more");
            if(has_more!=0){
                JSONArray cardArray = json.getJSONObject("data").getJSONArray("cards");
                for (int i = 0; i < cardArray.length(); i++) {
                    JSONObject cards = cardArray.getJSONObject(i);
                    String card = cards.getString("card");
                    card=Warma.unicodeDecode(card).replace("\\","");

                    if(!card.contains("互动抽奖")){
                        continue;
                    }

                    JSONObject desc = cards.getJSONObject("desc");

                    if(desc.toString().contains("dynamic_id_str")){
                        //动态的时间戳
                        long timestamp = desc.getLong("timestamp");
                        long time = System.currentTimeMillis()/1000;
                        long x=time-timestamp;
                        long x1=((60*60)*24)*30;
                        if(x>x1){
                            //System.out.println("时间超过一个月");
                            bool=false;
                        }else{
                            //动态ID
                            String dynamic_id= desc.getString("dynamic_id_str").trim();
                            offset_dynamic_id=dynamic_id;

                            if(card.contains("orig_dy_id")){
                                //别人转发的抽奖动态

                                //源UID
                                String[] uids = Warma.regex("\"uid\":([^\"]+),", card).split("\n");
                                String uid=uids[uids.length-1].trim();

                                //源动态ID
                                String[] orig_dy_id = Warma.regex("orig_dy_id\":([^\"]+),", card).split("\n");
                                if(!orig_dy_id[0].trim().equals("0")){
                                    dynamic_id=orig_dy_id[0].trim();
                                }

                                //如果抽奖没过期
                                if(isLottery(dynamic_id)){
                                    map.put(dynamic_id,uid);
                                }

                            }else{
                                //抽奖动态

                                //如果抽奖没过期
                                if(isLottery(dynamic_id)){
                                    map.put(dynamic_id,host_uid);
                                }
                            }
                        }
                    }
                }
            }
        }
        return map;
    }
    //检查抽奖是否过期
    public static boolean isLottery(String dynamicId){
        String url="https://api.vc.bilibili.com/lottery_svr/v1/lottery_svr/lottery_notice?dynamic_id="+dynamicId;
        ResultEntity res = Warma.get(url, new HashMap<>());

        assert res != null;
        String result = res.result;
        if(result.contains("-9999")){
            return false;
        }else{
            return !result.contains("lottery_result");
        }
    }
}
