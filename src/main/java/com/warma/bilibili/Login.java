package com.warma.bilibili;

import com.warma.bilibili.entity.ResultEntity;
import com.warma.bilibili.utils.QRCode;
import com.warma.bilibili.utils.Warma;
import org.json.JSONObject;

import java.util.HashMap;

public class Login {
    public static void main(String[] args) {

    }
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
}
