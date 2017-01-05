package com.adu.spring_test.mybatis.service.impl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.adu.spring_test.mybatis.dao.UserInfoMapper;
import com.adu.spring_test.mybatis.model.UserInfo;
import com.adu.spring_test.mybatis.service.UserInfoService;
import com.google.common.collect.Lists;

/**
 * @author yunjie.du
 * @date 2017/1/5 16:22
 */
@Service
public class UserInfoServiceImpl implements UserInfoService {
    @Resource
    private UserInfoMapper userInfoMapper;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Transactional()
    @Override
    public int addUsers(UserInfo userInfo1, UserInfo userInfo2) {
        userInfoMapper.addUser(userInfo1);
        Lists.newArrayList().subList(0, 10);//throw IndexOutOfBoundsException
        userInfoMapper.addUser(userInfo2);
        return 2;

    }
}
