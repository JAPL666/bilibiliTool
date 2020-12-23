package com.warma.bilibili.service.impl;

import com.warma.bilibili.entity.BiLiBiLiInfoEntity;
import com.warma.bilibili.mapper.BiLiBiLiMapper;
import com.warma.bilibili.service.BiLiBiLiService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("biLiBiLiServiceImpl")
public class BiLiBiLiServiceImpl implements BiLiBiLiService {
    @Resource
    BiLiBiLiMapper mapper;
    @Override
    public int insertCookies(BiLiBiLiInfoEntity entity) {
        return mapper.insertCookies(entity);
    }

    @Override
    public List<BiLiBiLiInfoEntity> findUserInfo() {
        return mapper.findUserInfo();
    }
}
