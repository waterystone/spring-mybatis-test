package com.adu.spring_test.mybatis.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.adu.spring_test.mybatis.model.UserInfo;

@Repository
public interface UserInfoMapper {

    /**
     * 根据id获取用户信息
     * 
     * @param id
     * @return
     */
    UserInfo getUserById(@Param("id") int id);

    /**
     * 根据id获取一批用户信息
     * 
     * @param ids
     * @return
     */
    List<UserInfo> getUsersByIds(@Param("ids") List<Integer> ids);

    /**
     * 获取某段时间插入的用户
     * 
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     */
    List<UserInfo> getUsersBetweenTime(@Param("startTime") Date startTime, @Param("endTime") Date endTime);

    /**
     * 添加用户
     *
     * @param user
     * @return
     */
    int addUser(UserInfo user);

    /**
     * 添加一批用户
     *
     * @param userInfoList
     * @return
     */
    int addUsers(@Param("userInfoList") List<UserInfo> userInfoList);

    /**
     * 根据id删除用户
     * 
     * @param id
     * @return
     */
    int deleteById(@Param("id") int id);
}
