package com.atguigu.atcrowdfunding.user.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.atguigu.atcrowdfunding.common.bean.Datas;
import com.atguigu.atcrowdfunding.common.bean.Permission;
import com.atguigu.atcrowdfunding.common.bean.User;
import com.atguigu.atcrowdfunding.common.bean.UserRole;
import com.atguigu.atcrowdfunding.user.dao.UserDao;
import com.atguigu.atcrowdfunding.user.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userDao;

	@Transactional
	public User queryUser(Map<String, Object> map) {
		return userDao.queryUser(map);

	}

	@Transactional
	public int saveUser(Map<String, Object> map) {
		return userDao.saveUser(map);
	}

	public List<User> pageQuery(Map<String, Object> paraMap) {
		return userDao.pageQuery(paraMap);
	}

	public int queryCount(Map<String, Object> paraMap) {
		return userDao.queryCount(paraMap);
	}

	public void insertUser(User user) {
		userDao.insertUser(user);

	}

	public User queryUserById(Integer id) {
		return userDao.queryUserById(id);
	}

	public int updateUser(User user) {

		return userDao.updateUser(user);
	}

	public int deleteUser(Integer id) {
		return userDao.deleteUser(id);
	}

	public int deleteUsers(Datas ds) {
		return userDao.deleteUsers(ds);
	}

	public String getMessage(Integer messageId) {
		return userDao.getMessage(messageId);
	}

	public void editMessage(Map<String, Object> paramMap) {
		userDao.editMessage(paramMap);
	}

	public List<Integer> queryRolesByUserId(Integer userId) {
		return userDao.queryRolesByUserId(userId);
	}

	public int insertUserRoles(Integer userId, Datas ds) {
		int count = 0;
		for (Integer roleId : ds.getDataIds()) {
			UserRole ur = new UserRole();
			ur.setRoleId(roleId);
			ur.setUserId(userId);
			count = count + userDao.insertUserRole(ur);
		}
		return count;
	}

	public int deleteUserRoles(Integer userId, Datas ds) {
		Map<String, Object> paramMap = new HashMap<String, Object>();

		paramMap.put("userId", userId);
		paramMap.put("roleIds", ds.getDataIds());

		return userDao.deleteUserRoles(paramMap);
	}

	public List<Permission> getPermissionidsById(Integer id) {
		return userDao.getPermissionidsById(id);
	}
}
