package com.adu.spring_test.mybatis.dao;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.adu.spring_test.mybatis.BaseTest;
import com.adu.spring_test.mybatis.model.UserInfo;

/**
 * @author yunjie.du
 * @date 2017/6/22 14:40
 */
public class UserInfoMapperSqlSessionTest extends BaseTest {
    @Autowired
    private SqlSessionFactoryBean sqlSessionFactoryBean;

    private SqlSession sqlSession;

    private UserInfoMapper userInfoMapper;

    @Test
    public void queryUserById() {
        long id = 2L;
        UserInfo res = userInfoMapper.queryUserById(id);
        logger.info("res={}", res);
    }

    @Before
    public void init() throws Exception {
        SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBean.getObject();
        sqlSession = sqlSessionFactory.openSession();
        userInfoMapper = sqlSession.getMapper(UserInfoMapper.class);
    }

    @After
    public void after() {
        if (sqlSession != null) {
            sqlSession.close();
        }
    }
}
