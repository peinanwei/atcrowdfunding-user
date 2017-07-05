package com.atguigu.atcrowdfunding.user.service;

import java.util.List;




import com.atguigu.atcrowdfunding.common.bean.Permission;

public interface PermissionService {

	List<Permission> queryRootPermisssions();

	List<Permission> queryChildrenPermissionById(Integer id);

	List<Permission> queryAll();

	int insert(Permission permission);

	int update(Permission permission);

	Permission getPermissinById(Integer id);

	int delteNode(Integer id);

}
