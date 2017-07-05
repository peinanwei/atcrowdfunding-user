package com.atguigu.atcrowdfunding.user.dao;

import java.util.List;
import java.util.Map;

import com.atguigu.atcrowdfunding.common.bean.Datas;
import com.atguigu.atcrowdfunding.common.bean.Permission;
import com.atguigu.atcrowdfunding.common.bean.User;
import com.atguigu.atcrowdfunding.common.bean.UserRole;

public interface UserDao {

	User queryUser(Map<String, Object> map);

	int saveUser(Map<String, Object> map);

	List<User> pageQuery(Map<String, Object> paraMap);

	int queryCount(Map<String, Object> paraMap);

	void insertUser(User user);

	User queryUserById(Integer id);

	int updateUser(User user);

	int deleteUser(Integer id);

	int deleteUsers(Datas ds);

	String getMessage(Integer messageId);

	void editMessage(Map<String, Object> paramMap);

	List<Integer> queryRolesByUserId(Integer userId);

	int insertUserRole(UserRole ur);

	int deleteUserRoles(Map<String, Object> paramMap);

	List<Permission> getPermissionidsById(Integer id);

}
