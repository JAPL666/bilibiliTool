package com.warma.bilibili.entity;

import java.io.Serializable;
import java.util.HashMap;

public class ResultEntity implements Serializable {
    public String result;
    public String cookies;
    public HashMap<String,String> cookieMap;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getCookies() {
        return cookies;
    }

    public void setCookies(String cookies) {
        this.cookies = cookies;
    }

    public HashMap<String, String> getCookieMap() {
        return cookieMap;
    }

    public void setCookieMap(HashMap<String, String> cookieMap) {
        this.cookieMap = cookieMap;
    }

    @Override
    public String toString() {
        return "ResultEntity{" +
                "result='" + result + '\'' +
                ", cookies='" + cookies + '\'' +
                ", cookieMap=" + cookieMap +
                '}';
    }
}
