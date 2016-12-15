package com.adu.spring_test.mybatis.dao;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.ibatis.session.RowBounds;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.adu.spring_test.mybatis.enums.SexEnum;
import com.adu.spring_test.mybatis.model.ProfInfo;
import com.adu.spring_test.mybatis.model.UserInfo;
import com.adu.spring_test.mybatis.util.CodeEnumUtil;
import com.google.common.collect.Lists;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext-data.xml")
public class UserInfoMapperTest {
    @Autowired
    private UserInfoMapper userInfoMapper;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Test
    public void getUserById() {
        int id = 1;
        UserInfo user = userInfoMapper.getUserById(id);
        logger.debug("user=" + user);
    }

    @Test
    public void getUserById2() {
        int id = 1;
        UserInfo user = userInfoMapper.getUserById2(id);
        logger.debug("user=" + user);
    }

    @Test
    public void getUsersByIds() {
        List<Integer> ids = Arrays.asList(1, 2, 3, 4);

        List<UserInfo> users = userInfoMapper.getUsersByIds(ids);

        for (UserInfo user : users) {
            logger.debug(user.toString());
        }
    }

    @Test
    public void getUsersBetweenTime() {
        Date now = new Date(), start = DateUtils.addDays(now, -365);
        int offset = 0, limit = 10;
        List<UserInfo> users = userInfoMapper.getUsersBetweenTime(start, now, new RowBounds(offset, limit));

        for (UserInfo user : users) {
            logger.debug(user.toString());
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
        List<UserInfo> users = mockUserInfos();

        int res = userInfoMapper.addUsers(users);
        logger.debug("res=" + res);
    }

    @Test
    public void addUsers2() {
        List<UserInfo> users = mockUserInfos();

        int res = userInfoMapper.addUsers2(users);
        logger.debug("res=" + res);
    }

    @Test
    public void deleteById() {
        int id = 1;
        int res = userInfoMapper.deleteById(id);
        logger.debug("res=" + res);

    }

    private UserInfo mockUserInfo() {
        UserInfo res = new UserInfo();
        res.setUserName("adu-" + System.currentTimeMillis());
        res.setSex(CodeEnumUtil.codeOf(SexEnum.class, (int) System.currentTimeMillis() % 3));
        res.setAge(20 + new Random().nextInt(20));
        res.setProfInfo(new ProfInfo(170 + 10 * new Random().nextDouble(), 80 + 10 * new Random().nextDouble(),
                Lists.newArrayList("台球", "乒乓球")));
        return res;
    }

    private List<UserInfo> mockUserInfos() {
        List<UserInfo> res = Lists.newArrayList();
        for (int i = 1; i <= 3; i++) {
            res.add(mockUserInfo());
        }
        return res;
    }
}
