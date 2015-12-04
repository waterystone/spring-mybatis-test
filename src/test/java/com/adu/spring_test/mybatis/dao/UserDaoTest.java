package com.adu.spring_test.mybatis.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.adu.spring_test.mybatis.model.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext-data.xml")
public class UserDaoTest {
	@Autowired
	private UserMapper userDao;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Test
	public void addUser() {
		User user = new User("adu", 27);
		int res = userDao.addUser(user);
		logger.debug("res=" + res);
	}

	@Test
	public void addUsers() {
		List<User> users = new ArrayList<User>();
		users.add(new User("zhangsan", 24));
		users.add(new User("lisi", 23));
		users.add(new User("wangwu", 18));

		int res = userDao.addUsers(users);
		logger.debug("res=" + res);
	}

	@Test
	public void getUserById() {
		int id = 1;
		User user = userDao.getUserById(id);
		logger.debug("user=" + user);
	}

	@Test
	public void getUsersByIds() {
		List<Integer> ids = Arrays.asList(1, 2, 3, 4);

		List<User> users = userDao.getUsersByIds(ids);

		for (User user : users) {
			logger.debug(user.toString());
		}
	}

	@Test
	public void getUsersBetweenTime() {
		long period = 60 * 60 * 1000;
		long now = System.currentTimeMillis();
		long start = now - period;
		List<User> users = userDao.getUsersBetweenTime(new Date(start), new Date(now));

		for (User user : users) {
			logger.debug(user.toString());
		}
	}

	@Test
	public void updateUser() {
		int id = 1;
		User user = userDao.getUserById(id);
		if (user == null) {
			return;
		}
		logger.debug("user={}", user);

		user.setUserName("updateTest");
		user.setAge(77);
		int res = userDao.updateUser(user);
		logger.debug("res=" + res);
	}

	@Test
	public void deleteById() {
		int id = 1;
		int res = userDao.deleteById(id);
		logger.debug("res=" + res);

	}
}
