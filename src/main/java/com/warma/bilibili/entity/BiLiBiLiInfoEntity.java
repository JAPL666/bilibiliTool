package com.warma.bilibili.entity;

public class BiLiBiLiInfoEntity {
    Integer uid;
    String name;
    String face;
    Integer level;
    String sex;
    Integer code;
    String cookies;
    String cookieMap;


    public String getCookieMap() {
        return cookieMap;
    }

    public void setCookieMap(String cookieMap) {
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

    @Override
    public String toString() {
        return "BiLiBiLiInfoEntity{" +
                "uid=" + uid +
                ", name='" + name + '\'' +
                ", face='" + face + '\'' +
                ", level=" + level +
                ", sex='" + sex + '\'' +
                ", code=" + code +
                ", cookies='" + cookies + '\'' +
                ", cookieMap='" + cookieMap + '\'' +
                '}';
    }
}
