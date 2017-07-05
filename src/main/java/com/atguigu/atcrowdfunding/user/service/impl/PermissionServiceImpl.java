package com.atguigu.atcrowdfunding.user.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atguigu.atcrowdfunding.common.bean.Permission;
import com.atguigu.atcrowdfunding.user.dao.PermissionDao;
import com.atguigu.atcrowdfunding.user.service.PermissionService;

@Service
public class PermissionServiceImpl implements PermissionService {

	@Autowired
	private PermissionDao permissionDao;
	
	public List<Permission> queryRootPermisssions() {
		return permissionDao.queryRootPermisssions();
	}

	public List<Permission> queryChildrenPermissionById(Integer id) {
		return permissionDao.queryChildrenPermissionById(id);
	}

	public List<Permission> queryAll() {
		return permissionDao.queryAll();
	}

	public int insert(Permission permission) {
		return permissionDao.insert(permission);
	}

	public int update(Permission permission) {
		return permissionDao.update(permission);
	}

	public Permission getPermissinById(Integer id) {
		return permissionDao.getPermissinById(id);
	}

	public int delteNode(Integer id) {
		return permissionDao.delteNode(id);
	}

}
