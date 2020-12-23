package com.warma.bilibili.service;

import com.warma.bilibili.entity.BiLiBiLiInfoEntity;

import java.util.List;

public interface BiLiBiLiService {
    int insertCookies(BiLiBiLiInfoEntity entity);
    List<BiLiBiLiInfoEntity> findUserInfo();
}
