package com.warma.bilibili.entity;

import java.io.Serializable;

public class BiLiBiLiEntity implements Serializable {
    String dynamicId;
    String host_uid;
    String myuid;

    public String getHost_uid() {
        return host_uid;
    }

    public void setHost_uid(String host_uid) {
        this.host_uid = host_uid;
    }

    public String getDynamicId() {
        return dynamicId;
    }

    public void setDynamicId(String dynamicId) {
        this.dynamicId = dynamicId;
    }

    public String getMyuid() {
        return myuid;
    }

    public void setMyuid(String myuid) {
        this.myuid = myuid;
    }
}
