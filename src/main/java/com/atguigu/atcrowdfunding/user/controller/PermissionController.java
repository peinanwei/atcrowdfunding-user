package com.atguigu.atcrowdfunding.user.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.atguigu.atcrowdfunding.common.bean.AJAXResult;
import com.atguigu.atcrowdfunding.common.bean.Permission;
import com.atguigu.atcrowdfunding.user.service.PermissionService;


@Controller
@RequestMapping(value="/permission")
public class PermissionController {

	@Autowired
	private PermissionService permissionService;
	
	@RequestMapping(value="/index")
	public String per(){
		
		return "permission/index";
	}
	
	@RequestMapping(value="/add")
	public String add(){
		
		return "permission/add";
	}
	
	/**
	 * 删除节点
	 */
	@ResponseBody
	@RequestMapping(value="/deleteNode")
	public Object deleteNode(Integer id){
		
		int count = permissionService.delteNode(id);
		
		return true;
	}
	
	/**
	 * 添加许可
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/insert")
	public Object insert(Permission permission ){
		
		int count = permissionService.insert(permission);
		
		return true;
	}
	
	
	@RequestMapping(value="/edit")
	public Object edit(Integer id,Model model ){
		
		Permission permission = permissionService.getPermissinById(id);
		model.addAttribute("permission", permission);
		return "permission/edit";
	}
	
	/**
	 * 修改许可信息
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/update")
	public Object update(Permission permission ){
		
		int count = permissionService.update(permission);
		
		return true;
	}
	
	@ResponseBody
	@RequestMapping(value="loadData")
	public Object loadData(){
		AJAXResult result = new AJAXResult();
		
//		try {
//			
//			
//			List<Permission> rootPermissions = permissionService.queryRootPermisssions();
//			for (Permission rootPermission : rootPermissions) {
//				List<Permission> permissions =permissionService.queryChildrenPermissionById(rootPermission.getId());
//				rootPermission.setChildren(permissions);
//			}
//			result.setDatas(rootPermissions);
//			result.setSuccess(true);
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//			result.setSuccess(false);
//		}
		
		List<Permission> rootPermissions = new ArrayList<Permission>();
		List<Permission> permissions =  permissionService.queryAll();
	
	for (Permission permission : permissions) {
		
		//将当前节点设置为子节点,根据子节点去查询父节点
		Permission children = permission;
		if ( permission.getPid() == 0) {
			rootPermissions.add(permission);
		}else{
			for (Permission innerPermission : permissions) {
				//将内部的permission和子节点的pid相比较,如果相等,就证明两者有绑定的关系,
				//当前节点就是父节点
				if(children.getPid() == innerPermission.getId()){
					Permission parent = innerPermission;
					
					//绑定父子关系,因为此时已经把子节点放进去了,此时在父节点子节点集合中就有了子节点的信息,
					
					parent.getChildren().add(children);
					break;
				};
				
			}
		}
		
	}
	
		//使用map集合将id域许可数据进行绑定
//		Map<Integer,Permission> permissionMap = new HashMap<Integer,Permission>();
//		for (Permission permission : permissions) {
//			permissionMap.put(permission.getId(), permission);
//		}
//		
//		for (Permission permission : permissions) {
//			
//			//将当前节点设置为子节点,根据子节点去查询父节点
//			Permission child = permission;
//			if ( permission.getPid() == 0) {
//				rootPermissions.add(permission);
//			}else{
//				// 父节点
//				Permission parent = permissionMap.get(child.getPid());
//				// 组合父子节点的关系
//				parent.getChildren().add(child);
//			}
//			
//		}
	
//		result.setDatas(rootPermissions);
		return rootPermissions;
	}
	
// 使用递归方式读取许可数据，但是效率比较低，所以需要进行改善
//	Permission p = new Permission();
//	p.setId(0);
//	queryChildPermissions(p);
	
	/**
	 * 使用递归的方法查询节点数据
	 * 	1. 方法调用自身 ： 查询父节点的子节点数据
	 *  2. 方法调用时，参数应该规律
	 *  3. 方法一定要有跳出的逻辑
	 */
	private void queryChildPermissions( Permission parent ) {
		parent.setOpen(true);
		System.out.println( "1111111111" );
		// 查询子节点数据集合
		// pid = parent id
		List<Permission> childrenPermissions =
			permissionService.queryChildrenPermissionById(parent.getId());
		
		for ( Permission permission : childrenPermissions ) {
			queryChildPermissions(permission);
		}
		
		// 组合父子节点的关系
		parent.setChildren(childrenPermissions);
	}
}
