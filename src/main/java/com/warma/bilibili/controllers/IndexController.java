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

        assert loginResult != null;
        JSONObject json = new JSONObject(loginResult.getResult());
        JSONObject data = json.getJSONObject("data");
        String url = data.getString("url");
        String oauthKey = data.getString("oauthKey");

        request.getSession().setAttribute("oauthKey",oauthKey);
        String base64 = QRCode.createQRCodeImageBase64(url, 180, 180);
        //String base64="data:image/jpeg;base64,iVBORw0KGgoAAAANSUhEUgAAALQAAAC0AQAAAAAVtjufAAABeUlEQVR42u2XUW4CQQxDfYPc/5a+QRo7O1uo+odpf1ghlnmDFJJ1PAH960V8+F9xAMXuIls3LaO8tOBsFRr0MstRNXfOppCWcT6ImA/U5zdw5TRVm9K9gc/Lmfl511M9E1wP+uF60kOC77WVa/zU8+t84jjy0MkOnOJFORVzg217IMylJrrtJi+9KsrrCIos0ZNXitORj2Z56zbFZUtQNvBTZ6Oj3O+zxfKX1IdRrmTW+aDw2oly+ZGt6dhfZfnkJEXptrZ96hbiqhT3XJCC9UOifDboYiktla2zfKu2EnYPIsvLVq2GhsLfvpTipzUm9FALIMsnkg9lXP7XYX4lpurNd77PoxTX43Z7X+536hbi1bVzkcYuNR4ry6svU6WP5eow38FxzU8SqI5yn/rWLdXfa95BvnOLHKMk26qneeZ1XjuNylbZHi46yy3cnbfU2g/nTpBbtxq+7r5Oclit9mw2wlzShVe8DqIod6muqdRNgSz//M/9R/4F9xUNj2KSMu8AAAAASUVORK5CYII=";
        model.addAttribute("base64",base64);
        return "login";
    }
}
