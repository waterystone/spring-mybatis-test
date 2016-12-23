package com.adu.spring_test.mybatis.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.adu.spring_test.mybatis.annotations.MapF2F;
import com.adu.spring_test.mybatis.model.UserInfo;

@Repository
public interface UserInfoMapper {

    /**
     * 根据id获取用户信息(在mapper.xml中指定typeHandler)
     * 
     * @param id
     * @return
     */
    UserInfo queryUserById(@Param("id") long id);

    /**
     * 根据id获取用户信息(在mybatis.xml中指定typeHandler)
     * 
     * @param id
     * @return
     */
    UserInfo queryUserById2(@Param("id") long id);

    /**
     * 根据id获取一批用户信息
     * 
     * @param ids
     * @return
     */
    @MapKey("id")
    Map<Long, UserInfo> queryUsersByIds(@Param("ids") List<Long> ids);

    /**
     * 批量获取用户姓名
     * 
     * @param ids
     * @return key为ID，value为username
     */
    @MapF2F()
    Map<Long, String> queryUserNamesByIds(@Param("ids") List<Long> ids);

    /**
     * 获取某段时间插入的用户
     * 
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     */
    @MapKey("id")
    Map<Long, UserInfo> queryUsersBetweenTime(@Param("startTime") Date startTime, @Param("endTime") Date endTime,
            @Param("rowBounds") RowBounds rowBounds);

    /**
     * 添加用户(在mapper.xml中指定typeHandler)
     *
     * @param user
     * @return
     */
    int addUser(UserInfo user);

    /**
     * 添加用户(在mybatis.xml中指定typeHandler)
     *
     * @param user
     * @return
     */
    int addUser2(UserInfo user);

    /**
     * 添加一批用户(在mapper.xml中指定typeHandler)
     *
     * @param userInfoList
     * @return
     */
    int addUsers(@Param("userInfoList") List<UserInfo> userInfoList);

    /**
     * 添加一批用户(在mybatis.xml中指定typeHandler)
     *
     * @param userInfoList
     * @return
     */
    int addUsers2(@Param("userInfoList") List<UserInfo> userInfoList);

    /**
     * 根据id删除用户
     * 
     * @param id
     * @return
     */
    int deleteById(@Param("id") long id);
}
