package com.atguigu.atcrowdfunding.user.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.atguigu.atcrowdfunding.common.bean.AJAXResult;
import com.atguigu.atcrowdfunding.common.bean.Datas;
import com.atguigu.atcrowdfunding.common.bean.Page;
import com.atguigu.atcrowdfunding.common.bean.Role;
import com.atguigu.atcrowdfunding.common.bean.User;
import com.atguigu.atcrowdfunding.user.service.RoleService;
import com.atguigu.atcrowdfunding.user.service.UserService;
import com.atguigu.atcrowdfunding.util.MD5Util;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserService userService;
	@Autowired
	private RoleService roleService;

	@RequestMapping("/index")
	public String index() {
		return "user/index";
	}
	
	/**
	 * 跳转找回密码页面
	 * @return
	 */
	@RequestMapping(value="/editpassword")
	public String ediPassword(){
		return "user/editpassword";
	}
	
	/**
	 * 跳转消息模板页面
	 * @return
	 */
	@RequestMapping(value="/message")
	public String message(){
		
		return"user/message";
	}
	
	/**
	 * 获取提示消息
	 * @param messageId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getMessage")
	public Object getMessage(Integer messageId){
		AJAXResult result = new AJAXResult();
		String message = userService.getMessage(messageId);
		result.setMessage(message);
		return result;
	}
	/**
	 * 修改提示消息
	 * @param id
	 * @param message
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/editMessage")
	public Object editMessage(Integer id, String message){
		AJAXResult result = new AJAXResult();
		Map<String,Object> paramMap = new HashMap<String,Object>();
		
		paramMap.put("messageid",id);
		paramMap.put("mes", message.replaceAll("\n", "<br>"));
		
		try {
			userService.editMessage(paramMap);
			result.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
		}
		return result;
	}
	
	/**
	 * 后台保存用户
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/insert")
	public Object insert(User user) {
		AJAXResult result = new AJAXResult();

		try {
			user.setUserpswd(MD5Util.digest("123456"));
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			user.setCreatetime(sdf.format(new Date()));
			userService.insertUser(user);
			result.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
		}

		return result;
	}

	/**
	 * 更新用户信息
	 * @param user
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/update")
	public Object update(User user) {
		AJAXResult result = new AJAXResult();

		try {
			int i = userService.updateUser(user);
			if (i == 1) {
				result.setSuccess(true);
			} else {
				result.setSuccess(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
		}

		return result;
	}

	/**
	 * 分页查询
	 * 
	 * @param pageText
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/pageQuery")
	public Object pageQuery(String pageText, Integer pageNo, Integer pageSize) {

		AJAXResult result = new AJAXResult();

		try {
			Map<String, Object> paraMap = new HashMap<String, Object>();
			paraMap.put("start", (pageNo - 1) * pageSize);
			paraMap.put("size", pageSize);
			paraMap.put("pageText", pageText);
			// 分页查询数据
			List<User> users = userService.pageQuery(paraMap);
			// 获取数据的总条数
			int acount = userService.queryCount(paraMap);
			int totalNo = 0;
			if (acount % pageSize == 0) {
				totalNo = acount / pageSize;
			} else {
				totalNo = acount / pageSize + 1;
			}

			Page<User> userPage = new Page<User>();

			userPage.setDatas(users);
			userPage.setPageNo(pageNo);
			userPage.setPageSize(pageSize);
			userPage.setTotalNo(totalNo);
			userPage.setTotalSize(acount);

			result.setPageObj(userPage);
			result.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
		}

		return result;
	}

	/**
	 * 删除用户
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/deleteUser")
	public Object deleteUser(Integer id) {
		AJAXResult result = new AJAXResult();

		try {
			int i = userService.deleteUser(id);
			if (i == 1) {
				result.setSuccess(true);
			} else {
				result.setSuccess(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
		}
		return result;
	}

	/**
	 * 批量删除
	 * @param ds
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/deletes")
	public Object deletes(Datas ds) {
		AJAXResult result = new AJAXResult();
		try {
			int i = userService.deleteUsers(ds);
			if (i == ds.getDatas().size()) {
				result.setSuccess(true);
			} else {
				result.setSuccess(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
		}
		return result;
	}
	/**
	 * 角色分配
	 * @param ds
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/assign")
	public Object assign(Integer userId,Datas ds) {
		AJAXResult result = new AJAXResult();
		try {
			int i = userService.insertUserRoles(userId,ds);
			if (i == ds.getDataIds().size()) {
				result.setSuccess(true);
			} else {
				result.setSuccess(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
		}
		return result;
	}
	/**
	 * 角色分配
	 * @param ds
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/deleteAssign")
	public Object deleteAssign(Integer userId,Datas ds) {
		AJAXResult result = new AJAXResult();
		try {
			int i = userService.deleteUserRoles(userId,ds);
			if (i == ds.getDataIds().size()) {
				result.setSuccess(true);
			} else {
				result.setSuccess(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
		}
		return result;
	}

	/**
	 * user/index跳转到user/add
	 * 
	 * @return
	 */
	@RequestMapping("/add")
	public String add() {
		return "user/add";

	}

	/**
	 * user/add跳转到user/edit
	 * 
	 * @return
	 */
	@RequestMapping("/edit")
	public String edit(Integer id, Model model) {
		User user = userService.queryUserById(id);
		model.addAttribute("user", user);
		return "user/edit";
	}

	/**
	 * user/index跳转到user/assignRole
	 * 
	 * @return
	 */
	@RequestMapping("/assignRole")
	public String assignRole(Integer id,Model model) {
		
		List<Role> roles = roleService.queryAll();
		List<Integer> roleIds = userService.queryRolesByUserId(id);
		
		List<Role> assignRoles = new ArrayList<Role>();
		List<Role> unAssignRoles = new ArrayList<Role>();
		
		for (Role role : roles) {
			if (roleIds.contains(role.getId())) {
				assignRoles.add(role);
			}else {
				unAssignRoles.add(role);
			}
		}
		
		User user = userService.queryUserById(id);
		model.addAttribute("user", user);
		
		model.addAttribute("assignRoles", assignRoles);
		model.addAttribute("unAssignRoles", unAssignRoles);
		
		return "user/assignRole";

	}

	@RequestMapping("/minecrowdfunding")
	public String minecrowdfunding() {

		return "user/minecrowdfunding";
	}

	@RequestMapping("/apply")
	public String apply() {

		return "user/apply";
	}

	@RequestMapping("/accttype")
	public String accttype() {

		return "user/accttype";
	}

	@RequestMapping("/apply-1")
	public String apply1() {
		return "user/apply-1";
	}

	@RequestMapping("/apply-2")
	public String apply2() {
		return "user/apply-2";
	}

	@RequestMapping("/apply-3")
	public String apply3() {
		return "user/apply-3";
	}

}
