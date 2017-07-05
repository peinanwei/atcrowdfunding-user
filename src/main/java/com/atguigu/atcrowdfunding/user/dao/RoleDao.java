package com.atguigu.atcrowdfunding.user.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.atguigu.atcrowdfunding.common.bean.Datas;
import com.atguigu.atcrowdfunding.common.bean.Permission;
import com.atguigu.atcrowdfunding.common.bean.Role;
import com.atguigu.atcrowdfunding.common.bean.RolePermission;

@Repository 
public interface RoleDao {

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

	void insertPermission(RolePermission rp);

}
