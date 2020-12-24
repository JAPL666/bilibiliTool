package com.warma.bilibili.mapper;

import com.warma.bilibili.entity.BiLiBiLiInfoEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface BiLiBiLiMapper {
    @Insert("insert into userinfo(uid,cookies,name) values(#{uid},#{cookies},#{name})")
    int insertCookies(BiLiBiLiInfoEntity entity);

    @Select("select * from userinfo")
    List<BiLiBiLiInfoEntity> findUserInfo();
}
