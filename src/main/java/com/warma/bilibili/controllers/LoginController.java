package com.warma.bilibili.controllers;

import com.warma.bilibili.entity.ResultEntity;
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
        String oauthKey = request.getSession().getAttribute("oauthKey").toString();

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

            return 0;
        }else if(jsonObject.getInt("data")==-2){
            System.out.println("已经登录了！");
            return 1;
        }else{
            System.out.println(res);
            System.out.println("请扫描二维码！");
            return 2;
        }
    }
}
