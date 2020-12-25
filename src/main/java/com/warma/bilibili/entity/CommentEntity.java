package com.warma.bilibili.entity;

import java.io.Serializable;

public class CommentEntity implements Serializable {
    String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
