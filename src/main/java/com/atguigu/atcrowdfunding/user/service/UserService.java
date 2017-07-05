package com.atguigu.atcrowdfunding.user.service;

import java.util.List;
import java.util.Map;

import com.atguigu.atcrowdfunding.common.bean.Datas;
import com.atguigu.atcrowdfunding.common.bean.Permission;
import com.atguigu.atcrowdfunding.common.bean.User;

public interface UserService {


	public User queryUser(Map<String, Object> map);

	public int saveUser(Map<String, Object> map);

	public List<User> pageQuery(Map<String, Object> paraMap);

	public int queryCount(Map<String, Object>  paraMap);

	public void insertUser(User user);

	public User queryUserById(Integer id);

	public int updateUser(User user);

	public int deleteUser(Integer id);

	public int deleteUsers(Datas ds);
	/**
	 * 获取提示信息
	 * @return
	 */
	public String getMessage(Integer messageId);
	/**
	 * 
	 * 修改提示信息
	 * @param message
	 * @return
	 */
	public void editMessage(Map<String, Object> paramMap);

	public List<Integer> queryRolesByUserId(Integer userId);

	public int insertUserRoles(Integer userId, Datas ds);

	public int deleteUserRoles(Integer userId, Datas ds);

	/**
	 * 根据用户id获取权限信息
	 * @param id
	 * @return
	 */
	public List<Permission> getPermissionidsById(Integer id);
}
