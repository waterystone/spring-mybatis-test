package com.adu.spring_test.mybatis.dao;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.ibatis.session.RowBounds;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adu.spring_test.mybatis.BaseTest;
import com.adu.spring_test.mybatis.model.ProfInfo;
import com.adu.spring_test.mybatis.model.UserInfo;

public class UserInfoMapperTest extends BaseTest {
    @Resource
    private UserInfoMapper userInfoMapper;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Test
    public void queryUserById() {
        long id = 1;
        UserInfo user = userInfoMapper.queryUserById(id);
        logger.debug("user=" + user);
    }

    @Test
    public void queryUserById2() {
        long id = 1;
        UserInfo user = userInfoMapper.queryUserById2(id);
        logger.debug("user=" + user);
    }

    @Test
    public void queryUsersByIds() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L, 4L);

        Map<Long, UserInfo> userId2userInfoMap = userInfoMapper.queryUsersByIds(ids);

        for (long id : ids) {
            logger.debug("id={},userInfo={}", id, userId2userInfoMap.get(id));
        }
    }

    @Test
    public void queryUserNamesByIds() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L, 4L);

        Map<Long, String> id2usernameMap = userInfoMapper.queryUserNamesByIds(ids);

        for (Long id : ids) {
            logger.debug("id={},username={}", id, id2usernameMap.get(id));
        }
    }

    @Test
    public void queryProfInfosByIds() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L, 4L);

        Map<Long, ProfInfo> id2profInfoMap = userInfoMapper.queryProfInfosByIds(ids);

        for (Long id : ids) {
            logger.debug("id={},profInfo={}", id, id2profInfoMap.get(id));
        }
    }

    @Test
    public void queryCreateTimesByIds() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L, 4L);

        Map<Long, Date> id2createTimeMap = userInfoMapper.queryCreateTimesByIds(ids);

        for (Long id : ids) {
            logger.debug("id={},createTime={}", id, id2createTimeMap.get(id));
        }
    }

    @Test
    public void queryUsersBetweenTime() {
        Date now = new Date(), start = DateUtils.addDays(now, -365);
        int offset = 0, limit = 10;
        Map<Long, UserInfo> userId2userInfoMap = userInfoMapper.queryUsersBetweenTime(start, now,
                new RowBounds(offset, limit));

        for (Map.Entry<Long, UserInfo> entry : userId2userInfoMap.entrySet()) {
            logger.debug("id={},userInfo={}", entry.getKey(), entry.getValue());
        }
    }

    @Test
    public void addUser() {
        UserInfo userInfo = mockUserInfo();
        int res = userInfoMapper.addUser(userInfo);
        logger.debug("userInfo={},res={}", userInfo, res);
    }

    @Test
    public void addUser2() {
        UserInfo userInfo = mockUserInfo();
        int res = userInfoMapper.addUser2(userInfo);
        logger.debug("userInfo={},res={}", userInfo, res);
    }

    @Test
    public void addUsers() {
        List<UserInfo> users = mockUserInfos(3);

        int res = userInfoMapper.addUsers(users);
        logger.debug("res=" + res);
    }

    @Test
    public void addUsers2() {
        List<UserInfo> users = mockUserInfos(3);

        int res = userInfoMapper.addUsers2(users);
        logger.debug("res=" + res);
    }

    @Test
    public void deleteById() {
        long id = 1L;
        int res = userInfoMapper.deleteById(id);
        logger.debug("res=" + res);

    }

}
