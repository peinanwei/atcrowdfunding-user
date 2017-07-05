package com.atguigu.atcrowdfunding.user.service;

import java.util.List;
import java.util.Map;

import com.atguigu.atcrowdfunding.common.bean.Datas;
import com.atguigu.atcrowdfunding.common.bean.Permission;
import com.atguigu.atcrowdfunding.common.bean.Role;

public interface RoleService {

	List<Role> pageQueryRole(Map<String, Object> paramMap);

	int queryCount(Map<String, Object> paramMap);

	void insertRole(Role role);

	int updateRole(Role role);

	Role queryId(Integer id);

	int delete(Integer id);

	int deletes(Datas datasRole);

	List<Role> queryAll();

	List<Integer> getPermissionsIdByRoleId(Integer id);

	List<Permission> getPermissionsById(Datas datas);

	int deletePermissions(Integer roleid);

	int insertPermission(Integer roleid, Datas datas);

}
