package com.warma.bilibili.mapper;

import com.warma.bilibili.entity.BiLiBiLiInfoEntity;
import com.warma.bilibili.entity.CommentEntity;
import com.warma.bilibili.entity.DynamicidAndUid;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface BiLiBiLiMapper {
    //插入登录信息
    @Insert("insert into userinfo(uid,cookies,name,cookieMap) values(#{uid},#{cookies},#{name},#{cookieMap})")
    int insertCookies(BiLiBiLiInfoEntity entity);

    //查询登录信息
    @Select("select * from bilibili.userinfo")
    List<BiLiBiLiInfoEntity> findUserInfo();

    //插入动态ID和UID
    @Insert("insert ignore into dynamicid_and_uid(dynamicId,uid) values(#{dynamicId},#{uid})")
    int insertDynamicidAndUid(DynamicidAndUid dynamicidAndUid);


    //查询动态ID和UID
    @Select("select * from dynamicid_and_uid")
    List<DynamicidAndUid> findDynamicidAndUid();

    //查询所有评论
    @Select("select * from comment")
    List<CommentEntity> findAllComment();


}
