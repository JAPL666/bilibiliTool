package com.warma.bilibili.controllers;

import com.warma.bilibili.entity.ResultEntity;
import com.warma.bilibili.utils.QRCode;
import com.warma.bilibili.utils.Warma;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@RestController
public class LoginController {

    @RequestMapping("getloginQRCode")
    public Object getloginQRCode(HttpServletRequest request){

        String loginUrl="https://passport.bilibili.com/qrcode/getLoginUrl";
        ResultEntity loginResult = Warma.get(loginUrl, new HashMap<>());

        assert loginResult != null;
        JSONObject json = new JSONObject(loginResult.getResult());
        JSONObject data = json.getJSONObject("data");
        String url = data.getString("url");
        String oauthKey = data.getString("oauthKey");

        request.getSession().setAttribute("oauthKey",oauthKey);
        return QRCode.createQRCodeImageBase64(url, 180, 180);
    }

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

            return res;
        }else{
            System.out.println("请扫描二维码！");
            return "请扫描二维码！";
        }
    }
}
