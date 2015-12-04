package com.adu.spring_test.mybatis.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.adu.spring_test.mybatis.model.User;

public interface UserMapper {
	/**
	 * 添加用户
	 * 
	 * @param user
	 * @return
	 */
	public int addUser(@Param("user") User user);

	/**
	 * 添加一批用户
	 * 
	 * @param users
	 * @return
	 */
	public int addUsers(@Param("list") List<User> users);

	/**
	 * 根据id获取用户信息
	 * 
	 * @param id
	 * @return
	 */
	public User getUserById(@Param("id") int id);

	/**
	 * 根据id获取一批用户信息
	 * 
	 * @param ids
	 * @return
	 */
	public List<User> getUsersByIds(@Param("ids") List<Integer> ids);

	/**
	 * 获取某段时间插入的用户
	 * 
	 * @param start
	 *            开始时间
	 * @param end
	 *            结束时间
	 * @return
	 */
	public List<User> getUsersBetweenTime(@Param("startTime") Date startTime, @Param("endTime") Date endTime);

	/**
	 * 更新用户信息(根据user的id)
	 * 
	 * @param user
	 * @return
	 */
	public int updateUser(@Param("user") User user);

	/**
	 * 根据id删除用户
	 * 
	 * @param id
	 * @return
	 */
	public int deleteById(@Param("id") int id);
}
