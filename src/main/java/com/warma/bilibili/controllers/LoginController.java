package com.warma.bilibili.controllers;

import com.warma.bilibili.entity.BiLiBiLiInfoEntity;
import com.warma.bilibili.entity.ResultEntity;
import com.warma.bilibili.utils.BiLiBiLiApi;
import com.warma.bilibili.utils.Warma;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@RestController
public class LoginController {
    @RequestMapping("checkLogin")
    public Object checkLogin(HttpServletRequest request){
        BiLiBiLiInfoEntity entity=new BiLiBiLiInfoEntity();
        String oauthKey = request.getSession().getAttribute("oauthKey").toString();

        String loginInfoUrl="https://passport.bilibili.com/qrcode/getLoginInfo";
        ResultEntity loginInfoResult = Warma.post(loginInfoUrl, "oauthKey=" + oauthKey, new HashMap<>());

        assert loginInfoResult != null;
        String res = loginInfoResult.result;
        String cookies = loginInfoResult.cookies;

        JSONObject jsonObject=new JSONObject(loginInfoResult.result);
        boolean status = jsonObject.getBoolean("status");

        if(status){
            //登录成功
            int uid = Integer.parseInt(res.substring(res.indexOf("DedeUserID=")).split("&")[0].replace("DedeUserID=",""));
            entity=new BiLiBiLiApi().getInfo(uid);
            //访问图片URL，把图片转base64
            entity.setFace(Warma.getImageBase64(entity.getFace()));
            entity.setCode(0);
        }else if(jsonObject.getInt("data")==-2){
            //已经登录过了
            entity.setCode(1);
        }else{
            //请扫描二维码
            entity.setCode(2);
        }
        return entity;
    }
}
