package com.warma.bilibili.utils;

import com.warma.bilibili.entity.BiLiBiLiEntity;
import com.warma.bilibili.entity.BiLiBiLiInfoEntity;
import com.warma.bilibili.entity.ResultEntity;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class BiLiBiLiApi {

    /**
     * 扫码登录
     * @return 登录成功后的数据
     */
    public ResultEntity login(){
        String loginUrl="https://passport.bilibili.com/qrcode/getLoginUrl";
        ResultEntity loginResult = Warma.get(loginUrl, new HashMap<>());

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
     * 获取可用的抽奖动态id
     * @param host_uid 用户uid
     * @return 可用的抽奖动态id List
     */
    public ArrayList<BiLiBiLiEntity> getDynamicIdList(String host_uid){
        ArrayList<BiLiBiLiEntity> list=new ArrayList<>();

        //获取下一页动态需要用到动态id
        String offset_dynamic_id="0";

        boolean bool=true;
        while (bool){
            String url="https://api.vc.bilibili.com/dynamic_svr/v1/dynamic_svr/space_history?visitor_uid=29204204&host_uid="+host_uid+"&offset_dynamic_id="+offset_dynamic_id+"&platform=web";
            ResultEntity result = Warma.get(url, new HashMap<>());

            String res = result.result;
            JSONObject json = new JSONObject(res);

            int has_more = json.getJSONObject("data").getInt("has_more");
            //判断动态的数据是否为空
            if(has_more!=0){
                JSONArray cardArray = json.getJSONObject("data").getJSONArray("cards");
                for (int i = 0; i < cardArray.length(); i++) {
                    JSONObject cards = cardArray.getJSONObject(i);

                    //动态的json对象
                    JSONObject cardObject = new JSONObject(cards.getString("card"));

                    //动态信息json对象
                    JSONObject desc = cards.getJSONObject("desc");

                    //动态的发布时间
                    long timestamp = desc.getLong("timestamp");

//                    SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                    String time = format.format(timestamp*1000);

                    //当前使劲按
                    long current = System.currentTimeMillis()/1000;
                    //当前时间减开奖时间
                    long x=current-timestamp;
                    long x1=((60*60)*24)*30;

                    //发布时间超过一个月则停止
                    if(x>x1){
                        bool=false;
                        break;
                    }


                    //跳过没有动态id的动态
                    if(!desc.toString().contains("dynamic_id_str")){
                        continue;
                    }

                    //本条动态的id
                    String dynamic_id= desc.getString("dynamic_id_str");
                    offset_dynamic_id=dynamic_id;

                    //储存动态id和uid的实体类
                    BiLiBiLiEntity biLiBiLiEntity=new BiLiBiLiEntity();

                    //解析过json转字符串
                    String card = cardObject.toString();

                    String dyid="";

                    if(card.contains("点击互动抽奖查看")){
                        //跳过开奖通知动态
                        continue;
                    }else if(card.contains("origin_extension")){
                        //别人转发的抽奖动态
                        dyid = String.valueOf(cardObject.getJSONObject("item").getLong("orig_dy_id"));

                        //动态id
                        biLiBiLiEntity.setDynamicId(dyid);
                        //发布动态的uid
                        biLiBiLiEntity.setHost_uid(String.valueOf(getSender_uid(dyid)));

                    }else if(card.contains("200b互动抽奖")){
                        //此用户发布的抽奖动态

                        dyid=dynamic_id;

                        //动态id
                        biLiBiLiEntity.setDynamicId(dynamic_id);
                        //发布动态的uid
                        biLiBiLiEntity.setHost_uid(host_uid);
                    }

                    //判断抽奖是否过期
                    if(isLottery(dyid)){
                        list.add(biLiBiLiEntity);
                    }

                }
            }else{
                //如果数据为空停止
                bool=false;
            }
        }
        return list;
    }

    /**
     * 获取已经开奖的动态id
     * @param host_uid 自己的uid
     * @return 已经开奖的动态id
     */
    public ArrayList<BiLiBiLiEntity> getExpiredDynamicIdList(String host_uid){
        ArrayList<BiLiBiLiEntity> list=new ArrayList<>();

        //获取下一页动态需要用到动态id
        String offset_dynamic_id="0";

        boolean bool=true;
        while (bool){
            String url="https://api.vc.bilibili.com/dynamic_svr/v1/dynamic_svr/space_history?visitor_uid=29204204&host_uid="+host_uid+"&offset_dynamic_id="+offset_dynamic_id+"&platform=web";
            ResultEntity result = Warma.get(url, new HashMap<>());

            String res = result.result;
            JSONObject json = new JSONObject(res);

            int has_more = json.getJSONObject("data").getInt("has_more");
            //判断动态的数据是否为空
            if(has_more!=0){
                JSONArray cardArray = json.getJSONObject("data").getJSONArray("cards");
                for (int i = 0; i < cardArray.length(); i++) {
                    JSONObject cards = cardArray.getJSONObject(i);

                    //动态的json对象
                    JSONObject cardObject = new JSONObject(cards.getString("card"));

                    //动态信息json对象
                    JSONObject desc = cards.getJSONObject("desc");

                    //动态的发布时间
                    long timestamp = desc.getLong("timestamp");

//                    SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                    String time = format.format(timestamp*1000);

                    //当前使劲按
                    long current = System.currentTimeMillis()/1000;
                    //当前时间减开奖时间
                    long x=current-timestamp;
                    long x1=((60*60)*24)*30;

                    //发布时间超过一个月则停止
                    if(x>x1){
                        bool=false;
                        break;
                    }


                    //跳过没有动态id的动态
                    if(!desc.toString().contains("dynamic_id_str")){
                        continue;
                    }

                    //本条动态的id
                    String dynamic_id= desc.getString("dynamic_id_str");
                    offset_dynamic_id=dynamic_id;

                    //储存动态id和uid的实体类
                    BiLiBiLiEntity biLiBiLiEntity=new BiLiBiLiEntity();

                    //解析过json转字符串
                    String card = cardObject.toString();

                    String dyid="";

                    if(card.contains("点击互动抽奖查看")){
                        //跳过开奖通知动态
                        continue;
                    }else if(card.contains("origin_extension")){
                        //别人转发的抽奖动态
                        dyid = String.valueOf(cardObject.getJSONObject("item").getLong("orig_dy_id"));

                        //自己转发的抽奖动态id
                        biLiBiLiEntity.setDynamicId(dynamic_id);
                        //发布动态的uid
                        biLiBiLiEntity.setHost_uid(String.valueOf(getSender_uid(dyid)));
                    }

                    //判断抽奖是否过期
                    if(!isLottery(dyid)){
                        list.add(biLiBiLiEntity);
                    }

                }
            }else{
                //如果数据为空停止
                bool=false;
            }
        }
        return list;
    }
    /**
     * 检查抽奖是否过期
     * @param dynamicId 动态id
     * @return 检测结果
     */
    public boolean isLottery(String dynamicId){
        String url="https://api.vc.bilibili.com/lottery_svr/v1/lottery_svr/lottery_notice?dynamic_id="+dynamicId;
        ResultEntity res = Warma.get(url, new HashMap<>());

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
     *
     * @param dynamicId 动态id
     * @return 获取发布动态的人的uid
     */
    public long getSender_uid(String dynamicId){
        String url="https://api.vc.bilibili.com/lottery_svr/v1/lottery_svr/lottery_notice?dynamic_id="+dynamicId;
        ResultEntity res = Warma.get(url, new HashMap<>());

        String result = res.result;
        if(!result.contains("-9999")){
            return new JSONObject(result).getJSONObject("data").getLong("sender_uid");
        }
        return 0;
    }

    /**
     * 查看是否关注
     * @param entity 登录返回的数据
     * @param mid 检测是否关注的uid
     */
    public boolean is_followed(BiLiBiLiInfoEntity entity,String mid){
        String url="https://api.bilibili.com/x/space/acc/info?mid="+mid+"&jsonp=jsonp";
        HashMap<String,String> requestProperty=new HashMap<>();
        requestProperty.put("Cookie",entity.getCookies());
        ResultEntity result = Warma.get(url, requestProperty);

        return new JSONObject(result.result).getJSONObject("data").getBoolean("is_followed");
    }

    //获取用户信息
    public BiLiBiLiInfoEntity getInfo(Integer uid){
        BiLiBiLiInfoEntity biLiBiLiInfoEntity = new BiLiBiLiInfoEntity();

        String url="https://api.bilibili.com/x/space/acc/info?mid="+uid+"&jsonp=jsonp";
        ResultEntity result = Warma.get(url, new HashMap<>());

        assert result.result !=null;

        //-412请求被拦截
        if(result.result.contains("-412")){

            biLiBiLiInfoEntity.setCode(-412);

        }else if(result.result.contains("-404")||result.result.contains("-400")){
            biLiBiLiInfoEntity.setCode(-404);
        }else{
            JSONObject data = new JSONObject(result.result).getJSONObject("data");

            biLiBiLiInfoEntity.setName(data.getString("name"));//名字
            biLiBiLiInfoEntity.setFace(data.getString("face"));//头像
            biLiBiLiInfoEntity.setLevel(data.getInt("level"));//等级
            biLiBiLiInfoEntity.setSex(data.getString("sex"));//性别
            biLiBiLiInfoEntity.setUid(uid);//uid
            biLiBiLiInfoEntity.setCode(0);//正常状态码
        }
        return biLiBiLiInfoEntity;
    }

    /**
     * 转发动态
     * @param entity 登录返回的数据
     * @param biLiBiLiEntity 哔哩哔哩实体类
     * @param str 转发内容
     */
    public void dynamic_repost(BiLiBiLiInfoEntity entity, BiLiBiLiEntity biLiBiLiEntity,String str){
        String url="https://api.vc.bilibili.com/dynamic_repost/v1/dynamic_repost/repost";

        JSONObject jsonObject = new JSONObject(entity.getCookieMap());
        String bili_jct = jsonObject.getString("bili_jct");

        String data="uid="+biLiBiLiEntity.getMyuid()+"&dynamic_id="+biLiBiLiEntity.getDynamicId()+"&content="+str+"&extension={\"emoji_type\":1}&at_uids=&ctrl=[]&csrf_token="+bili_jct+"&csrf="+bili_jct;

        HashMap<String,String> requestProperty=new HashMap<>();
        requestProperty.put("Cookie",entity.getCookies());
        requestProperty.put("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:83.0) Gecko/20100101 Firefox/83.0");

        Warma.post(url,data,requestProperty);
    }

    /**
     * 删除动态
     * @param entity 登录返回的数据
     * @param dynamic_id 动态id
     */
    public void rm_dynamic(BiLiBiLiInfoEntity entity, String dynamic_id){
        String url="https://api.vc.bilibili.com/dynamic_svr/v1/dynamic_svr/rm_dynamic";

        JSONObject jsonObject = new JSONObject(entity.getCookieMap());
        String bili_jct = jsonObject.getString("bili_jct");

        String data="dynamic_id="+dynamic_id+"&csrf_token="+bili_jct+"&csrf="+bili_jct;

        HashMap<String,String> requestProperty=new HashMap<>();
        requestProperty.put("Cookie",entity.getCookies());
        requestProperty.put("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:83.0) Gecko/20100101 Firefox/83.0");

        Warma.post(url,data,requestProperty);
    }

    /**
     * 关注和取关
     * @param entity 登录返回的数据
     * @param fid 要取关的uid
     * @param act 关注和取关 1关注 2取关
     */
    public void modify(BiLiBiLiInfoEntity entity, String fid,int act){
        String url="https://api.bilibili.com/x/relation/modify";

        System.out.println();
        JSONObject jsonObject = new JSONObject(entity.getCookieMap());
        String bili_jct = jsonObject.getString("bili_jct");

        String data="fid="+fid+"&act="+act+"&re_src=11&jsonp=jsonp&csrf="+bili_jct;

        HashMap<String,String> requestProperty=new HashMap<>();
        requestProperty.put("Cookie",entity.getCookies());
        requestProperty.put("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:83.0) Gecko/20100101 Firefox/83.0");

        Warma.post(url,data,requestProperty);
    }


}
