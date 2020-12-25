package com.warma.bilibili.service;

import com.warma.bilibili.entity.BiLiBiLiInfoEntity;
import com.warma.bilibili.entity.CommentEntity;
import com.warma.bilibili.entity.DynamicidAndUid;

import java.util.List;

public interface BiLiBiLiService {
    int insertCookies(BiLiBiLiInfoEntity entity);

    List<BiLiBiLiInfoEntity> findUserInfo();

    int insertDynamicidAndUid(DynamicidAndUid dynamicidAndUid);

    List<DynamicidAndUid> findDynamicidAndUid();

    List<CommentEntity> findAllComment();
}
