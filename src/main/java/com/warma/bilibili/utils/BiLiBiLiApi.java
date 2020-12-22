package com.warma.bilibili.utils;

import com.warma.bilibili.entity.BiLiBiLiEntity;
import com.warma.bilibili.entity.BiLiBiLiInfoEntity;
import com.warma.bilibili.entity.ResultEntity;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class BiLiBiLiApi {

    /**
     * 扫码登录
     * @return 登录成功后的数据
     */
    public ResultEntity login(){
        String loginUrl="https://passport.bilibili.com/qrcode/getLoginUrl";
        ResultEntity loginResult = Warma.get(loginUrl, new HashMap<>());

        assert loginResult != null;
        JSONObject json = new JSONObject(loginResult.getResult());
        JSONObject data = json.getJSONObject("data");
        String url = data.getString("url");
        String oauthKey = data.getString("oauthKey");
        try {
            String path=new File("").getCanonicalPath()+"\\bilibiliLogin.png";
            QRCode.createQRCodeImage(url,180,180,path);
        } catch (IOException e) {
            e.printStackTrace();
        }

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
    /**
     * 获取有效的抽奖动态id
     * @param host_uid 要获取动态id的uid
     * @return 抽奖动态id
     */
    public HashMap<String,String> getDynamicIdList(String host_uid){
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

    /**
     * 获取已经开奖的动态id
     * @param host_uid 自己的uid
     * @return 已经开奖的动态id
     */
    public HashMap<String,String> getExpiredDynamicIdList(String host_uid){
        HashMap<String,String> map=new HashMap<>();

        String offset_dynamic_id="0";

        boolean bool=true;
        while (bool){
            String url="https://api.vc.bilibili.com/dynamic_svr/v1/dynamic_svr/space_history?visitor_uid=29204204&host_uid="+host_uid+"&offset_dynamic_id="+offset_dynamic_id+"&platform=web";
            ResultEntity result = Warma.get(url, new HashMap<>());

            assert result != null;
            String res = result.result;
            JSONObject json = new JSONObject(res);

            //如果数据为空停止
            if(json.getJSONObject("data").getInt("has_more")==1){
                bool=false;
            }

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
                        //动态ID
                        String dynamic_id= desc.getString("dynamic_id_str").trim();
                        offset_dynamic_id=dynamic_id;

                        if(card.contains("orig_dy_id")){

                            //源UID
                            String[] uids = Warma.regex("\"uid\":([^\"]+),", card).split("\n");
                            String uid=uids[uids.length-1].trim();

                            //源动态ID
                            String[] orig_dy_id = Warma.regex("orig_dy_id\":([^\"]+),", card).split("\n");
                            if(!orig_dy_id[0].trim().equals("0")){
                                dynamic_id=orig_dy_id[0].trim();
                            }

                            //如果抽奖过期
                            if(!isLottery(dynamic_id)){
                                map.put(dynamic_id,uid);
                            }
                        }else{
                            //如果抽奖过期
                            if(isLottery(dynamic_id)){
                                map.put(dynamic_id,host_uid);
                            }
                        }
                    }
                }
            }
        }
        return map;
    }
    /**
     * 检查抽奖是否过期
     * @param dynamicId 动态id
     * @return 检测结果
     */
    public boolean isLottery(String dynamicId){
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

    /**
     * 检测是否中奖
     * @param dynamicId 动态id
     * @param uid 自己的id
     * @return 检测是否中奖
     */
    public boolean isWinning(String dynamicId,String uid){
        String url="https://api.vc.bilibili.com/lottery_svr/v1/lottery_svr/lottery_notice?dynamic_id="+dynamicId;
        ResultEntity res = Warma.get(url, new HashMap<>());

        assert res != null;
        String result = res.result;

        //检查是否中奖
        return result.contains("lottery_result")&&result.contains(uid);
    }

    /**
     * 查看是否关注
     * @param resultEntity 登录返回的数据
     * @param mid 检测是否关注的uid
     */
    public boolean is_followed(ResultEntity resultEntity,String mid){
        String url="https://api.bilibili.com/x/space/acc/info?mid="+mid+"&jsonp=jsonp";
        HashMap<String,String> requestProperty=new HashMap<>();
        requestProperty.put("Cookie",resultEntity.getCookies());
        ResultEntity result = Warma.get(url, requestProperty);

        assert result != null;
        return new JSONObject(result.result).getJSONObject("data").getBoolean("is_followed");
    }

    //获取用户信息
    public BiLiBiLiInfoEntity getInfo(Integer uid){
        BiLiBiLiInfoEntity biLiBiLiInfoEntity = new BiLiBiLiInfoEntity();

        String url="https://api.bilibili.com/x/space/acc/info?mid="+uid+"&jsonp=jsonp";
        ResultEntity result = Warma.get(url, new HashMap<>());

        assert result != null;
        if(!result.result.contains("-404")||!result.result.contains("-400")){
            JSONObject data = new JSONObject(result.result).getJSONObject("data");

            biLiBiLiInfoEntity.setName(data.getString("name"));//名字
            biLiBiLiInfoEntity.setFace(data.getString("face"));//头像
            biLiBiLiInfoEntity.setLevel(data.getInt("level"));//等级
            biLiBiLiInfoEntity.setSex(data.getString("sex"));//性别
            biLiBiLiInfoEntity.setUid(uid);//uid

        }
        return biLiBiLiInfoEntity;
    }

    /**
     * 转发动态
     * @param resultEntity 登录返回的数据
     * @param biLiBiLiEntity 哔哩哔哩实体类
     * @param str 转发内容
     */
    public void dynamic_repost(ResultEntity resultEntity, BiLiBiLiEntity biLiBiLiEntity,String str){
        String url="https://api.vc.bilibili.com/dynamic_repost/v1/dynamic_repost/repost";

        String bili_jct = resultEntity.getCookieMap().get("bili_jct");
        String data="uid="+biLiBiLiEntity.getMyuid()+"&dynamic_id="+biLiBiLiEntity.getDynamicId()+"&content="+str+"&extension={\"emoji_type\":1}&at_uids=&ctrl=[]&csrf_token="+bili_jct+"&csrf="+bili_jct;

        HashMap<String,String> requestProperty=new HashMap<>();
        requestProperty.put("Cookie",resultEntity.getCookies());
        requestProperty.put("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:83.0) Gecko/20100101 Firefox/83.0");

        Warma.post(url,data,requestProperty);
    }

    /**
     * 删除动态
     * @param resultEntity 登录返回的数据
     * @param dynamic_id 动态id
     */
    public void rm_dynamic(ResultEntity resultEntity, String dynamic_id){
        String url="https://api.vc.bilibili.com/dynamic_svr/v1/dynamic_svr/rm_dynamic";

        String bili_jct = resultEntity.getCookieMap().get("bili_jct");
        String data="dynamic_id="+dynamic_id+"&csrf_token="+bili_jct+"&csrf="+bili_jct;

        HashMap<String,String> requestProperty=new HashMap<>();
        requestProperty.put("Cookie",resultEntity.getCookies());
        requestProperty.put("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:83.0) Gecko/20100101 Firefox/83.0");

        Warma.post(url,data,requestProperty);
    }

    /**
     * 关注和取关
     * @param resultEntity 登录返回的数据
     * @param fid 要取关的uid
     * @param act 关注和取关 1关注 2取关
     */
    public void modify(ResultEntity resultEntity, String fid,int act){
        String url="https://api.bilibili.com/x/relation/modify";

        String bili_jct = resultEntity.getCookieMap().get("bili_jct");

        String data="fid="+fid+"&act="+act+"&re_src=11&jsonp=jsonp&csrf="+bili_jct;

        HashMap<String,String> requestProperty=new HashMap<>();
        requestProperty.put("Cookie",resultEntity.getCookies());
        requestProperty.put("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:83.0) Gecko/20100101 Firefox/83.0");

        Warma.post(url,data,requestProperty);
    }


}
