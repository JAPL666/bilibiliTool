package com.warma.bilibili;

import com.warma.bilibili.utils.Warma;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class MyTest {
    public static void main(String[] args) {

        String host_uid="401742377";
        String offset_dynamic_id="0";

        boolean bool=true;
        while (bool){
            String url="https://api.vc.bilibili.com/dynamic_svr/v1/dynamic_svr/space_history?visitor_uid=29204204&host_uid="+host_uid+"&offset_dynamic_id="+offset_dynamic_id+"&platform=web";
            HashMap<String, String> result = Warma.get(url, new HashMap<>());

            assert result != null;
            String res = result.get(Warma.RESULT);
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
                            String dynamic_id= desc.getString("dynamic_id_str");
                            offset_dynamic_id=dynamic_id;

                            if(card.contains("orig_dy_id")){
                                //别人转发的抽奖动态

                                //源动态ID
                                String[] orig_dy_id = Warma.regex("orig_dy_id\":([^\"]+),", card).split("\n");

                                if(orig_dy_id[0].trim().equals("0")){
                                    System.out.println("xxx源动态ID："+dynamic_id);
                                }else{
                                    System.out.println("源动态ID："+orig_dy_id[0].trim());
                                }

                                //源UID
                                String[] uids = Warma.regex("\"uid\":([^\"]+),", card).split("\n");
                                System.out.println("源UID:"+uids[uids.length-1].trim());

                            }else{
                                //抽奖动态
                                long uid= desc.getLong("uid");
                                System.out.println("UID:"+uid);
                                System.out.println("动态ID："+dynamic_id);
                            }
                        }
                    }
                }
            }
        }
    }
}
