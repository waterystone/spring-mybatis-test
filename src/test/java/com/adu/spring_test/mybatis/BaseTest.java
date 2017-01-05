package com.adu.spring_test.mybatis;

import java.util.List;
import java.util.Random;

import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.adu.spring_test.mybatis.enums.SexEnum;
import com.adu.spring_test.mybatis.model.ProfInfo;
import com.adu.spring_test.mybatis.model.UserInfo;
import com.adu.spring_test.mybatis.util.CodeEnumUtil;
import com.google.common.collect.Lists;

/**
 * @author yunjie.du
 * @date 2017/1/5 16:25
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class BaseTest {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    protected UserInfo mockUserInfo() {
        UserInfo res = new UserInfo();
        res.setUserName("adu-" + System.currentTimeMillis());
        res.setSex(CodeEnumUtil.codeOf(SexEnum.class, (int) System.currentTimeMillis() % 3));
        res.setAge(20 + new Random().nextInt(20));
        res.setProfInfo(new ProfInfo(170 + 10 * new Random().nextDouble(), 80 + 10 * new Random().nextDouble(),
                Lists.newArrayList("台球", "乒乓球")));
        return res;
    }

    protected List<UserInfo> mockUserInfos(int count) {
        List<UserInfo> res = Lists.newArrayList();
        for (int i = 0; i < count; i++) {
            res.add(mockUserInfo());
        }
        return res;
    }
}
