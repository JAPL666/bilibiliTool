package com.warma.bilibili.entity;

import java.util.HashMap;

public class BiLiBiLiInfoEntity {
    Integer uid;
    String name;
    String face;
    Integer level;
    String sex;
    Integer code;
    String cookies;
    HashMap<String, String> cookieMap;

    public HashMap<String, String> getCookieMap() {
        return cookieMap;
    }

    public void setCookieMap(HashMap<String, String> cookieMap) {
        this.cookieMap = cookieMap;
    }

    public String getCookies() {
        return cookies;
    }

    public void setCookies(String cookies) {
        this.cookies = cookies;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFace() {
        return face;
    }

    public void setFace(String face) {
        this.face = face;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
