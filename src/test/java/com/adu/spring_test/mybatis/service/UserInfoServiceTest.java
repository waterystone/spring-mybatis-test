package com.adu.spring_test.mybatis.service;

import com.adu.spring_test.mybatis.BaseTest;
import com.adu.spring_test.mybatis.model.UserInfo;
import org.junit.Test;

import javax.annotation.Resource;

/**
 * @author yunjie.du
 * @date 2017/1/5 16:25
 */
public class UserInfoServiceTest extends BaseTest {
    @Resource
    private UserInfoService userInfoService;

    @Test
    public void addUsers(){
        UserInfo userInfo1=mockUserInfo();
        UserInfo userInfo2=mockUserInfo();
        userInfoService.addUsers(userInfo1,userInfo2);
        logger.debug("end");
    }
}
