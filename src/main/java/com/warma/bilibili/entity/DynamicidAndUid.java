package com.warma.bilibili.entity;

public class DynamicidAndUid {
    String dynamicId;
    String uid;

    public String getDynamicId() {
        return dynamicId;
    }

    public void setDynamicId(String dynamicId) {
        this.dynamicId = dynamicId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public String toString() {
        return "DynamicidAndUid{" +
                "dynamicId='" + dynamicId + '\'' +
                ", uid='" + uid + '\'' +
                '}';
    }
}
