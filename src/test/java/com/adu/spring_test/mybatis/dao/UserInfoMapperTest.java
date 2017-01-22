package com.adu.spring_test.mybatis.dao;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.session.RowBounds;
import org.junit.Test;

import com.adu.spring_test.mybatis.BaseTest;
import com.adu.spring_test.mybatis.model.ProfInfo;
import com.adu.spring_test.mybatis.model.UserInfo;
import com.google.common.collect.Lists;

public class UserInfoMapperTest extends BaseTest {
    @Resource
    private UserInfoMapper userInfoMapper;

    @Test
    public void queryUserById() {
        long id = 2;
        UserInfo user = userInfoMapper.queryUserById(id);
        logger.debug("user={}", user);
    }

    @Test
    public void queryUserById2() {
        long id = 1;
        UserInfo user = userInfoMapper.queryUserById2(id);
        logger.debug("user={}", user);
    }

    @Test
    public void queryUsersByIds() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L, 4L);

        Map<Long, UserInfo> userId2userInfoMap = userInfoMapper.queryUsersByIds(ids);

        print(userId2userInfoMap);
    }

    @Test
    public void queryUserNamesByIds() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L, 4L);

        Map<Long, String> id2usernameMap = userInfoMapper.queryUserNamesByIds(ids);

        print(id2usernameMap);
    }

    @Test
    public void queryProfInfosByIds() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L, 4L);

        Map<Long, ProfInfo> id2profInfoMap = userInfoMapper.queryProfInfosByIds(ids);

        print(id2profInfoMap);
    }

    @Test
    public void queryCreateTimesByIds() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L, 4L);

        Map<Long, Date> id2createTimeMap = userInfoMapper.queryCreateTimesByIds(ids);

        print(id2createTimeMap);
    }

    @Test
    public void queryUsersBetweenTime() {
        Date now = new Date(), start = DateUtils.addDays(now, -365);
        int offset = 0, limit = 3;
        Map<Long, UserInfo> userId2userInfoMap = userInfoMapper.queryUsersBetweenTime(start, now,
                new RowBounds(offset, limit));

        print(userId2userInfoMap);
    }

    @Test
    public void queryUsersBetweenTime1() {
        Date now = new Date(), start = DateUtils.addDays(now, -365);
        int offset = 0, limit = 10;
        Cursor<UserInfo> userInfoCursor = userInfoMapper.queryUsersBetweenTime1(start, now,
                new RowBounds(offset, limit));

        print(userInfoCursor);
    }

    @Test
    public void saveUser() {
        UserInfo userInfo = mockUserInfo();
        int res = userInfoMapper.saveUser(userInfo);
        logger.debug("userInfo={},res={}", userInfo, res);
    }

    @Test
    public void saveUser2() {
        UserInfo userInfo = mockUserInfo();
        int res = userInfoMapper.saveUser2(userInfo);
        logger.debug("userInfo={},res={}", userInfo, res);
    }

    @Test
    public void saveUsers() {
        List<UserInfo> users = mockUserInfos(3);

        int res = userInfoMapper.saveUsers(users);
        logger.debug("res={}", res);
    }

    @Test
    public void saveUsers2() {
        List<UserInfo> users = mockUserInfos(3);

        int res = userInfoMapper.saveUsers2(users);
        logger.debug("res={}", res);
    }

    @Test
    public void deleteById() {
        long id = 1L;
        int res = userInfoMapper.deleteById(id);
        logger.debug("res={}", res);
    }

    @Test
    public void deleteByIds() {
        List<Long> idList = Lists.newArrayList(10000L, 10001L);
        int res = userInfoMapper.deleteByIds(idList);
        logger.debug("res={}", res);

    }

}
