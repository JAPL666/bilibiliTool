package com.warma.bilibili.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @RequestMapping("getloginQRCode")
    public Object getloginQRCode(){

        return null;
    }

    @RequestMapping("checkLogin")
    public Object checkLogin(){

        return null;
    }
}
