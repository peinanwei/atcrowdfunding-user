package com.atguigu.atcrowdfunding.user.dao;

import java.util.List;

import com.atguigu.atcrowdfunding.common.bean.Permission;

public interface PermissionDao {

	List<Permission> queryRootPermisssions();

	List<Permission> queryChildrenPermissionById(Integer id);

	List<Permission> queryAll();

	int insert(Permission permission);

	Permission getPermissinById(Integer id);

	int update(Permission permission);

	int delteNode(Integer id);

}
