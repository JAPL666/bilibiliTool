package com.warma.bilibili.controllers;

import com.warma.bilibili.entity.ResultEntity;
import com.warma.bilibili.utils.QRCode;
import com.warma.bilibili.utils.Warma;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@Controller
public class IndexController {

    @GetMapping("login")
    public Object login(HttpServletRequest request, Model model){

        String loginUrl="https://passport.bilibili.com/qrcode/getLoginUrl";
        ResultEntity loginResult = Warma.get(loginUrl, new HashMap<>());

        JSONObject json = new JSONObject(loginResult.getResult());
        JSONObject data = json.getJSONObject("data");
        String url = data.getString("url");
        String oauthKey = data.getString("oauthKey");

        request.getSession().setAttribute("oauthKey",oauthKey);
        String base64 = QRCode.createQRCodeImageBase64(url, 250, 250);
        model.addAttribute("base64",base64);
        return "login";
    }
}
