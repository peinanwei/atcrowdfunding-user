package com.atguigu.atcrowdfunding.user.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.atguigu.atcrowdfunding.common.bean.AJAXResult;
import com.atguigu.atcrowdfunding.common.bean.Datas;
import com.atguigu.atcrowdfunding.common.bean.Page;
import com.atguigu.atcrowdfunding.common.bean.Permission;
import com.atguigu.atcrowdfunding.common.bean.Role;
import com.atguigu.atcrowdfunding.common.bean.User;
import com.atguigu.atcrowdfunding.user.service.PermissionService;
import com.atguigu.atcrowdfunding.user.service.RoleService;

@Controller()
@RequestMapping("/role")
public class RoleController {
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private PermissionService permissionService;
	
	@RequestMapping("/form")
	public String form(){
		return "role/form";
	}
	
	@RequestMapping("/role")
	public String role(){
		return "role/role";
	}
	
	@RequestMapping(value="/assignPermission")
	public String assignPermission(Integer roleid ,Model model){
		
		List<Integer> permissionsId = roleService.getPermissionsIdByRoleId(roleid);
		model.addAttribute("perids", permissionsId);
		
		return "role/assignPermission";
	}
	
	@ResponseBody
	@Transactional
	@RequestMapping(value="editData")
	public Object eidtData(Integer roleid, Datas datas,Model model ){
		
		AJAXResult result = new AJAXResult();
		List<Integer> list = datas.getDataIds();
		
		if(datas.getDataIds() == null	){
			int count = roleService.deletePermissions(roleid);
		}else{
			//先将数据库中和roleid的数据进行清空
			int count = roleService.deletePermissions(roleid);
			//再将新的数据插入进去
			int count2 = roleService.insertPermission(roleid,datas);
		}
		result.setSuccess(true);
		return result;
	}
	
	@ResponseBody
	@RequestMapping(value="getData")
	public Object getData( Integer roleid , Model model ){
		
		model.addAttribute("roleid", roleid);
		AJAXResult result = new AJAXResult();
		
		//获取roleid获取到权限信息的id
		List<Integer> permissionsId = roleService.getPermissionsIdByRoleId(roleid);
		
		result.setDatas(permissionsId);
		result.setSuccess(true);
		return permissionsId;
	}
	
	@ResponseBody
	@RequestMapping("/deletes")
	public Object deletes(Datas datasRole){
		AJAXResult result = new AJAXResult();
		try {
			int count = roleService.deletes(datasRole);
			if (count==datasRole.getDatasRole().size()) {
				result.setSuccess(true);
			}else{
				result.setSuccess(false);
			}
		} 
		 catch (Exception e) {
				e.printStackTrace();
				result.setSuccess(false);
			}
			return result;
	}
	
	
	@ResponseBody
	@RequestMapping("/delete")
	public Object delete(Integer id){
		AJAXResult result = new AJAXResult();
		try {
			int count = roleService.delete(id);
			if (count==1) {
				result.setSuccess(true);
			}else{
				result.setSuccess(false);
			}
		} 
		 catch (Exception e) {
				e.printStackTrace();
				result.setSuccess(false);
			}
			return result;
	}
	
	@RequestMapping("/edit")
	public String edit(Integer id,Model model){
		Role role = roleService.queryId(id);
		model.addAttribute("role", role);
		return "role/edit";
	}
	
	@ResponseBody
	@RequestMapping("/update")
	public Object update(Role role) {
		AJAXResult result = new AJAXResult();
		try {
			
			int count = roleService.updateRole(role);
			System.out.println(count);
			if (count==1) {
				result.setSuccess(true);
			}else{
				result.setSuccess(false);
			}
		} 
		 catch (Exception e) {
				e.printStackTrace();
				result.setSuccess(false);
			}
			return result;
	}
	
	@ResponseBody
	@RequestMapping("/insert")
	public Object insert(Role role){
		AJAXResult result = new AJAXResult();
		
		try {
			roleService.insertRole(role);
			result.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
		}
		return result;
	}
	
	@ResponseBody
	@RequestMapping("/pageQueryRole")
	public Object pageQueryRole(String pagetext, Integer pageNo,Integer pageSize){
		AJAXResult result = new AJAXResult();
		
		try {
			
			Map<String,Object> paramMap = new HashMap<String, Object>();
			
			paramMap.put("start", (pageNo-1)*pageSize);
			paramMap.put("size", pageSize);
			paramMap.put("pagetext", pagetext);
			List<Role> roles = roleService.pageQueryRole(paramMap);
			int count = roleService.queryCount(paramMap);
			int totalno = 0;
			if (count % pageSize ==0) {
				totalno= count/pageSize;
			}else {
				//总页数
				totalno=count/pageSize+1;
			}
			Page<Role> rolePage = new Page<Role>();
			//rolePage.setCount(count);
			rolePage.setDatas(roles);
			rolePage.setPageNo(pageNo);
			rolePage.setPageSize(pageSize);
			rolePage.setTotalNo(totalno);
			
			result.setPageObj(rolePage);
			result.setSuccess(true);
			
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
		}
		return  result;
	}
	
}
