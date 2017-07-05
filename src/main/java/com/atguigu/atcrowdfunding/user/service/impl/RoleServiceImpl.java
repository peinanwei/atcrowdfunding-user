package com.atguigu.atcrowdfunding.user.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atguigu.atcrowdfunding.common.bean.Datas;
import com.atguigu.atcrowdfunding.common.bean.Permission;
import com.atguigu.atcrowdfunding.common.bean.Role;
import com.atguigu.atcrowdfunding.common.bean.RolePermission;
import com.atguigu.atcrowdfunding.user.dao.RoleDao;
import com.atguigu.atcrowdfunding.user.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {

	@Autowired
	private RoleDao roleDao;
	
	public List<Role> pageQueryRole(Map<String, Object> paramMap) {
		return roleDao.pageQueryRole(paramMap);
	}

	public int queryCount(Map<String, Object> paramMap) {
		return roleDao.queryCount(paramMap);
	}

	public void insertRole(Role role) {
		roleDao.insertRole(role);
	}

	public int updateRole(Role role) {
		return roleDao.updateRole(role);
	}

	public Role queryId(Integer id) {
		return roleDao.queryId(id);
	}

	public int delete(Integer id) {
		return roleDao.delete(id);
	}

	public int deletes(Datas datasRole) {
		return roleDao.deletes(datasRole);
	}

	public List<Role> queryAll() {
		return roleDao.queryAll();
	}

	public List<Integer> getPermissionsIdByRoleId(Integer id) {
		return roleDao.getPermissionsIdByRoleId(id);
	}

	public List<Permission> getPermissionsById(Datas datas) {
		return roleDao.getPermissionsById(datas);
	}

	public int deletePermissions(Integer roleid) {
		return roleDao.deletePermissions(roleid);
	}

	public int insertPermission(Integer roleid, Datas datas) {
		int count =0;
		
		List<Integer> dataIds = datas.getDataIds();
		RolePermission rp = new RolePermission();
		rp.setRoleid(roleid);
		for (Integer perid : dataIds) {
			rp.setPerid(perid);
			roleDao.insertPermission(rp);
			count = count+1;
		}
		return count;
	}

}
