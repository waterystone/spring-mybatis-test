package com.adu.spring_test.mybatis.dao;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
        long id = 2;
        UserInfo user = userInfoMapper.queryUserById2(id);
        logger.debug("user={}", user);
    }

    @Test
    public void queryUserById2CacheTest() {
        long id = 2;
        for (int i = 0; i < 5; i++) {
            UserInfo user = userInfoMapper.queryUserById2(id);
            logger.debug("user={}", user);
        }
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
    public void queryUsersByUserNameAndOthers() {
        String userName = "adu-1474973034151";
        int sex = 1, age = 1;
        List<UserInfo> res = userInfoMapper.queryUsersByUserNameAndOthers(userName, sex, age);

        print(res);
    }

    @Test
    public void queryAgesByUserNames() {
        List<String> userNameList = Lists.newArrayList("adu-1474973034151", "adu-1474973344928");
        Map<String, Integer> userName2AgeMap = userInfoMapper.queryAgesByUserNames(userNameList);

        print(userName2AgeMap);
    }

    @Test
    public void queryProfInfosByIds() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L, 4L);

        Map<Long, ProfInfo> id2profInfoMap = userInfoMapper.queryProfInfosByIds(ids);

        print(id2profInfoMap);
    }

    @Test
    public void queryProfInfosByIds2() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L, 4L);

        Map<Long, String> id2profInfoMap = userInfoMapper.queryProfInfosByIds2(ids);

        print(id2profInfoMap);
    }

    @Test
    public void queryProfInfosByIds3() {
        List<BigInteger> ids = Lists.newArrayList(BigInteger.valueOf(1), BigInteger.valueOf(2), BigInteger.valueOf(3),
                BigInteger.valueOf(4));

        Map<BigInteger, ProfInfo> id2profInfoMap = userInfoMapper.queryProfInfosByIds3(ids);

        ProfInfo profInfo = id2profInfoMap.get(ids.get(1));

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
    public void queryUsersBetweenTime2() {
        Date now = new Date(), start = DateUtils.addDays(now, -365);
        int offset = 0, limit = 10;
        Set<String> userInfoCursor = userInfoMapper.queryUserNamesBetweenTime2(start, now,
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
